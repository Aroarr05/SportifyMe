import { Routes } from '@angular/router';
import { AuthGuard } from './auth/guards/auth.guard';
import { NoAuthGuard } from './auth/guards/no-auth.guard';

export const routes: Routes = [
  // RUTAS PÚBLICAS (sin layout)
  {
    path: 'auth',
    loadComponent: () => import('./auth/components/login/login.component').then(m => m.LoginComponent),
    canActivate: [NoAuthGuard]
  },

  // RUTAS CON LAYOUT (protegidas)
  {
    path: '',
    loadComponent: () => import('./shared/components/layout/layout.component').then(m => m.LayoutComponent),
    children: [
      // Desafíos
      {
        path: 'desafios',
        children: [
          {
            path: '',
            loadComponent: () => import('./modules/desafios/components/lista-desafios/lista-desafios.component').then(m => m.ListaDesafiosComponent)
          },
          {
            path: 'crear',
            loadComponent: () => import('./modules/desafios/components/crear-desafio/crear-desafio.component').then(m => m.CrearDesafioComponent),
            canActivate: [AuthGuard]
          },
          {
            path: ':id',
            loadComponent: () => import('./modules/desafios/components/detalle-desafio/detalle-desafio.component').then(m => m.DetalleDesafioComponent)
          }
        ]
      },
      
      // Progresos
      {
        path: 'progresos',
        loadComponent: () => import('./modules/progresos/components/mis-progresos/mis-progresos.component').then(m => m.MisProgresosComponent),
        canActivate: [AuthGuard]
      },
      
      // Rankings
      {
        path: 'rankings',
        loadComponent: () => import('./modules/rankings/components/ranking-global/ranking-global.component').then(m => m.RankingGlobalComponent)
      },
      
      // Ruta por defecto
      { path: '', redirectTo: 'desafios', pathMatch: 'full' }
    ]
  },
  
  // Redirecciones
  { path: '**', redirectTo: 'desafios' }
];