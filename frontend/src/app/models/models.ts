export interface User {
  id?: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
  roleIds?: number[];
}

export interface Role {
  id?: number;
  name: string;
  description?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface NotificationType {
  id?: number;
  name: string;
  description?: string;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface Notification {
  id?: number;
  title: string;
  message: string;
  typeId: number;
  typeName?: string;
  priority?: string;
  scheduledAt?: string;
  sentAt?: string;
  status?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface UserNotification {
  id?: number;
  userId: number;
  notificationId: number;
  username?: string;
  notificationTitle?: string;
  read?: boolean;
  readAt?: string;
  deliveredAt?: string;
  createdAt?: string;
}
