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

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;


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
            // Containers are started before Spring Context is initialized
            // So we need to set the container values, for Spring to use them
            System.out.println("PostgreSQL URL: jdbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/postgres");
            System.out.println("RabbitMQ URL: amqp://localhost:" + rabbitmqContainer.getFirstMappedPort());

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
    public static void setUpContainers() throws IOException, InterruptedException {

        Network network = Network.newNetwork();

        postgresqlContainer = new PostgreSQLContainer<>("postgres:15.6-alpine")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres_test"))
                .withNetwork(network)
                .withNetworkAliases("postgres")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s"))
                .withStartupTimeout(Duration.ofSeconds(30))
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/changelog/sql"), "/docker-entrypoint-initdb.d/");

        rabbitmqContainer = new RabbitMQContainer("rabbitmq:3.13-alpine")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("rabbitmq_test"))
                .withNetwork(network)
                .withExposedPorts(5672, 15672)
                .withNetworkAliases("rabbitmq")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Server startup complete.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        rabbitmqContainer.setPortBindings(Arrays.asList("5672:5672", "15672:15672"));

        postgresqlContainer.start();
        rabbitmqContainer.start();

        System.out.println("Testing network connection to RabbitMQ:");
        System.out.println("Ping RabbitMQ from PostgreSQL container:");
        System.out.println(postgresqlContainer.execInContainer("ping", "-c", "4", "rabbitmq"));



        // First start db and rabbitmq
        postgresqlContainer.start();
		rabbitmqContainer.start();


        transactionContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("transaction_test"))
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("transaction-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/transaction-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8083)
                .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
                .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting TransactionApplication.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        commonContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("common_test"))
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("common-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/common-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8080)
                .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
                .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting CommonApplication.*"))
                .withStartupTimeout(Duration.ofSeconds(30));

        balanceContainer = new GenericContainer<>("openjdk:17-jdk-slim")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("balance_test"))
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forClasspathResource("balance-0.0.1-SNAPSHOT.jar"), "/tmp/")
                .withCommand("java", "-jar", "/tmp/balance-0.0.1-SNAPSHOT.jar")
                .withExposedPorts(8082)
                .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
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
