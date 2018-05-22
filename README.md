# Overview

Spring Cloud Stream is a framework for building highly scalable event-driven microservices connected with shared messaging systems.

This repository contains a basic example of usage Spring Cloud Streams with Kafka.

# Project Structure

This project consists of 3 Spring Boot microservices, Apache Kafka and Zookeeper.

Basically, this project covers the following scenario:

1) The client sends an order to the publisher using REST endpoint

2) The publisher receives the order and then sends it to a first queue in a message broker

3) The first subscriber will receive the message through the first queue, validate it and send it to a second queue

4) The second subscriber will receive the message through the second queue and log it to file

# How to run it

Start Kafka and Zookeeper. You can run them in docker or run your own instances. The project contains 
docker-compose file which starts Kafka and Zookeeper and exposes 9092 and 2181 ports respectively

In order to start docker containers from docker-compose file go to the root of the repository and execute the following:

```console
docker-compose up
```

Build each microservice with gradle:

```console
./gradlew clean build
```

Run each microservice using fat jar.

# How to use it

After bootstrapping microservices, Kafka and Zookeeper you could send order to orderproducer microservice, for this
send POST request to localhost:9000/order with request body:

```json
{
    "userName": "testUser",
    "goodsType": "LIQUIDS",
    "quantity": 1,
    "totalPrice": 1
}
```

Result will be validated and written to logback.log file which is located in orderlog microservice.

# Tests

This project also includes a few tests which can be helpful if you want to understand how to
test Spring Cloud Stream with JUnit.