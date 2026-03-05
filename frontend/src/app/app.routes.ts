import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () => import('./pages/landing/landing.component').then((m) => m.LandingComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'templates',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/templates/templates-page.component').then((m) => m.TemplatesPageComponent)
  },
  {
    path: 'projects/:projectId',
    canActivate: [authGuard],
    loadComponent: () => import('./features/project/project-detail.component').then((m) => m.ProjectDetailComponent)
  },
  {
    path: 'projects/:projectId/editor',
    canActivate: [authGuard],
    loadComponent: () => import('./features/project/editor/editor.component').then((m) => m.EditorComponent)
  },
  {
    path: 'projects/:projectId/setup/business',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/project/setup/project-setup-business.component').then((m) => m.ProjectSetupBusinessComponent)
  },
  {
    path: 'projects/:projectId/setup/template',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/project/setup/project-setup-template.component').then((m) => m.ProjectSetupTemplateComponent)
  },
  {
    path: 'projects/:projectId/setup/editor',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/project/setup/project-setup-editor.component').then((m) => m.ProjectSetupEditorComponent)
  },
  {
    path: 'projects/:projectId/setup/publish',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/project/setup/project-setup-publish.component').then((m) => m.ProjectSetupPublishComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard, adminGuard],
    loadComponent: () => import('./features/admin/admin-layout.component').then((m) => m.AdminLayoutComponent),
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/admin/admin-dashboard.component').then((m) => m.AdminDashboardComponent)
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./features/admin/admin-users-page.component').then((m) => m.AdminUsersPageComponent)
      },
      {
        path: 'templates',
        loadComponent: () =>
          import('./features/admin/admin-template-page.component').then((m) => m.AdminTemplatePageComponent)
      },
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' }
    ]
  },
  { path: '**', redirectTo: '' }
];
