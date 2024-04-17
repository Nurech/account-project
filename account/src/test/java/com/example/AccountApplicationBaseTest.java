package com.example;

import com.example.account.AccountApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AccountApplication.class, properties = {"spring.config.location=classpath:/"})
@ActiveProfiles("integtest")
@Testcontainers
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

    private static Network network = Network.newNetwork();

    @Container
    public static final RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3-management")
            .withNetwork(network)
            .withNetworkAliases("rabbitmq")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("rabbitmq_test"))
            .withExposedPorts(5672, 15672);

    @Container
    public static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15.6-alpine")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres_test"))
            .withNetwork(network)
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withNetworkAliases("postgres")
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s"))
            .withStartupTimeout(Duration.ofSeconds(30))
            .withCopyFileToContainer(MountableFile.forClasspathResource("db/changelog/sql"), "/docker-entrypoint-initdb.d/");

    @Container
    public static GenericContainer<?> commonContainer = new GenericContainer<>("openjdk:17-jdk-slim")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("common_test"))
            .withNetwork(network)
            .withCopyFileToContainer(MountableFile.forClasspathResource("common-0.0.1-SNAPSHOT.jar"), "/tmp/")
            .withCommand("java", "-jar", "/tmp/common-0.0.1-SNAPSHOT.jar")
            .withExposedPorts(8080)
            .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Started CommonApplication.*"))
            .withStartupTimeout(Duration.ofSeconds(60))
            .dependsOn(rabbitMqContainer, postgresqlContainer);

    @Container
    public static GenericContainer<?> transactionContainer = new GenericContainer<>("openjdk:17-jdk-slim")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("transaction_test"))
            .withNetwork(network)
            .withCopyFileToContainer(MountableFile.forClasspathResource("transaction-0.0.1-SNAPSHOT.jar"), "/tmp/")
            .withCommand("java", "-jar", "/tmp/transaction-0.0.1-SNAPSHOT.jar")
            .withExposedPorts(8083)
            .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting TransactionApplication.*"))
            .withStartupTimeout(Duration.ofSeconds(60))
            .dependsOn(commonContainer);

    @Container
    public static GenericContainer<?> balanceContainer = new GenericContainer<>("openjdk:17-jdk-slim")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("balance_test"))
            .withNetwork(network)
            .withCopyFileToContainer(MountableFile.forClasspathResource("balance-0.0.1-SNAPSHOT.jar"), "/tmp/")
            .withCommand("java", "-jar", "/tmp/balance-0.0.1-SNAPSHOT.jar")
            .withExposedPorts(8082)
            .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/postgres")
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Starting BalanceApplication.*"))
            .withStartupTimeout(Duration.ofSeconds(60))
            .dependsOn(transactionContainer);


    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
        registry.add("spring.liquibase.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", () -> "postgres");
        registry.add("spring.liquibase.password", () -> "postgres");
        registry.add("spring.rabbitmq.port", () -> rabbitMqContainer.getMappedPort(5672).toString());
    }


    @AfterAll
    public static void tearDownContainers() {
        postgresqlContainer.stop();
        rabbitMqContainer.stop();
        transactionContainer.stop();
        balanceContainer.stop();
        commonContainer.stop();
    }
}
