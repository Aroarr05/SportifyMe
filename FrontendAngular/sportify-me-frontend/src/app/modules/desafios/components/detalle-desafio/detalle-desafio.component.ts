import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio } from '../../../../shared/models';
import { LoadingSpinnerComponent } from '../../../../shared/components/loading-spinner/loading-spinner.component';
import { ErrorAlertComponent } from '../../../../shared/components/error-alert/error-alert.component';

@Component({
  standalone: true,
  selector: 'app-detalle-desafio',
  templateUrl: './detalle-desafio.component.html',
  styleUrls: ['./detalle-desafio.component.scss'],
  imports: [
    CommonModule,
    RouterModule,
    LoadingSpinnerComponent,
    ErrorAlertComponent
  ]
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
    } else {
      this.error = 'No se encontró el ID del desafío';
      this.loading = false;
    }
  }

  cargarDesafio(id: number): void {
    this.loading = true;
    this.error = null;
    
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
    if (!this.desafio) {
      this.error = 'No se puede unir a un desafío no cargado';
      return;
    }

    this.unirseLoading = true;
    this.error = null;

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