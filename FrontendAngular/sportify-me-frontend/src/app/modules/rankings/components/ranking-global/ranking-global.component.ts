import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RankingsService } from '../../services/rankings.service';
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
  desafioId: number = 1; // ✅ ID del desafío para filtrar

  constructor(private rankingsService: RankingsService) {}

  ngOnInit(): void {
    this.cargarRankingDesafio(); // ✅ Cambiado a cargarRankingDesafio
  }

  // ✅ CORREGIDO: Usa SOLO el método que SÍ existe
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
        this.error = 'Error al cargar el ranking. Por favor, inténtalo de nuevo.';
        this.loading = false;
        console.error('❌ Error:', err);
      }
    });
  }

  // ✅ Método para cambiar de desafío
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

  // ❌ ELIMINA estos métodos que NO se pueden usar:
  // cargarRankingGlobal(): void { ... } // ❌ NO EXISTE
  // onFiltroChange(): void { ... }      // ❌ NO EXISTE
}