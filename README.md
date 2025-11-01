title: "Acme Microservices (Monorepo)"
language: "en"
version: "1.0"
description: >
  Educational project demonstrating a modern microservices environment built
  with Java 21, Spring Boot 3, Docker Compose, and GitHub Actions. The goal is
  to simulate a small-scale production setup with infrastructure, monitoring,
  and CI/CD.

repository_structure: |
  acme-microservices/
  ├─ pom.xml                  # parent Maven module
  ├─ .github/workflows/ci.yml # GitHub Actions pipeline
  ├─ platform-infra/          # Docker Compose infra (Postgres, Redis, Kafka, Keycloak, Prometheus, Grafana)
  ├─ service-catalog/         # example Spring Boot microservice
  ├─ shared-libs/             # shared code and libraries
  └─ README.md

requirements:
  - tool: "JDK"
    min_version: "21"
  - tool: "Maven"
    min_version: "3.9+"
  - tool: "Docker Desktop"
    min_version: "latest"
  - tool: "Git"
    min_version: "any"

local_setup:
  steps:
    - title: "Build the microservice"
      commands: |
        cd service-catalog
        mvn clean package
    - title: "Build Docker image"
      commands: |
        docker build -t acme/service-catalog:local .
    - title: "Start infrastructure"
      commands: |
        cd ../platform-infra
        docker compose up -d
    - title: "Run services with infrastructure"
      commands: |
        docker compose -f docker-compose.yml -f docker-compose.services.yml up -d

health_check:
  services:
    - name: "service-catalog"
      url: "http://localhost:8081/actuator/health"
      notes: "Health check should return {\"status\":\"UP\"}"
    - name: "Prometheus"
      url: "http://localhost:9090"
      notes: "Metrics UI"
    - name: "Grafana"
      url: "http://localhost:3000"
      credentials: "admin/admin"
      notes: "Monitoring dashboards"
    - name: "Keycloak"
      url: "http://localhost:8080"
      credentials: "admin/admin"
      notes: "Authentication server"

add_new_microservice:
  steps:
    - "Copy service-catalog → service-<name>"
    - "Update artifactId and application port in the new service"
    - "Add the module to root pom.xml (<modules>)"
    - "Extend matrix in .github/workflows/ci.yml"
    - "Add entry in platform-infra/docker-compose.services.yml"
    - "Build & run just this module"
  commands: |
    mvn clean package -pl service-<name>
    docker build -t acme/service-<name>:local ./service-<name>

ci_pipeline:
  file: ".github/workflows/ci.yml"
  behavior: |
    - Builds changed Maven modules in the monorepo (matrix strategy)
    - Validates Docker Compose configuration
    - Runs a health-check smoke test for service-catalog
  triggers:
    - push to: ["main"]
    - pull_request: true

cleanup:
  instructions: |
    To stop and remove containers:
      docker compose -f platform-infra/docker-compose.yml -f platform-infra/docker-compose.services.yml down

monitoring:
  notes: |
    - Spring Boot Actuator exposes /health, /info, and /actuator/prometheus
    - Prometheus scrapes the metrics endpoint
    - Grafana visualizes dashboards on port 3000 (admin/admin)

author:
  name: "Your Name"
  note: >
    Learning microservices architecture (Java + Spring Boot + DevOps). This repository
    is an educational, production-like setup built from scratch for learning purposes.

summary:
  bullets:
    - "Monorepo with modular Spring Boot services"
    - "Full local infrastructure via Docker Compose"
    - "Health checks and metrics with Actuator + Prometheus"
    - "Automated CI with GitHub Actions"
    - "Clear developer onboarding (this README in YAML form)"