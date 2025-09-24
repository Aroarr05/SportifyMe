import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { AuthService } from './auth/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule, 
    RouterOutlet, 
    RouterLink, 
    NavbarComponent,
    LoadingSpinnerComponent,
    FooterComponent
  ],
  template: `
    <div class="min-h-screen flex flex-col bg-gray-50">
      
      <!-- Loading Spinner Global -->
      <app-loading-spinner *ngIf="loading"></app-loading-spinner>

      <!-- Navbar siempre visible -->
      <app-navbar />

      <!-- Contenido principal -->
      <main class="flex-grow container mx-auto px-4 py-6">
        <router-outlet></router-outlet>
      </main>

      <!-- Footer siempre visible -->
      <app-footer />
    </div>
  `
})
export class App {
  title = 'SportifyMe';
  loading = false;

  constructor(public authService: AuthService) {}
}