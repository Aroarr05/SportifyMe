import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListaDesafiosComponent } from './components/lista-desafios/lista-desafios.component';
import { CrearDesafioComponent } from './components/crear-desafio/crear-desafio.component';
import { DetalleDesafioComponent } from './components/detalle-desafio/detalle-desafio.component';

const routes: Routes = [
  { path: '', component: ListaDesafiosComponent, data: { breadcrumb: 'Lista de desafíos' } },
  { path: 'crear', component: CrearDesafioComponent, data: { breadcrumb: 'Crear desafío' } },
  { path: ':id', component: DetalleDesafioComponent, data: { breadcrumb: 'Detalle del desafío' } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DesafiosRoutingModule { }