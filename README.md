# events-playground

A Quarkus-based playground project to experiment with **reactive messaging** and **parallel processing** using **Artemis (AMQP 1.0)**.

---

## 🚀 Overview

This project simulates the flow of "mountain request processing".
- **Producer service** sends `RequestDTO` messages into Artemis.
- **Consumer pipeline** processes each request through multiple steps:
    1. **Persist request** into Postgres.
    2. **Simple request validation**:
        - Mountain(s) must exist in Slovenia.
        - Dates must make sense (`CLIMBED` → past, `WISHLISTED` → future).
    3. **Split request into mountain tasks** — one task per mountain.
    4. **Enrich each mountain** with elevation, coordinates, and region (mocked API calls with delay) using **structured concurrency**.
    5. **Persist mountain entry** into DB.
    6. **Join results back** into the parent request using **row-level locking** to increment a `processedMountainCount`. When all tasks are processed, the request is marked `COMPLETED`.

Invalid requests are redirected into a **Dead Letter Queue (DLQ)**, where they are persisted with an `INVALID` state and reason.

---

## 🛠️ Tech Stack

- **Quarkus**
- **SmallRye Reactive Messaging**
- **Artemis AMQP 1.0**
- **Docker Compose** for local infra

---

## ▶️ How to Run

### 1. Start infrastructure
```bash
docker compose up -d
```

### 2. Start producer and consumer service
`quarkus dev`

### 3. Send POST request to producer API
`http://localhost:8080/api/requests`

// example body for successful processing
```json
{
  "mountains": ["Stol", "Brana", "Krvavec", "Raduha"],
  "requestAction": "ADD_TO_WISHLIST",
  "date": "2025-10-18",
  "note": "Can't wait!"
}
```

// example body for unsuccessful processing
```json
{
  "mountains": ["Nanos", "Trstelj"],
  "requestAction": "ADD_TO_WISHLIST",
  "date": "2022-10-18",
  "note": "In the past!"
}
```

## 🗺️ Next Steps

* Pipeline context for better traceability of pipeline steps
* Apache Kafka integration