import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
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

export class DetalleDesafioComponent implements OnInit, OnDestroy {
  desafio: Desafio | null = null;
  loading = true;
  error: string | null = null;
  unirseLoading = false;
  private routeSub?: Subscription;
  
  // CAMBIO: Quitar 'private' para hacerla pública y accesible desde el template
  usuarioActualId = 1; // Ejemplo - reemplazar con lógica real

  constructor(
    private route: ActivatedRoute,
    private desafiosService: DesafiosService
  ) { }

  ngOnInit(): void {
    // Usar observable para cambios de ruta
    this.routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.cargarDesafio(+id);
      } else {
        this.error = 'No se encontró el ID del desafío';
        this.loading = false;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }

  cargarDesafio(id: number): void {
    this.loading = true;
    this.error = null;
    
    this.desafiosService.obtenerDesafioPorId(id).subscribe({
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

    // Validaciones adicionales
    if (this.yaEsParticipante()) {
      this.error = 'Ya eres participante de este desafío';
      return;
    }

    if (this.esDesafioExpirado()) {
      this.error = 'No puedes unirte a un desafío expirado';
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

  esDesafioExpirado(): boolean {
    if (!this.desafio?.fechaLimite) return false;
    return new Date(this.desafio.fechaLimite) < new Date();
  }

  yaEsParticipante(): boolean {
    if (!this.desafio?.participantes || !this.usuarioActualId) return false;
    return this.desafio.participantes.some(p => p.id === this.usuarioActualId);
  }

  // Método para obtener el texto del botón según el estado
  getTextoBoton(): string {
    if (this.yaEsParticipante()) return 'Ya participas';
    if (this.esDesafioExpirado()) return 'Desafío Expirado';
    return 'Unirse al Desafío';
  }
}