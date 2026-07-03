# i2i-Academy-ApacheIgnite-8

A Java application that connects to a single-node **Apache Ignite 3** cluster running in Docker and performs table creation, data insertion, retrieval and updates through the modern **Thin Client API**.

This project was built as part of the i2i Academy training program to explore In-Memory Data Grid (IMDG) concepts and distributed database operations with Apache Ignite.

## Overview

The application simulates a telecom subscriber usage tracker:

- Connects to a local Ignite 3 node using `IgniteClient`.
- Creates a `Subscriber` table (`customerId`, `dataUsage`, `smsUsage`, `callUsage`).
- Clears the table on every run so each execution starts from a clean state.
- Inserts 5 dummy subscribers with baseline (zero) usage values.
- Retrieves the subscribers back from the database, applies random usage increments, and writes the updated values back with `UPDATE`.
- Prints the final state of all 5 subscribers to the console and terminates.

## Tech Stack

- **Java 17**
- **Maven**
- **Apache Ignite 3.1.0** (`ignite-client` Thin Client)
- **Docker / Docker Compose**

## Project Structure

```
i2i-Academy-ApacheIgnite-8/
├── docker-compose.yml              # single-node Ignite 3 cluster definition
└── ignite-subscriber-app/
    ├── pom.xml
    └── src/main/java/com/i2iacademy/ignite/
        ├── Subscriber.java         # data model
        └── Main.java                # client logic: create, delete, insert, retrieve, update, print
```

## Prerequisites

- Docker & Docker Compose
- Java 17+
- Maven

## Getting Started

### 1. Start the Ignite 3 cluster

```bash
docker compose up -d
```

This starts a single Ignite 3 node and exposes:
- `10300` — REST API port
- `10800` — Thin Client port

### 2. Initialize the cluster

Ignite 3 requires an explicit initialization step before it can be used:

```bash
docker run --rm -it --network=host -e LANG=C.UTF-8 -e LC_ALL=C.UTF-8 apacheignite/ignite:3.1.0 cli
```

Inside the CLI:

```
connect http://localhost:10300
cluster init --name=ignite3
```

### 3. Run the application

```bash
cd ignite-subscriber-app
mvn compile
mvn exec:java -Dexec.mainClass="com.i2iacademy.ignite.Main"
```

Each run clears the `Subscriber` table, inserts 5 fresh records, simulates usage updates with random values, and prints the final state to the console.

### 4. (Optional) Verify the data directly in Ignite

```bash
docker run --rm -it --network=host -e LANG=C.UTF-8 -e LC_ALL=C.UTF-8 apacheignite/ignite:3.1.0 cli
```

```
sql
SELECT * FROM Subscriber;
```
