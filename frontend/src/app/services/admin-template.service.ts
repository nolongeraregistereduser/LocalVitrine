import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ActivityType } from './template.service';

export interface AdminTemplateDto {
  id: number;
  name: string;
  code: string;
  description: string;
  activityType: ActivityType;
  previewUrl: string;
  starterHtml: string;
  starterCss: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface AdminTemplatePayload {
  name: string;
  code: string;
  description: string;
  activityType: ActivityType;
  previewUrl: string;
  starterHtml: string;
  starterCss: string;
}

@Injectable({ providedIn: 'root' })
export class AdminTemplateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/admin/templates`;

  listAll(): Observable<AdminTemplateDto[]> {
    return this.http.get<AdminTemplateDto[]>(this.baseUrl);
  }

  create(payload: AdminTemplatePayload): Observable<AdminTemplateDto> {
    return this.http.post<AdminTemplateDto>(this.baseUrl, payload);
  }

  update(id: number, payload: AdminTemplatePayload): Observable<AdminTemplateDto> {
    return this.http.put<AdminTemplateDto>(`${this.baseUrl}/${id}`, payload);
  }

  activate(id: number): Observable<AdminTemplateDto> {
    return this.http.patch<AdminTemplateDto>(`${this.baseUrl}/${id}/activate`, null);
  }

  deactivate(id: number): Observable<AdminTemplateDto> {
    return this.http.patch<AdminTemplateDto>(`${this.baseUrl}/${id}/deactivate`, null);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
