import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RankingsService } from '../../services/rankings.service'; // ← RankingsService (con 's')
import { Ranking, RankingDesafio } from '../../../../shared/models';

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
  filtroTipo = 'todos';
  vista = 'global';
  rankingDesafio: RankingDesafio | null = null;

  constructor(private rankingsService: RankingsService) {} // ← RankingsService (con 's')

  ngOnInit(): void {
    this.cargarRankingGlobal();
  }

  cargarRankingGlobal(): void {
    this.loading = true;
    this.error = null;
    this.vista = 'global';
    
    this.rankingsService.obtenerRankingGlobal(this.filtroTipo).subscribe({
      next: (ranking) => {
        this.ranking = ranking;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el ranking. Por favor, inténtalo de nuevo.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onFiltroChange(): void {
    this.cargarRankingGlobal();
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
}