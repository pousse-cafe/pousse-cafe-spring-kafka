![Travis build status](https://travis-ci.org/pousse-cafe/pousse-cafe-spring-kafka.svg?branch=master)
![Maven status](https://maven-badges.herokuapp.com/maven-central/org.pousse-cafe-framework/pousse-cafe-spring-kafka/badge.svg)

# Pousse-Café Spring Kafka

This projects enables the use of [Spring Kakfa](https://github.com/spring-projects/spring-kafka) as messaging backend.
A JSON (Jackson-based) codec is being used for messages which are transmitted as text.

Your Spring configuration class should look like this (do not forget to include `poussecafe.spring` in your
package scan):

    @Configuration
    @ComponentScan(basePackages = { "poussecafe.spring" })
    public class AppConfiguration {
    
        @Bean
        public Bundles bundles(
                SpringKafkaMessaging messaging,
                Storage storage) {
            MessagingAndStorage messagingAndStorage = new MessagingAndStorage(messaging, storage);
            return new Bundles.Builder()
                // Register your bundles here using withBundle and use messagingAndStorage
                // when building them
                .build();
        }
    }

## Properties

- `poussecafe.spring.kafka.topic`: the topic used for subscription and publication (default is `pousse-cafe`)

## Configure your Maven project

Add the following snippet to your POM:

    <dependency>
        <groupId>org.pousse-cafe-framework</groupId>
        <artifactId>pousse-cafe-spring-kafka</artifactId>
        <version>${poussecafe.spring.kafka.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
        <version>${spring.kafka.version}</version>
    </dependency>
    <dependency>
        <groupId>org.pousse-cafe-framework</groupId>
        <artifactId>pousse-cafe-jackson</artifactId>
        <version>${poussecafe.jackson.version}</version>
    </dependency>
