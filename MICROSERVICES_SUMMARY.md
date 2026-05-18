# Microservices Summary

## How many microservices are built?

This project includes 3 main business microservices plus 3 supporting infrastructure services.

### Business microservices

1. `fitness-advisor` (`user-service`)
   - Manages user accounts.
   - Provides endpoints for user registration, user profile retrieval, and user validation.

2. `activityservice`
   - Tracks fitness activities for users.
   - Provides endpoints to create activities, list activities for a user, and fetch activity details.
   - Stores activity data in MongoDB.

3. `aiservice`
   - Provides recommendation endpoints for users and activities.
   - Uses a MongoDB-backed `RecommendationRepository` to persist recommendation records.
   - Contains a `GeminiService` that calls an external Gemini API for AI data.

### Infrastructure/support services

4. `configserver`
   - Centralized Spring Cloud Config server.
   - Serves configuration for other services from `src/main/resources/config/*`.

5. `eureka`
   - Service registry using Netflix Eureka.
   - Allows services to discover each other at runtime.

6. `gateway`
   - API gateway for routing traffic to backend services.
   - Routes configured for `/api/users/**`, `/api/activities/**`, and `/api/recommendations/**`.

### Frontend

- `fitness-app-frontend`
  - React application that interacts with the backend microservices.
  - Handles user login, activity display, and calling backend APIs.

## What is the "RAG" part?

The project does not have a dedicated vector store or document retrieval knowledge base implemented in the codebase.

- The `aiservice` contains a `GeminiService` that calls an external Gemini API using environment values `GEMINI_API_URL` and `GEMINI_API_KEY`.
- This is a direct AI model call, not a full retrieval-augmented generation pipeline with a separate document store.
- Therefore, the current “knowledge” source is essentially the external Gemini/LLM endpoint, not an internal indexed knowledge base.

## What does the AI actually recommend?

- **Contents of recommendations:** The `aiservice` generates structured JSON recommendations that include:
  - `analysis`: brief analysis of the activity
  - `pace`, `heartRate`, `caloriesBurned`: measured metrics when available
  - `activitySummary`: a formatted exercise summary
  - `Area of improvements`: list of specific areas and targeted recommendations
  - `suggestions`: suggested workouts (name + detailed description)
  - `safety`: safety guidelines and descriptions

- **Does it recommend workout plans or diet?** The system focuses on exercise analysis and workout suggestions (specific workouts and improvements). There is no diet or nutrition plan generation in the current code.

- **Inputs used to generate recommendations:** Recommendations are generated from an `Activity` record. The prompt sent to the external Gemini model includes:
  - Activity type (e.g., run, walk, cycle)
  - Duration (minutes)
  - Calories burned
  - Additional metrics (pace, heart rate, and any other `additionalMetrics` stored with the activity)

- **How the pipeline works:** The `ActivityAiService` builds a strict JSON prompt from the `Activity` data, calls `GeminiService` (an external LLM API), parses the returned JSON and stores the resulting `Recommendation` document in MongoDB via `RecommendationRepository`.

## Tech stack and databases

- `fitness-advisor` (user-service)
  - **Tech:** Java 17, Spring Boot, Spring Cloud (Config, Eureka client), Spring Data JPA, Lombok
  - **DB:** MySQL (via `spring-boot-starter-data-jpa` and `mysql-connector-j`)

- `activityservice`
  - **Tech:** Java 17, Spring Boot, Spring Cloud (Config, Eureka client), Spring Data MongoDB, WebFlux, RabbitMQ (AMQP)
  - **DB:** MongoDB (`spring-boot-starter-data-mongodb`)

- `aiservice`
  - **Tech:** Java 17, Spring Boot, Spring Cloud (Config, Eureka client), Spring Data MongoDB, WebFlux (WebClient), RabbitMQ (AMQP)
  - **DB:** MongoDB (`RecommendationRepository` / `spring-boot-starter-data-mongodb`)

- `configserver`
  - **Tech:** Java 17, Spring Boot, Spring Cloud Config Server
  - **DB:** None (native config files served from `src/main/resources/config`)

- `eureka`
  - **Tech:** Java 17, Spring Boot, Netflix Eureka Server
  - **DB:** None (service registry kept in-memory)

- `gateway`
  - **Tech:** Java 17, Spring Boot, Spring Cloud Gateway, Eureka client
  - **DB:** None

- `fitness-app-frontend`
  - **Tech:** React (Vite), JavaScript/JSX, MUI, Redux Toolkit
  - **DB:** None (frontend; communicates with backend services)
