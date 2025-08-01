// modules/desafios/desafios.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { DesafiosRoutingModule } from './desafios-routing.module';
import { ListaDesafiosComponent } from './components/lista-desafios/lista-desafios.component';
import { CrearDesafioComponent } from './components/crear-desafio/crear-desafio.component';
import { DetalleDesafioComponent } from './components/detalle-desafio/detalle-desafio.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ListaDesafiosComponent,
    CrearDesafioComponent,
    DetalleDesafioComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    DesafiosRoutingModule,
    SharedModule
  ],
  providers: [DesafiosService]
})
export class DesafiosModule {}