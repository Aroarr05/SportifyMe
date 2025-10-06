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
  showUserMenu = false;

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

    // Actualizar título inicial
    this.actualizarTitulo();
  }

  private checkAuthStatus(): void {
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
      '/progresos': 'Mi Progreso',
      '/auth/login': 'Iniciar Sesión',
      '/auth/registro': 'Registrarse'
    };

    // Para rutas de detalle de desafío
    if (rutaActual.startsWith('/desafios/') && !rutaActual.includes('/crear')) {
      this.tituloPagina = 'Detalle del Desafío';
    } 
    // Para rutas de ranking
    else if (rutaActual.startsWith('/rankings')) {
      this.tituloPagina = 'Ranking Global';
    }
    else {
      this.tituloPagina = titulos[rutaActual] || 'SportifyMe';
    }
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.showUserMenu = false;
    this.router.navigate(['/auth/login']);
  }

  irALogin(): void {
    this.router.navigate(['/auth/login']);
  }

  irARegistro(): void {
    this.router.navigate(['/auth/registro']);
  }
}