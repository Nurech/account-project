package com.example;

import com.example.account.AccountApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AccountApplication.class, properties = {"spring.config.location=classpath:/"})
@ActiveProfiles("integtest")
@Testcontainers
@ContextConfiguration(initializers = {AccountApplicationBaseTest.Initializer.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AccountApplicationBaseTest {

    /**
     * To speed up tests, lets use static container,
     * it is used in all test classes that are executed inside one Spring Context
     * But to keep things convenient, we build the java module containers dynamically,
     * so if we update modules, we can test changes by running test again.
     * Gradle first copies required modules into account/src/test/resources when running task
     * 'integrationTest' and then we build containers.
     */

    private static PostgreSQLContainer<?> postgresqlContainer;
    private static RabbitMQContainer rabbitmqContainer;
    private static GenericContainer<?> commonContainer;
    private static GenericContainer<?> balanceContainer;
    private static GenericContainer<?> transactionContainer;


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            // Now we directly set the updated URL in the TestPropertyValues
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
                    "spring.datasource.username=postgres",
                    "spring.datasource.password=postgres",
                    "spring.liquibase.url=" + postgresqlContainer.getJdbcUrl(),
                    "spring.liquibase.user=postgres",
                    "spring.liquibase.password=postgres"
            ).applyTo(applicationContext.getEnvironment());
        }
    }


    @BeforeAll
    public static void setUpContainers() {

        Network network = Network.newNetwork();

        postgresqlContainer = new PostgreSQLContainer<>("postgres:15.6-alpine")
                .withNetwork(network)
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s"))
                .withStartupTimeout(Duration.ofSeconds(30))
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/changelog/sql"), "/docker-entrypoint-initdb.d/")
                .withNetworkAliases("postgres");

        rabbitmqContainer = new RabbitMQContainer("rabbitmq:3.13-alpine")
                .withNetwork(network)
                .withExposedPorts(5672)
                .withNetworkAliases("rabbitmq")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Server startup complete.*"))
                .withStartupTimeout(Duration.ofSeconds(30));


        // First start db and rabbitmq
        postgresqlContainer.start();
		rabbitmqContainer.start();


        transactionContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("transaction-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/transaction-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8083)
				.withNetworkAliases("transaction")
				.withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting TransactionApplication.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        commonContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("common-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/common-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8080)
				.withNetworkAliases("common")
				.withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting CommonApplication.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        balanceContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("balance-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/balance-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8082)
				.withNetworkAliases("balance")
				.withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting BalanceApplication.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        // Start all containers
        commonContainer.start();
        balanceContainer.start();
        transactionContainer.start();
    }


    @AfterAll
    public static void tearDownContainers() {
        postgresqlContainer.stop();
        rabbitmqContainer.stop();
        transactionContainer.stop();
        balanceContainer.stop();
        commonContainer.stop();
    }
}
