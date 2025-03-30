# Customer Management System

A CRUD application for managing customer data with JWT authentication.

## Features

- User authentication with JWT
- Create, read, update, and delete customers
- List customers with pagination, sorting, and searching
- Sync customers from a remote API

## Technologies Used

- Backend: Spring Boot 3+
- Frontend: HTML, CSS, JavaScript (ES6)
- Database: MySQL

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MySQL

### Installation

1. Clone the repository:

## Application Run

1. Fork this Repository/ Clone / Pull this Repository as per your convenence. and open into Eclips
-  Configure the database connection in `src/main/resources/application.properties`:
	- spring.datasource.url=jdbc:mysql://localhost:3306/customer_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password

2.  Build and run the maven application: 
then run frontend part how I am telling you
open this `src/main/resources/static/html/login.html` and open with browser, please remeber, Donot change server.port = 2000, some other port number, If you edit, you have to edit frontend part also.


