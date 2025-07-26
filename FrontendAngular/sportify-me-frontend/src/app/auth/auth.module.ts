import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Componentes (nota la extensión .component en los imports)
import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro.component';

// Servicios
import { AuthService } from './services/auth.service';

// Guards
import { AuthGuard } from './guards/auth.guard';
// Importa NoAuthGuard solo si lo vas a usar
import { NoAuthGuard } from './guards/no-auth.guard';

// Rutas
import { AuthRoutes } from './auth.routes';

@NgModule({
  declarations: [
    LoginComponent,
    RegistroComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(AuthRoutes)
  ],
  providers: [
    AuthService,
    AuthGuard,
    NoAuthGuard // Solo si lo creaste
  ]
})
export class AuthModule { }