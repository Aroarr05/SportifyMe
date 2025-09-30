import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  imports: [CommonModule, RouterModule]
})
export class LayoutComponent implements OnInit {
  tituloPagina = 'SportifyMe';
  isLoggedIn = false;
  usuarioLogueado: any = null;

  menuItems = [
    { path: '/desafios', label: 'Desafíos', icon: '🏆' },
    { path: '/rankings', label: 'Ranking', icon: '📊' },
    { path: '/progresos', label: 'Mi Progreso', icon: '📈' }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Verificar estado de autenticación
    this.checkAuthStatus();
    
    // Escuchar cambios de ruta
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.actualizarTitulo();
      });
  }

  private checkAuthStatus(): void {
    // Usar tu servicio de autenticación real
    this.isLoggedIn = this.authService.isAuthenticated();
    if (this.isLoggedIn) {
      this.usuarioLogueado = this.authService.getCurrentUser();
    }
  }

  getInicialesUsuario(): string {
    if (!this.usuarioLogueado?.nombre) return 'U';
    
    return this.usuarioLogueado.nombre
      .split(' ')
      .map((palabra: string) => palabra.charAt(0))
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }

  private actualizarTitulo(): void {
    const rutaActual = this.router.url;
    
    const titulos: { [key: string]: string } = {
      '/desafios': 'Desafíos',
      '/desafios/crear': 'Crear Desafío',
      '/rankings': 'Ranking Global',
      '/progresos': 'Mi Progreso'
    };

    // Para rutas de detalle de desafío
    if (rutaActual.startsWith('/desafios/') && !rutaActual.includes('/crear')) {
      this.tituloPagina = 'Detalle del Desafío';
    } else {
      this.tituloPagina = titulos[rutaActual] || 'SportifyMe';
    }
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }

  irALogin(): void {
    this.router.navigate(['/auth']);
  }
}