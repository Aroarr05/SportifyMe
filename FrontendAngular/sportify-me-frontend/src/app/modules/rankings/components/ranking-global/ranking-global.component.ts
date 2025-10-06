import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RankingsService } from '../../services/rankings.service';
import { AuthService } from '../../../../auth/services/auth.service';
import { RankingDesafio, Ranking } from '../../../../shared/models';

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
  nombreDesafio: string = '';
  desafioId: number = 1;

  constructor(
    private rankingsService: RankingsService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
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
      next: (rankingDesafio: RankingDesafio) => {
        this.ranking = rankingDesafio.ranking || [];
        this.nombreDesafio = rankingDesafio.nombreDesafio || `Desafío ${this.desafioId}`;
        this.loading = false;
        console.log('✅ Ranking cargado:', rankingDesafio);
      },
      error: (err: any) => {
        this.loading = false;
        
        if (err.status === 403 || err.status === 401) {
          this.error = 'Sesión expirada o sin permisos. Redirigiendo al login...';
          this.authService.logout();
          setTimeout(() => {
            this.router.navigate(['/auth/login']);
          }, 2000);
        } else if (err.status === 404) {
          this.error = 'No se encontró el ranking para este desafío.';
          this.ranking = [];
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

  reintentar(): void {
    this.cargarRankingDesafio();
  }
}