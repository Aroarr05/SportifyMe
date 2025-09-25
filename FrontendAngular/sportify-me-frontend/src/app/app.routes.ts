// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { AuthGuard } from './auth/guards/auth.guard';
import { NoAuthGuard } from './auth/guards/no-auth.guard';

export const routes: Routes = [
  // Rutas de autenticación (sin layout)
  {
    path: 'auth',
    loadComponent: () => import('./auth/components/login/login.component').then(m => m.LoginComponent),
    canActivate: [NoAuthGuard]
  },
  
  // Rutas principales (CON layout)
  {
    path: '',
    loadComponent: () => import('./shared/components/layout/layout.component').then(m => m.LayoutComponent),
    children: [
      {
        path: 'desafios',
        children: [
          {
            path: '',
            loadComponent: () => import('./modules/desafios/components/lista-desafios/lista-desafios.component').then(m => m.ListaDesafiosComponent)
          },
          {
            path: 'crear',
            loadComponent: () => import('./modules/desafios/components/crear-desafio/crear-desafio.component').then(m => m.CrearDesafioComponent)
          },
          {
            path: ':id',
            loadComponent: () => import('./modules/desafios/components/detalle-desafio/detalle-desafio.component').then(m => m.DetalleDesafioComponent)
          }
        ]
      },
      {
        path: 'progresos',
        loadComponent: () => import('./modules/progresos/components/mis-progresos/mis-progresos.component').then(m => m.MisProgresosComponent)
      },
      {
        path: 'rankings',
        loadComponent: () => import('./modules/rankings/components/ranking-global/ranking-global.component').then(m => m.RankingGlobalComponent)
      },
      { path: '', redirectTo: 'desafios', pathMatch: 'full' }
    ],
    canActivate: [AuthGuard]
  },
  
  // Rutas por defecto
  { path: '', redirectTo: 'desafios', pathMatch: 'full' },
  { path: '**', redirectTo: 'desafios' }
];