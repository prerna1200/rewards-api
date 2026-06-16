# Rewards API

This project is a Spring Boot based REST API that calculates reward points for customers based on their transaction history over a period of time.

---

## Problem Statement

A retailer provides a reward program to its customers based on the amount spent in each transaction:

- 2 points are awarded for every dollar spent above $100
- 1 point is awarded for every dollar spent between $50 and $100
- No points are awarded for spending $50 or less

### Example

For a transaction of $120:

- (120 - 100) * 2 = 40 points
- (100 - 50) * 1 = 50 points

**Total = 90 reward points**

---

## Features

- RESTful API built using Spring Boot
- Reward calculation based on transaction amount
- Monthly reward points per customer
- Total reward points calculation
- Supports multiple customers with multiple transactions
- Pagination supported for large data sets
- DTO-based clean response structure
- Input validation using Jakarta Validation
- Global exception handling implemented
- In-memory H2 database used for demonstration
- Structured and modular project design

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 Database
- Maven
- JUnit & Mockito (for testing)

---

## Project Structure

