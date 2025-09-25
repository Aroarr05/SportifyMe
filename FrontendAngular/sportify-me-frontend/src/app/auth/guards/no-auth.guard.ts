import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})

export class NoAuthGuard implements CanActivate {
  
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    // Si el usuario YA está autenticado, redirigir a la página principal
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/desafios']);
      return false;
    }
    
    // Si NO está autenticado, permitir acceso a login/registro
    return true;
  }
}