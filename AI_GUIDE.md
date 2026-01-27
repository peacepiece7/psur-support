# AI Guide

Quick context for future Codex sessions working in this repo.

## Project Basics

- Backend is Spring Boot + MyBatis + MySQL in `backend/`.
- Default app port: `9090` (`backend/src/main/resources/application.properties`).
- DB schema/data live at:
  - `backend/src/main/resources/db/schema.sql`
  - `backend/src/main/resources/data.sql`

## Sports Club Flow (Operating Sports)

- Common codes for sports live in `common_code` (group `OPERATING_SPORT`).
- Direct club API uses `categoryIds` (list of `common_code.id`) and stores them in
  `sports_club_category`.
- Registered club application uses:
  - `operatingSportParentCodeId` / `operatingSportChildCodeId` (legacy fields)
  - `operatingSportCodeIds` (list) stored in `reg_sports_club_application_category`
- On approval, application categories are copied into `sports_club_category`.

## DB Reset (Docker MySQL)

```
docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 -e "DROP DATABASE IF EXISTS mydb; CREATE DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\db\\schema.sql"
cmd /c "docker exec -i mysql mysql -uroot -proot1234! --default-character-set=utf8mb4 mydb < backend\\src\\main\\resources\\data.sql"
```

## Encoding Notes

- JDBC `characterEncoding` should be `UTF-8` (Java charset name).
- DB collation is `utf8mb4_unicode_ci`.
