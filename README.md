# Ada Capstone Project

## Overview

This is a full-stack HVAC system estimator tool built as part of the Ada Developers Academy Capstone project. It features a Spring Boot backend (Java 21) and a React + Vite frontend, with MySQL for data persistence.

---

## Backend Tech Stack

* **Java 21**, **Spring Boot 3.5.3**
* **MySQL** (FreeSQLDatabase)
* **Hibernate / JPA** ORM
* **Docker** deployment on **Render**
* **Validation** with Jakarta `@Valid`
* **Custom error handling** via `GlobalExceptionHandler`

---

## API Endpoints

All endpoints are prefixed with: `https://hvac-system-api.onrender.com/api/projects`

### ✅ POST `/api/projects`

Create a new project.

* Requires JSON body with all required fields (see below).
* Returns: `201 CREATED`

### ✅ GET `/api/projects`

Get all projects or filter by query parameters:

* Optional query params: `projectName`, `location`
* Returns: list of matching projects
* Returns `404` if no matches found

### ✅ GET `/api/projects/{id}`

Get project by ID.

* Returns: project object
* Throws `ResourceNotFoundException` if not found

### ✅ PUT `/api/projects/{id}`

Update project by ID.

* Requires full valid body
* Returns updated project
* Returns `404` if ID not found

### ✅ DELETE `/api/projects/{id}`

Delete project by ID.

* Returns: success message
* Returns `404` if ID not found

---

## Entity: `Project.java`

Mapped to table: `projects`

| Field            | Column Name   | Type    | Constraints    |
| ---------------- | ------------- | ------- | -------------- |
| id               | id            | Long    | Auto-generated |
| projectName      | project\_name | String  | `@NotBlank`    |
| location         | location      | String  | `@NotBlank`    |
| area             | area          | Double  | `@Positive`    |
| northWallArea    | north\_wall   | Double  | `@Positive`    |
| northWindowCount | north\_window | Integer | `@NotNull`     |
| southWallArea    | south\_wall   | Double  | `@Positive`    |
| southWindowCount | south\_window | Integer | `@NotNull`     |
| eastWallArea     | east\_wall    | Double  | `@Positive`    |
| eastWindowCount  | east\_window  | Integer | `@NotNull`     |
| westWallArea     | west\_wall    | Double  | `@Positive`    |
| westWindowCount  | west\_window  | Integer | `@NotNull`     |

---

## DB Setup (FreeSQLDatabase)

### Table: `projects`

Latest columns:

```sql
ALTER TABLE projects
  ADD COLUMN north_wall DOUBLE,
  ADD COLUMN north_window INT,
  ADD COLUMN south_wall DOUBLE,
  ADD COLUMN south_window INT,
  ADD COLUMN east_wall DOUBLE,
  ADD COLUMN east_window INT,
  ADD COLUMN west_wall DOUBLE,
  ADD COLUMN west_window INT;

UPDATE projects
SET
  north_wall = 10, north_window = 1,
  south_wall = 10, south_window = 1,
  east_wall = 10, east_window = 1,
  west_wall = 10, west_window = 1;
```

---

## application.properties (Deployment Config)

```properties
spring.application.name=capstone

# DB local connection
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA config
# Remove next line if Flyway will update DB:
spring.jpa.hibernate.ddl-auto=update 
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Optional: Fix time zone issues
spring.datasource.hikari.connection-timezone=UTC
```

---

## Error Handling

Global exception handler provides:

* 400: Validation errors (field-specific)
* 404: Resource not found (custom message)
* 400: Malformed JSON body
* 500: Generic fallback

---

## Status

* ✅ Backend fully deployed and tested on Render
* ✅ CRUD and filter routes working
* 🚧 Frontend in progress (React + Vite)

---

## Next Steps

* Complete and deploy frontend
* Add authentication layer (optional)
* Refine validation or add additional rules

---

© Elham "Ellie" Alavi | Ada Developers Academy Capstone 2025
