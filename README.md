# Bet Settlement Service

**BetSettlementService** is an event-driven backend service responsible for bet settlement processing.

It:

- Accepts event outcomes via REST API
- Publishes outcomes to Kafka
- Consumes outcomes in a worker service
- Matches bets from the database
- Sends bet settlement messages to RocketMQ

The project demonstrates clean architecture principles, asynchronous processing, and separation between domain logic and
infrastructure.

---

## Repository Structure

This is a multi-module Maven project:

- `contracts` – Shared DTOs and messaging contracts
- `settlement-trigger-service` – REST API → Kafka producer
- `settlement-worker` – Kafka consumer → Bet matching → RocketMQ producer

---

## Architecture & Data Flow

1. `settlement-trigger-service` receives an event outcome via REST.
2. The outcome is published to Kafka topic `event-outcomes`.
3. `settlement-worker` consumes the outcome (consumer group: `settlement-worker`).
4. The worker matches relevant bets in the database.
5. Settlement messages are sent to RocketMQ.

---

## Message Contracts

### EventOutcomeMessage (Kafka)

```java
public record EventOutcomeMessage(
        String eventId,
        String eventName,
        String eventWinnerId
) {
}
```

Example JSON:

```json
{
  "eventId": "event-1",
  "eventName": "Team A vs Team B",
  "eventWinnerId": "team-a"
}
```

---

### BetSettlementMessage (RocketMQ)

```java
public record BetSettlementMessage(
        String betId,
        String userId,
        String eventId,
        String eventMarketId,
        String eventWinnerId,
        BigDecimal betAmount
) {
}
```

---

## Demo Bet Storage

`settlement-worker` uses in-memory **H2** database with `ddl-auto: create-drop`.

Sample seed data (`data.sql`):

---

## Requirements

- JDK 21
- Maven 3.9+
- Docker

---

## Future Improvements

- **Match by `eventId` only**  
  Retrieve all bets for the event and determine win/lose in the service layer to align more strictly with the assignment
  wording.

- **Encapsulate settlement logic in the domain model**  
  Move settlement calculation into a method like `Bet.settle(outcome)` to better demonstrate object-oriented modelling
  and separation of concerns.

- **Add idempotency handling**  
  Prevent duplicate settlements in case of Kafka message reprocessing (at-least-once delivery).

---

# Running the Project

---

## 1. Start Kafka

From the project root:

```bash
docker compose up -d
```

Kafka runs on:

```
localhost:9092
```

---

## 2. Start RocketMQ

The worker is configured with:

- NameServer: `localhost:9876`
- Producer group: `bet-settlement-worker-producer`

Start RocketMQ:

```bash
docker compose -f docker-compose-rocketmq.yml up -d
```

NameServer will be available at:

```
localhost:9876
```

---

## 3. Build the Project

```bash
mvn clean install
```

---

## 4. Run Services

### Settlement Trigger Service

Configuration:

- Port: `8080`
- Kafka bootstrap server: `localhost:9092`
- Topic: `event-outcomes`

Run:

```bash
mvn -pl settlement-trigger-service spring-boot:run
```

Service URL:

```
http://localhost:8080
```

---

### Settlement Worker

Configuration:

- Port: `8081`
- Kafka bootstrap server: `localhost:9092`
- Topic: `event-outcomes`
- Consumer group: `settlement-worker`
- H2 in-memory DB
- RocketMQ NameServer: `localhost:9876`

Run:

```bash
mvn -pl settlement-worker spring-boot:run
```

Service URL:

```
http://localhost:8081
```

---

## Database Configuration (Worker)

The worker uses:

```
jdbc:h2:mem:bets
ddl-auto: create-drop
```

Seed data (`data.sql`):

```sql
insert into bets (bet_id, user_id, event_id, event_market_id, event_winner_id, bet_amount)
values ('bet-1', 'user-1', 'event-1', 'mkt-1', 'team-a', 10.00),
       ('bet-2', 'user-2', 'event-1', 'mkt-1', 'team-b', 20.00),
       ('bet-3', 'user-3', 'event-2', 'mkt-2', 'team-x', 15.00);
```

---

## Trigger Settlement

If the controller is mapped to:

```
POST /api/v1/events/outcome
```

Send:

```bash
curl -X POST http://localhost:8080/api/v1/events/outcome \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "event-1",
    "eventName": "Team A vs Team B",
    "eventWinnerId": "team-a"
  }'
```

Internal flow:

1. Message sent to Kafka topic `event-outcomes`
2. Worker consumes the message
3. Worker finds bets where:

- `event_id = event-1`
- `event_winner_id = team-a`

4. Matching bets are sent to RocketMQ

---

## Ports Summary

| Component                  | Port |
|----------------------------|------|
| settlement-trigger-service | 8080 |
| settlement-worker          | 8081 |
| Kafka                      | 9092 |
| RocketMQ NameServer        | 9876 |
