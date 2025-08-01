import { Component, OnInit } from '@angular/core';
import { RankingsService } from '../../services/rankings.service';

@Component({
  selector: 'app-ranking-global',
  templateUrl: './ranking-global.component.html',
  styleUrls: ['./ranking-global.component.scss']
})
export class RankingGlobalComponent implements OnInit {
  ranking: any[] = [];
  filtroTipo: string = 'todos';

  constructor(private rankingsService: RankingsService) {}

  ngOnInit(): void {
    this.cargarRanking();
  }

  cargarRanking() {
    this.rankingsService.obtenerRankingGlobal(this.filtroTipo).subscribe({
      next: (ranking) => this.ranking = ranking,
      error: (err) => console.error('Error al cargar ranking:', err)
    });
  }
}