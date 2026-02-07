import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserNotification } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserNotificationService {
  private apiUrl = `${environment.apiUrl}/user-notifications`;

  constructor(private http: HttpClient) {}

  getAllUserNotifications(): Observable<UserNotification[]> {
    return this.http.get<UserNotification[]>(this.apiUrl);
  }

  getUserNotificationById(id: number): Observable<UserNotification> {
    return this.http.get<UserNotification>(`${this.apiUrl}/${id}`);
  }

  getUserNotificationsByUserId(userId: number): Observable<UserNotification[]> {
    return this.http.get<UserNotification[]>(`${this.apiUrl}/user/${userId}`);
  }

  getUnreadUserNotifications(userId: number): Observable<UserNotification[]> {
    return this.http.get<UserNotification[]>(`${this.apiUrl}/user/${userId}/unread`);
  }

  createUserNotification(userNotification: UserNotification): Observable<UserNotification> {
    return this.http.post<UserNotification>(this.apiUrl, userNotification);
  }

  markAsRead(id: number): Observable<UserNotification> {
    return this.http.put<UserNotification>(`${this.apiUrl}/${id}/mark-read`, {});
  }

  deleteUserNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
