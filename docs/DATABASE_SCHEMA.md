# ENS Database Schema Documentation

## Overview
The Enterprise Notification System (ENS) database consists of 5 main tables with clear relationships designed to manage users, roles, notifications, and their associations.

## Database: `ensdb`

### Tables

#### 1. users
Stores application user information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(50) | NOT NULL, UNIQUE | User's login name |
| email | VARCHAR(100) | NOT NULL, UNIQUE | User's email address |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| first_name | VARCHAR(50) | NOT NULL | User's first name |
| last_name | VARCHAR(50) | NOT NULL | User's last name |
| phone_number | VARCHAR(20) | NULL | User's phone number |
| active | BOOLEAN | NOT NULL, DEFAULT true | User active status |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Record last update timestamp |

**Indexes:**
- PRIMARY KEY on `id`
- UNIQUE INDEX on `username`
- UNIQUE INDEX on `email`

---

#### 2. roles
Stores role definitions for access control.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique role identifier |
| name | VARCHAR(50) | NOT NULL, UNIQUE | Role name (e.g., ADMIN, USER) |
| description | VARCHAR(255) | NULL | Role description |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Record last update timestamp |

**Indexes:**
- PRIMARY KEY on `id`
- UNIQUE INDEX on `name`

---

#### 3. user_roles (Junction Table)
Many-to-many relationship between users and roles.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | BIGINT | FOREIGN KEY → users(id) | Reference to user |
| role_id | BIGINT | FOREIGN KEY → roles(id) | Reference to role |

**Indexes:**
- COMPOSITE PRIMARY KEY on `(user_id, role_id)`
- FOREIGN KEY on `user_id` references `users(id)`
- FOREIGN KEY on `role_id` references `roles(id)`

---

#### 4. notification_types
Defines different types of notifications.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique type identifier |
| name | VARCHAR(50) | NOT NULL, UNIQUE | Type name (e.g., EMAIL, SMS, PUSH) |
| description | VARCHAR(255) | NULL | Type description |
| active | BOOLEAN | NOT NULL, DEFAULT true | Type active status |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Record last update timestamp |

**Indexes:**
- PRIMARY KEY on `id`
- UNIQUE INDEX on `name`

---

#### 5. notifications
Stores notification messages.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique notification identifier |
| title | VARCHAR(200) | NOT NULL | Notification title |
| message | VARCHAR(2000) | NOT NULL | Notification message content |
| type_id | BIGINT | FOREIGN KEY → notification_types(id) | Reference to notification type |
| priority | VARCHAR(20) | NOT NULL, DEFAULT 'MEDIUM' | Priority (HIGH, MEDIUM, LOW) |
| scheduled_at | TIMESTAMP | NULL | When to send notification |
| sent_at | TIMESTAMP | NULL | When notification was sent |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | Status (PENDING, SENT, FAILED) |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Record last update timestamp |

**Indexes:**
- PRIMARY KEY on `id`
- FOREIGN KEY on `type_id` references `notification_types(id)`
- INDEX on `status`

---

#### 6. user_notifications (Junction Table)
Many-to-many relationship tracking which notifications are sent to which users.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique record identifier |
| user_id | BIGINT | FOREIGN KEY → users(id) | Reference to user |
| notification_id | BIGINT | FOREIGN KEY → notifications(id) | Reference to notification |
| read | BOOLEAN | NOT NULL, DEFAULT false | Whether notification was read |
| read_at | TIMESTAMP | NULL | When notification was read |
| delivered_at | TIMESTAMP | NULL | When notification was delivered |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |

**Indexes:**
- PRIMARY KEY on `id`
- FOREIGN KEY on `user_id` references `users(id)`
- FOREIGN KEY on `notification_id` references `notifications(id)`
- INDEX on `user_id`
- INDEX on `read`

---

## Entity Relationships

```
users ←→ user_roles ←→ roles
  ↓
user_notifications ←→ notifications → notification_types
```

### Relationship Details:

1. **Users ↔ Roles** (Many-to-Many)
   - A user can have multiple roles
   - A role can be assigned to multiple users
   - Implemented via `user_roles` junction table

2. **Users ↔ Notifications** (Many-to-Many)
   - A user can receive multiple notifications
   - A notification can be sent to multiple users
   - Implemented via `user_notifications` junction table with additional metadata (read status, delivery time)

3. **Notifications → NotificationTypes** (Many-to-One)
   - Each notification has one type
   - A type can be used by many notifications

---

## Sample Data

### Sample Users
```sql
INSERT INTO users (username, email, password, first_name, last_name, active, created_at, updated_at)
VALUES 
('admin', 'admin@ens.com', 'hashed_password', 'Admin', 'User', true, NOW(), NOW()),
('jdoe', 'john.doe@example.com', 'hashed_password', 'John', 'Doe', true, NOW(), NOW());
```

### Sample Roles
```sql
INSERT INTO roles (name, description, created_at, updated_at)
VALUES 
('ADMIN', 'System Administrator', NOW(), NOW()),
('USER', 'Regular User', NOW(), NOW());
```

### Sample Notification Types
```sql
INSERT INTO notification_types (name, description, active, created_at, updated_at)
VALUES 
('EMAIL', 'Email notification', true, NOW(), NOW()),
('SMS', 'SMS notification', true, NOW(), NOW()),
('PUSH', 'Push notification', true, NOW(), NOW());
```

---

## Database Configuration

### PostgreSQL Setup
```sql
-- Create database
CREATE DATABASE ensdb;

-- Create user
CREATE USER ens_user WITH PASSWORD 'your_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE ensdb TO ens_user;
```

### Connection String
```
jdbc:postgresql://localhost:5432/ensdb?user=ens_user&password=your_password
```

---

## Maintenance

### Indexes
- All foreign keys are indexed automatically
- Consider adding indexes on frequently queried columns:
  - `notifications.created_at`
  - `user_notifications.delivered_at`

### Backup
Regular backups recommended:
```bash
pg_dump -U ens_user ensdb > backup_$(date +%Y%m%d).sql
```

### Restore
```bash
psql -U ens_user ensdb < backup_20260207.sql
```
