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
  templateId: number | null;
  templateName: string | null;
  templateCode: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectPayload {
  title: string;
  status: ProjectStatus;
  publicUrl: string | null;
}

export interface PublishResultDto {
  projectId: number;
  slug: string;
  publicUrl: string;
}

export interface PublicLandingPageDto {
  title: string;
  htmlContent: string;
  cssContent: string;
}

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/projects`;

  list(): Observable<ProjectDto[]> {
    return this.http.get<ProjectDto[]>(this.baseUrl);
  }

  getById(id: number): Observable<ProjectDto> {
    return this.http.get<ProjectDto>(`${this.baseUrl}/${id}`);
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

  assignTemplate(projectId: number, templateId: number): Observable<ProjectDto> {
    return this.http.put<ProjectDto>(`${this.baseUrl}/${projectId}/template/${templateId}`, null);
  }

  publish(projectId: number, slug?: string): Observable<PublishResultDto> {
    return this.http.post<PublishResultDto>(`${this.baseUrl}/${projectId}/publish`, { slug: slug ?? null });
  }

  getPublicLandingPage(slug: string): Observable<PublicLandingPageDto> {
    return this.http.get<PublicLandingPageDto>(`${environment.apiUrl}/public/${slug}`);
  }
}
