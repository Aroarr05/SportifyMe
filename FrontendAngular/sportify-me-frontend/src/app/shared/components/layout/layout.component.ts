import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { FooterComponent } from '../footer/footer.component'; // ← Tu footer
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent, FooterComponent],
  template: `
    <div class="min-h-screen flex flex-col bg-gray-50">
      <!-- Navbar -->
      @if (authService.isLoggedIn()) {
        <app-navbar />
      }

      <!-- Contenido principal -->
      <main class="flex-grow container mx-auto px-4 py-6">
        <router-outlet />
      </main>

      <!-- Footer -->
      @if (authService.isLoggedIn()) {
        <app-footer />
      }
    </div>
  `
})
export class LayoutComponent {
  constructor(public authService: AuthService) {}
}