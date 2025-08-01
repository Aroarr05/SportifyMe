// modules/desafios/desafios.routes.ts
import { Routes } from '@angular/router';
import { AuthGuard } from '../../auth/guards/auth.guard';

export const DESAFIOS_ROUTES: Routes = [
  { 
    path: '', 
    loadComponent: () => import('./components/lista-desafios/lista-desafios.component')
      .then(m => m.ListaDesafiosComponent),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Desafíos' }
  },
  { 
    path: 'crear', 
    loadComponent: () => import('./components/crear-desafio/crear-desafio.component')
      .then(m => m.CrearDesafioComponent),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Crear Desafío' }
  },
  { 
    path: ':id', 
    loadComponent: () => import('./components/detalle-desafio/detalle-desafio.component')
      .then(m => m.DetalleDesafioComponent),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Detalle' }
  }
];