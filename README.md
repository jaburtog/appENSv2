# ENS - Enterprise Notification System

A full-stack web application built with **Angular 19** frontend and **Jakarta EE 10** backend, deployable on **WebSphere Liberty** server with **PostgreSQL** database.

## Tech Stack

### Backend
- **Java 17** (compatible with Jakarta EE 10)
- **Jakarta EE 10** (JAX-RS, JPA, CDI, Bean Validation)
- **PostgreSQL** database
- **WebSphere Liberty** application server
- **Maven** for build management

### Frontend
- **Angular 19** with TypeScript
- **Standalone Components**
- **Reactive programming** with RxJS
- **HTTP Client** for API communication

### Architecture
- **Clean Architecture** with clear separation of concerns:
  - **Domain Layer**: Entities and repository interfaces
  - **Application Layer**: DTOs
  - **Infrastructure Layer**: Repository implementations
  - **Presentation Layer**: REST API endpoints

## Database Schema

The application manages the following entities:

1. **Users** - Application users
2. **Roles** - User roles for access control
3. **NotificationTypes** - Different types of notifications
4. **Notifications** - Notification messages
5. **UserNotifications** - Junction table linking users to notifications

## Features

- ✅ Full CRUD operations for all entities
- ✅ RESTful API with JSON responses
- ✅ Clean architecture with separation of concerns
- ✅ JPA/Hibernate for ORM
- ✅ Bean validation for input validation
- ✅ Responsive UI with modern design
- ✅ Error handling and loading states
- ✅ CORS support for cross-origin requests

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- Node.js 18+ and npm
- PostgreSQL 12+
- WebSphere Liberty 24.0.0.3+

### Backend Setup
```bash
cd backend
mvn clean package
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

## License
Copyright © 2026 ENS Application
