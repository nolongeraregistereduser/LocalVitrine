import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ProjectContentDto {
  projectId: number;
  htmlContent: string | null;
  cssContent: string | null;
  updatedAt: string;
}

export interface ProjectContentPayload {
  htmlContent: string;
  cssContent: string | null;
}

@Injectable({ providedIn: 'root' })
export class ProjectEditorService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/projects`;

  getContent(projectId: number): Observable<ProjectContentDto> {
    return this.http.get<ProjectContentDto>(`${this.baseUrl}/${projectId}/content`);
  }

  saveContent(projectId: number, payload: ProjectContentPayload): Observable<ProjectContentDto> {
    return this.http.put<ProjectContentDto>(`${this.baseUrl}/${projectId}/content`, payload);
  }
}
