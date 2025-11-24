# Library-Angular-SpringBoot 📚
## 📝 Project Description
The **Online Bookstore Management System** is a web-based app that allows users to browse, search, and purchase books. The system is divided into two main components:
- **Frontend (Angular)** – provides a responsive and user-friendly interface for users and admins.
- **Backend (Spring Boot)** – handles API endpoints, business logic, and secure authentication via JWT.
## 🚀 Getting Started
## Prerequisites 
- Java 21
- Node.js 18+ & npm
- MySQL Database instance
- Angular CLI (install globally: npm install -g @angular/cli)
  ## backend setup
  - clone the repository
  - configure your database credentials in application.properties
  - build and run the application (./mvnw spring-boot:run) will be running on http://localhost:8080
  ## frontend setup
  - navigate to the frontend directory
  - install dependencies (npm install)
  - start server (ng serve) frontend will open in your browser at http://localhost:4200
## 🛠️ API Documentation (Swagger UI)
Once the Spring Boot backend is running, the complete list of available API endpoints can be explored via the Swagger UI interface:
🔗 Access API Documentation: http://localhost:8080/swagger-ui.html
This tool allows you to view documentation, test endpoints directly, and understand the expected request/response schemas.
## Features
- User registration & login with JWT authentication
- View, filter, and search books
- Admin panel for adding/editing books
- Responsive UI (Angular Material)
- REST API built with Spring Boot
## Future Development
This project is under active development. Planned features and improvements include:
- Shop&Purchase
- Loyalty program
- Forum
- Docker
- more tests.
