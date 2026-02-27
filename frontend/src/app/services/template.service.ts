import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type ActivityType = 'RESTAURANT' | 'RETAIL' | 'SERVICES' | 'OTHER';

export interface TemplateDto {
  id: number;
  name: string;
  code: string;
  description: string;
  activityType: ActivityType;
  previewUrl: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({ providedIn: 'root' })
export class TemplateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/templates`;

  list(): Observable<TemplateDto[]> {
    return this.http.get<TemplateDto[]>(this.baseUrl);
  }

  getById(id: number): Observable<TemplateDto> {
    return this.http.get<TemplateDto>(`${this.baseUrl}/${id}`);
  }
}
