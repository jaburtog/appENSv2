# ENS REST API Documentation

## Base URL
```
http://localhost:9080/api/v1
```

## Response Format
All responses are in JSON format.

## Error Handling
Errors follow standard HTTP status codes:
- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource (username/email already exists)
- `500 Internal Server Error` - Server error

---

## Users API

### Get All Users
```http
GET /users
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "active": true,
    "createdAt": "2026-02-07T10:00:00",
    "updatedAt": "2026-02-07T10:00:00",
    "roleIds": [1, 2]
  }
]
```

### Get User by ID
```http
GET /users/{id}
```

**Parameters:**
- `id` (path) - User ID

**Response:** Same as single user object above

### Create User
```http
POST /users
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "active": true
}
```

**Response:** `201 Created` with created user object

**Validation:**
- `username`: Required, 3-50 characters, unique
- `email`: Required, valid email format, unique
- `firstName`: Required
- `lastName`: Required
- `phoneNumber`: Optional

### Update User
```http
PUT /users/{id}
Content-Type: application/json
```

**Request Body:** Same as Create User

**Response:** `200 OK` with updated user object

### Delete User
```http
DELETE /users/{id}
```

**Response:** `204 No Content`

---

## Roles API

### Get All Roles
```http
GET /roles
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "ADMIN",
    "description": "System Administrator",
    "createdAt": "2026-02-07T10:00:00",
    "updatedAt": "2026-02-07T10:00:00"
  }
]
```

### Get Role by ID
```http
GET /roles/{id}
```

### Create Role
```http
POST /roles
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "ADMIN",
  "description": "System Administrator"
}
```

**Validation:**
- `name`: Required, 2-50 characters, unique
- `description`: Optional, max 255 characters

### Update Role
```http
PUT /roles/{id}
```

### Delete Role
```http
DELETE /roles/{id}
```

---

## Notification Types API

### Get All Notification Types
```http
GET /notification-types
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "EMAIL",
    "description": "Email notifications",
    "active": true,
    "createdAt": "2026-02-07T10:00:00",
    "updatedAt": "2026-02-07T10:00:00"
  }
]
```

### Get Active Notification Types
```http
GET /notification-types/active
```

Returns only notification types where `active = true`

### Get Notification Type by ID
```http
GET /notification-types/{id}
```

### Create Notification Type
```http
POST /notification-types
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "EMAIL",
  "description": "Email notifications",
  "active": true
}
```

**Validation:**
- `name`: Required, 2-50 characters
- `description`: Optional, max 255 characters
- `active`: Optional, defaults to true

### Update Notification Type
```http
PUT /notification-types/{id}
```

### Delete Notification Type
```http
DELETE /notification-types/{id}
```

---

## Notifications API

### Get All Notifications
```http
GET /notifications
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "System Maintenance",
    "message": "The system will be down for maintenance...",
    "typeId": 1,
    "typeName": "EMAIL",
    "priority": "HIGH",
    "scheduledAt": "2026-02-08T00:00:00",
    "sentAt": null,
    "status": "PENDING",
    "createdAt": "2026-02-07T10:00:00",
    "updatedAt": "2026-02-07T10:00:00"
  }
]
```

### Get Notification by ID
```http
GET /notifications/{id}
```

### Get Notifications by Status
```http
GET /notifications/status/{status}
```

**Parameters:**
- `status` - One of: PENDING, SENT, FAILED

### Get Notifications by Type
```http
GET /notifications/type/{typeId}
```

### Create Notification
```http
POST /notifications
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "System Maintenance",
  "message": "The system will be down for maintenance...",
  "typeId": 1,
  "priority": "HIGH",
  "scheduledAt": "2026-02-08T00:00:00",
  "status": "PENDING"
}
```

**Validation:**
- `title`: Required, 3-200 characters
- `message`: Required, 10-2000 characters
- `typeId`: Required, must reference existing notification type
- `priority`: Optional, defaults to MEDIUM (HIGH, MEDIUM, LOW)
- `status`: Optional, defaults to PENDING (PENDING, SENT, FAILED)

### Update Notification
```http
PUT /notifications/{id}
```

### Delete Notification
```http
DELETE /notifications/{id}
```

---

## User Notifications API

### Get All User Notifications
```http
GET /user-notifications
```

**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "username": "johndoe",
    "notificationId": 1,
    "notificationTitle": "System Maintenance",
    "read": false,
    "readAt": null,
    "deliveredAt": "2026-02-07T10:00:00",
    "createdAt": "2026-02-07T10:00:00"
  }
]
```

### Get User Notification by ID
```http
GET /user-notifications/{id}
```

### Get Notifications for User
```http
GET /user-notifications/user/{userId}
```

Returns all notifications for a specific user

### Get Unread Notifications for User
```http
GET /user-notifications/user/{userId}/unread
```

Returns only unread notifications for a specific user

### Create User Notification
```http
POST /user-notifications
Content-Type: application/json
```

**Request Body:**
```json
{
  "userId": 1,
  "notificationId": 1,
  "read": false,
  "deliveredAt": "2026-02-07T10:00:00"
}
```

**Validation:**
- `userId`: Required, must reference existing user
- `notificationId`: Required, must reference existing notification

### Mark Notification as Read
```http
PUT /user-notifications/{id}/mark-read
```

Sets `read = true` and `readAt = NOW()`

**Response:** Updated user notification object

### Delete User Notification
```http
DELETE /user-notifications/{id}
```

---

## Common Use Cases

### 1. Send a notification to all active users
```bash
# Step 1: Create notification
POST /notifications
{
  "title": "New Feature",
  "message": "Check out our new feature!",
  "typeId": 1,
  "priority": "MEDIUM"
}
# Returns: { "id": 123, ... }

# Step 2: Get all active users
GET /users
# Returns: [{ "id": 1, ... }, { "id": 2, ... }]

# Step 3: Create user_notification for each user
POST /user-notifications { "userId": 1, "notificationId": 123 }
POST /user-notifications { "userId": 2, "notificationId": 123 }
```

### 2. Mark user's notifications as read
```bash
# Get unread notifications
GET /user-notifications/user/1/unread

# Mark each as read
PUT /user-notifications/456/mark-read
```

### 3. Check notification delivery status
```bash
# Get all notifications for a specific notification ID
GET /user-notifications/notification/123

# Filter by delivery status in application layer
```

---

## Testing with cURL

### Create a user
```bash
curl -X POST http://localhost:9080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Get all users
```bash
curl http://localhost:9080/api/v1/users
```

### Update a user
```bash
curl -X PUT http://localhost:9080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User Updated",
    "active": true
  }'
```

### Delete a user
```bash
curl -X DELETE http://localhost:9080/api/v1/users/1
```

---

## Rate Limiting
Currently, there is no rate limiting implemented. For production use, consider implementing rate limiting at the application or infrastructure level.

## Authentication
The current implementation does not include authentication. For production use, implement JWT-based authentication or OAuth 2.0.

## CORS
CORS is configured in the Liberty server to allow requests from:
- `http://localhost:4200` (Angular dev server)
- `http://localhost:9080` (Liberty server)

Additional origins can be configured in `server.xml`.
