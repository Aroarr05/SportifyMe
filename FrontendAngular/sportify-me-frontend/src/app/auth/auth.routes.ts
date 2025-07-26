import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro.component';
import { NoAuthGuard } from './guards/no-auth.guard'; // Solo si lo implementaste

export const AuthRoutes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent,
    canActivate: [NoAuthGuard] // Opcional
  },
  { 
    path: 'registro', 
    component: RegistroComponent,
    canActivate: [NoAuthGuard] // Opcional
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];