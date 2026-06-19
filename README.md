# Rewards API

This project is a Spring Boot REST API that calculates reward points for customers based on their transaction history.

The application allows adding transactions and viewing reward points for customers in different ways such as overall, by customer, or by month.

---

## Business Logic

Reward points are calculated based on the transaction amount:

- 2 points for every dollar spent above $100
- 1 point for every dollar spent between $50 and $100
- No points for amounts $50 or below

Example:

If a customer spends $120:
- 20 × 2 = 40 points (above 100)
- 50 × 1 = 50 points (between 50 and 100)

Total = 90 points

---

##  What This API Does

- Fetch reward points for all customers (with pagination)
- Fetch reward details for a specific customer
- Fetch rewards grouped by month
- Add new transactions
- Validate input data (invalid input returns proper error messages)
- Handle exceptions globally in a structured way

---
##  Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit & Mockito (for testing)

---

## Project Structure

The project follows a layered architecture:

- controller → handles API requests
- service → contains business logic
- repository → interacts with database
- model → database entity
- dto → request/response objects
- exception → global error handling

---

##  API Endpoints

### Get all rewards (with pagination)
GET /rewards?page=0&size=5

---

### Get rewards for a specific customer

GET /rewards/{customerId}

---

### Get rewards by month

GET /rewards/month/{month}

---

### Add a new transaction

POST /rewards/transactions

Example request body:
```json
{
  "customerId": "C1",
  "amount": 120,
  "transactionDate": "2026-01-10"
}


Testing
The project includes unit and integration tests for:

Service layer
Controller layer
Repository layer
DTOs and models
Exception handling

Edge cases like invalid input, wrong JSON, and missing data are also covered.

Notes

H2 database is used for quick testing and development (no external setup needed)
Data resets every time the application restarts
Validation annotations are used to ensure correct input


Summary
This project demonstrates:

Clean layered architecture
Proper use of Spring Boot and JPA
Business logic implementation
API design
Exception handling
Test coverage and edge case handling

