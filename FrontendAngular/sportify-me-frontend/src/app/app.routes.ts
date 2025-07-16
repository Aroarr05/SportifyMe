import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/desafios', pathMatch: 'full' },
  { path: 'login', loadChildren: () => import('./auth/auth-module').then(m => m.AuthModule) },
  { path: 'registro', loadChildren: () => import('./auth/auth-module').then(m => m.AuthModule) },
  
  { path: 'desafios', 
    loadChildren: () => import('./desafios/desafios-module').then(m => m.DesafiosModule),
    canActivate: [AuthGuard] 
  },
  
  { path: 'desafio/:id', 
    loadChildren: () => import('./desafios/desafios-module').then(m => m.DesafiosModule),
    canActivate: [AuthGuard] 
  },
  
  { path: 'crear-desafio', 
    loadChildren: () => import('./desafios/desafios-module').then(m => m.DesafiosModule),
    canActivate: [AuthGuard] 
  },
  
  { path: 'registrar-progreso/:desafioId', 
    loadChildren: () => import('./progresos/progresos-module').then(m => m.ProgresosModule),
    canActivate: [AuthGuard] 
  },
  
  { path: 'mis-progresos', 
    loadChildren: () => import('./progresos/progresos-module').then(m => m.ProgresosModule),
    canActivate: [AuthGuard] 
  },
  
  { path: 'ranking', 
    loadChildren: () => import('./rankings/rankings-module').then(m => m.RankingsModule),
    canActivate: [AuthGuard] 
  },
  
  { path: '**', redirectTo: '/desafios' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }