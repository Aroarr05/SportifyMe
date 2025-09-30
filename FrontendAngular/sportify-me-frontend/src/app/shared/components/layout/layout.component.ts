import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  imports: [CommonModule, RouterModule]
})
export class LayoutComponent {
  usuarioLogueado = { nombre: 'Juan Pérez', email: 'juan@ejemplo.com' };
  isLoggedIn = true;

  menuItems = [
    { path: '/desafios', label: 'Desafíos', icon: '🏆' },
    { path: '/ranking', label: 'Ranking', icon: '📊' },
    { path: '/progreso', label: 'Mi Progreso', icon: '📈' },
    { path: '/comunidad', label: 'Comunidad', icon: '👥' },
    { path: '/perfil', label: 'Mi Perfil', icon: '👤' }
  ];

  // Método para obtener las iniciales del usuario
  getInicialesUsuario(): string {
    if (!this.usuarioLogueado?.nombre) return 'U';
    
    return this.usuarioLogueado.nombre
      .split(' ')
      .map(palabra => palabra.charAt(0))
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }
}