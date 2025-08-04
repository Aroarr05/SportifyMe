import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio } from '../../../../shared/models';

@Component({
  standalone: true,
  selector: 'app-detalle-desafio',
  templateUrl: './detalle-desafio.component.html',
  styleUrls: ['./detalle-desafio.component.scss']
})

export class DetalleDesafioComponent implements OnInit {
  desafio: Desafio | null = null;
  loading = true;
  error: string | null = null;
  unirseLoading = false;

  constructor(
    private route: ActivatedRoute,
    private desafiosService: DesafiosService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarDesafio(+id);
    }
  }

  cargarDesafio(id: number): void {
    this.loading = true;
    this.desafiosService.getDesafioById(id).subscribe({
      next: (desafio) => {
        this.desafio = desafio;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el desafío. Por favor, inténtalo de nuevo más tarde.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  unirseADesafio(): void {
    if (!this.desafio) return;

    this.unirseLoading = true;
    this.desafiosService.unirseADesafio(this.desafio.id).subscribe({
      next: (desafioActualizado) => {
        this.desafio = desafioActualizado;
        this.unirseLoading = false;
      },
      error: (err) => {
        this.error = 'Error al unirse al desafío. Por favor, inténtalo de nuevo.';
        this.unirseLoading = false;
        console.error(err);
      }
    });
  }
}