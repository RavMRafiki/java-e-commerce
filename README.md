# Java E‑Commerce (Spring Boot)

Basic Spring Boot setup using Maven, Java 21, PostgreSQL.

## Quick Start

- Prereqs: Java 21, Maven, Docker (optional).

### Run locally (no DB)

- Build: `mvn -q -DskipTests package`
- Start: `mvn spring-boot:run`
- Health: GET http://localhost:8080/actuator/health
- Ping: GET http://localhost:8080/api/ping

### Run with Postgres (dev profile)

- Start DB: `docker compose up -d postgres`
- Run app: `mvn spring-boot:run -Dspring-boot.run.profiles=dev \
-Dspring-boot.run.jvmArguments='-DDB_URL=jdbc:postgresql://localhost:5432/ecommerce -DDB_USERNAME=postgres -DDB_PASSWORD=postgres'`

## Project Layout

- [src/main/java/com/ravmr/ecommerce](src/main/java/com/ravmr/ecommerce)
- [src/main/resources/application.yaml](src/main/resources/application.yaml)
- [src/main/resources/application-dev.yaml](src/main/resources/application-dev.yaml)
- [docker-compose.yml](docker-compose.yml)
- [.github/workflows/ci.yml](.github/workflows/ci.yml)

## Next Steps

- Add entities (User, Product, Order) and repositories.
- Add auth (Spring Security + JWT).
- Add database migrations in `src/main/resources/db/migration` with Flyway.

## JWT Auth (Dev)

- Set a dev secret (32+ chars) and start:

```bash
export JWT_SECRET='dev-secret-change-me-32-bytes-minimum-xxxxxxxxxxxx'
mvn spring-boot:run
```

- Login with the built-in dev user:

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
	-H 'Content-Type: application/json' \
	-d '{"username":"dev","password":"password"}'
```

- Use the returned `accessToken`:

```bash
TOKEN=...
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/ping
```
