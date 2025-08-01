// modules/desafios/desafios-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../auth/guards/auth.guard';

const routes: Routes = [
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

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DesafiosRoutingModule {}