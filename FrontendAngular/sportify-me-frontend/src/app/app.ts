import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { AuthService } from './auth/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent],
  template: `
    <div class="min-h-screen flex flex-col bg-gray-50">
      <!-- Navbar -->
      @if (authService.isLoggedIn()) {
        <app-navbar />
      }

      <!-- Contenido principal -->
      <main class="flex-grow-1 container mx-auto px-4 py-6">
        <router-outlet />
      </main>

      <!-- Footer -->
      @if (authService.isLoggedIn()) {
        <footer class="bg-gray-800 text-white text-center py-4 mt-auto">
          <div class="container mx-auto">
            <p class="mb-0">&copy; 2025 SportifyMe</p>
          </div>
        </footer>
      }
    </div>
  `,
  styles: [`
    :host {
      display: block;
      min-height: 100vh;
    }
  `]
})
export class App {
  title = 'SportifyMe';

  constructor(public authService: AuthService) {}
}