import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/services/auth.service';
import { Usuario } from '../../models/usuario.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  isAuthenticated = false;
  currentUser?: Usuario;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // ← CAMBIA currentUser por currentUser$
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = !!user;
      this.currentUser = user || undefined;
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']); // ← Ajusta la ruta según tu configuración
  }
}