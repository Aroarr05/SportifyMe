import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/desafios', pathMatch: 'full' },
  
  // Auth Routes
  { 
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  // Desafios Routes
  { 
    path: 'desafios', 
    loadChildren: () => import('./modules/desafios/desafios.module').then(m => m.DesafiosModule),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Desafíos' }
  },
  
  // Progresos Routes
  { 
    path: 'progresos', 
    loadChildren: () => import('./modules/progresos/progresos.module').then(m => m.ProgresosModule),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Progresos' }
  },
  
  // Rankings Routes
  { 
    path: 'rankings', 
    loadChildren: () => import('./rankings/rankings.module').then(m => m.RankingsModule),
    canActivate: [AuthGuard],
    data: { breadcrumb: 'Rankings' }
  },
  
  // 404 Not Found
  { path: '**', redirectTo: '/desafios' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }