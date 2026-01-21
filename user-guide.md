# User Guide

This document explains how to run and explore the project locally.

## Overview

- Spring Boot + MyBatis + MySQL
- Session-based authentication (JSESSIONID cookie)
- API documentation via Swagger UI (springdoc)

## Project Structure (High Level)

- `src/main/java/com/service/demo/domain`: domain features (user, common code, sports club)
- `src/main/java/com/service/demo/common`: shared API wrapper, errors, filters, exceptions
- `src/main/java/com/service/demo/config`: web and OpenAPI configuration
- `src/main/resources/db/schema.sql`: database schema
- `src/main/resources/data.sql`: seed data (large)
- `src/main/resources/mapper`: MyBatis mappers

## Running Locally

1) Start MySQL (example docker compose):
```
docker compose -f docker/mysql/docker-compose.yml up -d
```

2) Run the application:
```
./gradlew bootRun
```

Default URL: `http://localhost:8080`

## Swagger UI

- URL: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication

- Login creates a server session and returns a `JSESSIONID` cookie.
- All protected endpoints require the session cookie.
- Interceptor exclusions: `/auth/login`, `/auth/register`, Swagger, and error endpoints.

## Main Endpoints (Summary)

Auth:
- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/logout`
- `POST /auth/password/reset`

User:
- `GET /users/me`
- `PUT /users/me`

Common Codes:
- `GET /common-codes/{groupCode}/tree`

Sports Clubs:
- `POST /sports-clubs`
- `GET /sports-clubs/{id}`
- `GET /sports-clubs`
- `PUT /sports-clubs/{id}`
- `DELETE /sports-clubs/{id}`

## Notes

- `schema.sql` is auto-applied on startup via `spring.sql.init.*` settings.
- `data.sql` is also auto-applied and is large; if you prefer to disable it, set:
  - `spring.sql.init.mode=never`
