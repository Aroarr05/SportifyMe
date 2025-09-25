import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {
  
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    // Si el usuario está autenticado, permitir acceso
    if (this.authService.isLoggedIn()) {
      return true;
    }
    
    // Si no está autenticado, redirigir al login
    this.router.navigate(['/auth']);
    return false;
  }
}