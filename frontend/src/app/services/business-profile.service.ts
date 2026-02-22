import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface BusinessProfileDto {
  id: number;
  projectId: number;
  businessName: string;
  city: string;
  address: string;
  description: string;
  detailedDescription?: string;
  targetAudience?: string;
  phone: string;
  email: string;
  website?: string;
  goal: string;
  sector: string;
  primaryCTA?: string;
  facebook?: string;
  instagram?: string;
  whatsapp?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BusinessProfilePayload {
  businessName: string;
  city: string;
  address: string;
  description: string;
  detailedDescription?: string | null;
  targetAudience?: string | null;
  phone: string;
  email: string;
  website?: string | null;
  goal: string;
  sector: string;
  primaryCTA?: string | null;
  facebook?: string | null;
  instagram?: string | null;
  whatsapp?: string | null;
}

@Injectable({ providedIn: 'root' })
export class BusinessProfileService {
  private readonly http = inject(HttpClient);

  private url(projectId: number): string {
    return `${environment.apiUrl}/projects/${projectId}/business-profile`;
  }

  get(projectId: number): Observable<BusinessProfileDto> {
    return this.http.get<BusinessProfileDto>(this.url(projectId));
  }

  create(projectId: number, body: BusinessProfilePayload): Observable<BusinessProfileDto> {
    return this.http.post<BusinessProfileDto>(this.url(projectId), body);
  }

  update(projectId: number, body: BusinessProfilePayload): Observable<BusinessProfileDto> {
    return this.http.put<BusinessProfileDto>(this.url(projectId), body);
  }
}
