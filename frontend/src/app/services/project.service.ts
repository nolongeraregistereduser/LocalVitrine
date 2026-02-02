import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type ProjectStatus = 'DRAFT' | 'PUBLISHED';

export interface ProjectDto {
  id: number;
  title: string;
  status: ProjectStatus;
  publicUrl: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectPayload {
  title: string;
  status: ProjectStatus;
  publicUrl: string | null;
}

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/projects`;

  list(): Observable<ProjectDto[]> {
    return this.http.get<ProjectDto[]>(this.baseUrl);
  }

  create(body: ProjectPayload): Observable<ProjectDto> {
    return this.http.post<ProjectDto>(this.baseUrl, body);
  }

  update(id: number, body: ProjectPayload): Observable<ProjectDto> {
    return this.http.put<ProjectDto>(`${this.baseUrl}/${id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
