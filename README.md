# Kafka Order Processing System

A microservices-based order processing system using Apache Kafka for message handling and Spring Boot for service implementation.

## Architecture Overview

### Components
- **Order Service**: Handles order creation and management
- **Delivery Service**: Manages delivery assignments and tracking
- **Customer Service**: Handles customer interactions
- **Analytics Service**: Processes business metrics

### Technologies
- Java 17
- Spring Boot 3.2.x
- Apache Kafka
- PostgreSQL
- Docker & Docker Compose

## Getting Started

### Prerequisites
- JDK 17+
- Docker and Docker Compose
- Maven

### Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd kafka-order-system
```

2. Start Kafka infrastructure:
```bash
docker-compose up -d
```

3. Create Kafka topics:
```bash
# Create topics with partitions
kafka-topics --bootstrap-server localhost:9092 \
    --create \
    --topic new-orders \
    --partitions 3 \
    --replication-factor 1
```

4. Build and run services:
```bash
mvn clean install
java -jar order-service/target/order-service.jar
```

### Configuration

#### Kafka Producer Configuration
```java
@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, Order> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
```

#### Kafka Consumer Configuration
```java
@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, Order> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.G