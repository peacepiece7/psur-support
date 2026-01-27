# User Guide

This document explains how to run and explore the project locally.

## Overview

- Spring Boot + MyBatis + MySQL
- Session-based authentication (JSESSIONID cookie)
- API documentation via Swagger UI (springdoc)

## Project Structure (High Level)

- `src/main/java/com/service/demo/domain`: domain features (user, common code, sports club, reg sports club)
- `src/main/java/com/service/demo/common`: shared API wrapper, errors, filters, exceptions
- `src/main/java/com/service/demo/config`: web and OpenAPI configuration
- `src/main/resources/db/schema.sql`: database schema
- `src/main/resources/data.sql`: seed data (large)
- `src/main/resources/mapper`: MyBatis mappers

## Running Locally

1) Start MySQL (example docker compose):
```
docker compose -f backend/docker/mysql/docker-compose.yml up -d
```

2) Run the application:
```
./gradlew bootRun
```

Default URL: `http://localhost:9090`

## Swagger UI

- URL: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`

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

Operating Sports (Common Code):
- Sports clubs accept `categoryIds` (list of `common_code.id`) and store them in `sports_club_category`.
- Registered sports club applications accept `operatingSportCodeIds` (list of `common_code.id`) and store them in
  `reg_sports_club_application_category`.
- If `operatingSportCodeIds` is omitted, the service falls back to
  `operatingSportParentCodeId`/`operatingSportChildCodeId`.

Registered Sports Club Applications:
- `POST /reg-sports-club-applications`
- `PATCH /reg-sports-club-applications/{applyId}/status`
- `GET /reg-sports-club-applications/{applyId}`
- `GET /reg-sports-club-applications`

## Notes

- `schema.sql` is auto-applied on startup via `spring.sql.init.*` settings.
- `data.sql` is also auto-applied and is large; if you prefer to disable it, set:
  - `spring.sql.init.mode=never`
- Database reset (Docker MySQL example):
```
docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 -e "DROP DATABASE IF EXISTS mydb; CREATE DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\db\\schema.sql"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\data.sql"
```
