import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AdminUserDto {
  id: number;
  fullName: string;
  email: string;
  role: string;
  status: 'ACTIVE' | 'DISABLED';
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class AdminUserService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/admin/users`;

  list(): Observable<AdminUserDto[]> {
    return this.http.get<AdminUserDto[]>(this.baseUrl);
  }

  getById(id: number): Observable<AdminUserDto> {
    return this.http.get<AdminUserDto>(`${this.baseUrl}/${id}`);
  }

  enable(id: number): Observable<AdminUserDto> {
    return this.http.put<AdminUserDto>(`${this.baseUrl}/${id}/enable`, null);
  }

  disable(id: number): Observable<AdminUserDto> {
    return this.http.put<AdminUserDto>(`${this.baseUrl}/${id}/disable`, null);
  }

  softDelete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
