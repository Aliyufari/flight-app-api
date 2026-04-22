# Flight Booking API (SaaS-Ready Backend)

A production-grade backend system built with Spring Boot, designed for scalability, security, and clean architecture. This project demonstrates how to build a modern API using enterprise-level best practices suitable for real-world SaaS applications.

## Overview

This API provides a solid foundation for building secure and scalable systems. It includes authentication, role-based access control, email verification, and a clean, maintainable architecture that can scale with business needs.

## Live Demo & Documentation

Base URL  
https://flight-app-api.onrender.com  

Swagger UI  
https://flight-app-api.onrender.com/swagger-ui/index.html#/  

You can explore and test all endpoints directly via Swagger.

## Key Features

### Authentication & Security
- JWT-based authentication (stateless)  
- Secure login and token refresh flow  
- Email verification with expiring tokens  
- Resend verification support  

### Role-Based Access Control (RBAC)
- Fine-grained permissions (e.g., role:view)  
- Method-level authorization using @PreAuthorize  
- Flexible role and permission structure  

### Architecture
- Layered design (Controller → Service → Repository)  
- Clear separation of concerns  
- Scalable and maintainable codebase  

### DTO & Mapping
- DTO pattern for request/response handling  
- MapStruct for clean and efficient mapping  

### Event-Driven Design
- Email verification handled via event publishing  
- Decoupled and extensible system components  

### Testing
- Unit tests for business logic  
- Integration tests for API endpoints  

### API Documentation
- OpenAPI/Swagger integration  
- Interactive and easy-to-use endpoint testing  

## Tech Stack

- Java  
- Spring Boot  
- Spring Security  
- JWT  
- JPA / Hibernate  
- MapStruct  
- MySQL / PostgreSQL  
- Docker  

## Why This Project

This project reflects real-world backend engineering standards:

- Secure by design  
- Modular and extensible  
- Built with production-level practices  
- Ready to scale into a SaaS platform  

## Use Cases

- Flight booking platforms  
- SaaS applications  
- Multi-user systems with roles and permissions  
- Secure REST API backends  

## Getting Started

Clone the repository:

```bash
git clone https://github.com/Aliyufari/flight-app-api.git
cd your-repo
