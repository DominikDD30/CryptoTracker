# CryptoTracker ![Crypto Logo](logo.png)

CryptoTracker is a microservices-based application that provides real-time cryptocurrency tracking and alerting capabilities. The application fetches cryptocurrency data from Binance, aggregates and stores it, calculates hourly and daily price averages, and enables users to set  price alerts. When alert conditions are met, users receive notifications via email. The application is orchestrated with Kubernetes to ensure scalability and reliability.

## Project Overview

CryptoTracker consists of five microservices, each handling a specific part of the system’s functionality. Data flows through the system via Kafka topics, enabling efficient, asynchronous communication between services. Redis is used as a caching layer for quick access, while PostgreSQL is the persistent storage for historical and aggregated data.



https://github.com/user-attachments/assets/3a9ff6ac-67e4-4360-85ed-6085c7ccf96e


## Technologies Used

- **Languages**: Python FastAPI (MarketDataFetcher), Java (other services), Typescript
- **Frameworks & Libraries**: 
  - Kafka for message streaming
  - Redis for caching
  - PostgreSQL for persistent storage
  - Kubernetes for container orchestration
  - React for simple frontend client
- **Other Tools**:
  - Binance API for live cryptocurrency price data
  - JavaMail library for sending notifications

## Microservices

### 1. MarketDataFetcher
- **Language**: Python
- **Functionality**: Fetches real-time cryptocurrency prices from the Binance API every 5 seconds. Publishes the price data to the Kafka topic `crypto-prices` for other services to consume.
- **Kafka Topic**: `crypto-prices`

### 2. PriceAggregator
- **Language**: Java
- **Functionality**: Consumes cryptocurrency data from the `crypto-prices` Kafka topic. Stores recent price data in Redis for quick retrieval and persists it to PostgreSQL for long-term storage. This service also calculates hourly and daily average prices, which are stored in the database.
- **Database**: Redis (short-term cache) and PostgreSQL (persistent storage)

### 3. UserService
- **Language**: Java
- **Functionality**: Manages user subscriptions for price alerts. Users can set alert thresholds based on percentage changes in cryptocurrency prices. This service stores user data and subscription criteria.
- **Database**: PostgreSQL

### 4. AlertService
- **Language**: Java
- **Functionality**: Regularly checks stored subscriptions against current price data to see if alert conditions are met. If a condition is triggered, it publishes a message to the `alert` Kafka topic.
- **Kafka Topic**: `alert`

### 5. EmailService
- **Language**: Java
- **Functionality**: Consumes alert messages from the `alert` Kafka topic and sends email notifications to users whose alert conditions are met.
- **Dependencies**: JavaMail library for notification delivery

## Kubernetes Deployment

All microservices are containerized and orchestrated using Kubernetes. Each service runs in its own pod, and Kubernetes manages load balancing, scaling, and service discovery across the application. This setup allows CryptoTracker to efficiently handle high data throughput and large numbers of concurrent users.

## Getting Started

1. Clone the repository.
2. Deploy the services on a Kubernetes cluster (instructions in `deployment-commands.txt`,`images-commands.txt`).
4. Start each microservice, beginning with MarketDataFetcher, to initialize data flow.

## Future Enhancements

- Extend alert customization options (e.g., by time, volume).
- Improve scalability by separating read and write operations in the database.
- Add additional notification channels beyond email, such as SMS or push notifications.
- Expand the range of tracked cryptocurrencies to include more assets, providing users with broader monitoring options.
- Store additional information for each cryptocurrency, such as historical price data, trading volume, market sentiment indicators, circulating supply, and technical analysis metrics. This would enable more detailed insights and analytics for users.
- Implement user accounts with secure authentication.
- Introduce e-commerce features, enabling users to purchase cryptocurrencies directly within the application.


## License

This project is open-source and available under the MIT License.
