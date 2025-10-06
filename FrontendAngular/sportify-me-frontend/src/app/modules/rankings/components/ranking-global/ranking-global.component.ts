import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; // Añade Router
import { RankingsService } from '../../services/rankings.service';
import { AuthService } from '../../../../auth/services/auth.service';
import { Ranking } from '../../../../shared/models';

@Component({
  standalone: true,
  selector: 'app-ranking-global',
  templateUrl: './ranking-global.component.html',
  styleUrls: ['./ranking-global.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class RankingGlobalComponent implements OnInit {
  loading = false;
  error: string | null = null;
  ranking: Ranking[] = [];
  desafioId: number = 1;

  constructor(
    private rankingsService: RankingsService,
    private authService: AuthService, // Inyecta AuthService
    private router: Router // Inyecta Router
  ) {}

  ngOnInit(): void {
    // Verificar autenticación antes de cargar
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/auth/login']);
      return;
    }
    this.cargarRankingDesafio();
  }

  cargarRankingDesafio(): void {
    this.loading = true;
    this.error = null;
    
    this.rankingsService.obtenerRankingDesafio(this.desafioId).subscribe({
      next: (ranking: Ranking[]) => {
        this.ranking = ranking;
        this.loading = false;
        console.log('✅ Ranking cargado:', ranking);
      },
      error: (err: any) => {
        this.loading = false;
        
        if (err.status === 403 || err.status === 401) {
          this.error = 'Sesión expirada o sin permisos. Redirigiendo al login...';
          this.authService.logout();
          setTimeout(() => {
            this.router.navigate(['/auth/login']);
          }, 2000);
        } else {
          this.error = 'Error al cargar el ranking. Por favor, inténtalo de nuevo.';
          console.error('❌ Error:', err);
        }
      }
    });
  }

  cambiarDesafio(nuevoId: number): void {
    this.desafioId = nuevoId;
    this.cargarRankingDesafio();
  }

  getRowClass(index: number): string {
    if (index === 0) return 'bg-yellow-50';
    if (index === 1) return 'bg-gray-50';
    if (index === 2) return 'bg-orange-50';
    return 'bg-white';
  }

  getMedal(index: number): string {
    if (index === 0) return '🥇';
    if (index === 1) return '🥈';
    if (index === 2) return '🥉';
    return '';
  }

  // Método para reintentar
  reintentar(): void {
    this.cargarRankingDesafio();
  }
}