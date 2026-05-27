# Rewards API

This project implements a REST API using Spring Boot to calculate reward points for customers based on their transactions over a three-month period.

## Problem Statement

A retailer offers a reward system where:

- 2 points are given for every dollar spent above 100
- 1 point is given for every dollar spent between 50 and 100
- No points are given for spending 50 or less

Example:
A transaction of 120 results in:
(120 - 100) * 2 + (100 - 50) * 1 = 40 + 50 = 90 points

## Features

- Spring Boot based REST API
- Reward point calculation for each transaction
- Monthly reward calculation per customer
- Total reward calculation per customer
- Multiple customers with multiple transactions
- In-memory dataset used for demonstration
- Basic exception handling included

## Tech Stack

- Java 21
- Spring Boot
- Maven

## Project Structure

com.example.rewardsapi
controller
service
repository
model
dto
exception

## How to Run

1. Clone the repository:
   git clone https://github.com/prerna1200/rewards-api.git

2. Open the project in IntelliJ IDEA

3. Run the main class:
   RewardsApiApplication.java

## API Endpoint

GET /rewards

Example:
http://localhost:8080/rewards

## Sample Output

{
"C1": {
"JANUARY": 115,
"FEBRUARY": 250,
"MARCH": 40,
"TOTAL": 405
},
"C2": {
"JANUARY": 10,
"FEBRUARY": 120,
"MARCH": 0,
"TOTAL": 130
}
}

## Testing

The API can be tested using:

Browser:
http://localhost:8080/rewards

Command line:
Invoke-RestMethod http://localhost:8080/rewards

## Notes

- Months are derived dynamically from transaction dates
- No hardcoding of months is done
- Clean and modular project structure is followed
- Code is formatted and readable
- Designed to be easily extendable to a real database

## Author

Prerna