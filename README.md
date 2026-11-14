# Project Management System

A RESTful backend application built with Java and Spring Boot to manage projects, tasks, team members, and comments.

## Overview

The Project Management System provides APIs for organizing projects, assigning tasks to users, managing project membership, and tracking task progress.

The application includes authentication and role-based authorization to protect API resources.

## Features

- User management.
- Project creation and management.
- Task creation, assignment, and status tracking.
- Project member management.
- Comments on tasks.
- JWT-based authentication.
- Role-based authorization (ADMIN and USER).
- Input validation and centralized exception handling.
- Unit testing for core services.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- MySQL
- Maven
- JUnit and Mockito
- Postman

## Project Architecture

The application follows a layered architecture:

- **Model:** Entity classes representing database tables.
- **DTO:** Data Transfer Objects for API requests and responses.
- **Repository:** Database access using Spring Data JPA.
- **Service:** Business logic and application operations.
- **Controller:** REST API endpoints.
- **Security:** Authentication and authorization.
- **Config:** Application and security configuration.
- **Exception:** Centralized exception handling.
- **Validation:** Custom validation logic.

## Main API Resources

| Resource | Base Endpoint |
|---|---|
| Authentication | `/Auth` |
| Users | `/Users` |
| Projects | `/Projects` |
| Tasks | `/Tasks` |
| Comments | `/api/comments` |
| Project Members | `/Projects` |

Login endpoint:

`POST /Auth/login`

Protected endpoints require a valid JWT token in the Authorization header:

`Authorization: Bearer <your-token>`

## Security

- JWT authentication.
- Password hashing using BCrypt.
- Role-based access control.
- Protected API endpoints.

## Database

The application uses MySQL with Spring Data JPA.

Database name:

`project_management_db`

## Configuration

Sensitive configuration values are provided through environment variables instead of being stored directly in the source code.

Required environment variables:

- `DB_username`
- `DB_password`
- `JWT_SECRET`

## Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/zahranhosam749-prog/Project-Management-System.git
   ```

2. Navigate to the project directory.

3. Create the MySQL database:

   ```sql
   CREATE DATABASE project_management_db;
   ```

4. Configure the required environment variables.

5. Run the application on Windows:

   ```bash
   .\mvnw.cmd spring-boot:run
   ```

The application runs on:

`http://localhost:8080`

## Running Tests

Execute the following command:

```bash
.\mvnw.cmd clean test
```

## API Testing

The APIs were tested using Postman, including authentication, authorization, validation, and CRUD operations.

## Author

**Zahran Hosam**

GitHub: [zahranhosam749-prog](https://github.com/zahranhosam749-prog)
