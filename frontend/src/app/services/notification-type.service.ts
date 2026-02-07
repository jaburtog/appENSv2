import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotificationType } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationTypeService {
  private apiUrl = `${environment.apiUrl}/notification-types`;

  constructor(private http: HttpClient) {}

  getAllNotificationTypes(): Observable<NotificationType[]> {
    return this.http.get<NotificationType[]>(this.apiUrl);
  }

  getActiveNotificationTypes(): Observable<NotificationType[]> {
    return this.http.get<NotificationType[]>(`${this.apiUrl}/active`);
  }

  getNotificationTypeById(id: number): Observable<NotificationType> {
    return this.http.get<NotificationType>(`${this.apiUrl}/${id}`);
  }

  createNotificationType(type: NotificationType): Observable<NotificationType> {
    return this.http.post<NotificationType>(this.apiUrl, type);
  }

  updateNotificationType(id: number, type: NotificationType): Observable<NotificationType> {
    return this.http.put<NotificationType>(`${this.apiUrl}/${id}`, type);
  }

  deleteNotificationType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
