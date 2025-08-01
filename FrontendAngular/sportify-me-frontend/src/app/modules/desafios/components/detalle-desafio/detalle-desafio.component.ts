import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio, Progreso, Comentario, Usuario } from '../../../../shared/models';
import { AuthService } from '../../../../auth/services/auth.service';

@Component({
  selector: 'app-detalle-desafio',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './detalle-desafio.component.html',
  styleUrls: ['./detalle-desafio.component.scss']
})
export class DetalleDesafioComponent implements OnInit {
  desafio?: Desafio;
  loading = true;
  error: string | null = null;
  participando = false;
  nuevoComentario = '';
  comentarios: Comentario[] = [];
  ranking: {usuario: Usuario, progreso: string}[] = [];
  usuarioActual?: Usuario;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private desafiosService: DesafiosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Obtener usuario actual
    this.usuarioActual = this.authService.getUsuarioActual();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarDesafio(+id);
      this.cargarComentarios(+id);
      this.cargarRanking(+id);
    }
  }

  cargarDesafio(id: number): void {
    this.desafiosService.getDesafioById(id).subscribe({
      next: (desafio) => {
        this.desafio = desafio;
        this.participando = this.checkParticipacion(desafio);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el desafío';
        this.loading = false;
      }
    });
  }

  cargarComentarios(desafioId: number): void {
    this.desafiosService.getComentarios(desafioId).subscribe({
      next: (comentarios) => this.comentarios = comentarios,
      error: (err) => console.error('Error cargando comentarios', err)
    });
  }

  cargarRanking(desafioId: number): void {
    this.desafiosService.getRanking(desafioId).subscribe({
      next: (ranking) => this.ranking = ranking,
      error: (err) => console.error('Error cargando ranking', err)
    });
  }

  unirseAlDesafio(): void {
    if (this.desafio) {
      this.desafiosService.unirseADesafio(this.desafio.id).subscribe({
        next: (desafioActualizado) => {
          this.desafio = desafioActualizado;
          this.participando = true;
        },
        error: (err) => {
          console.error('Error al unirse al desafío', err);
        }
      });
    }
  }

  abandonarDesafio(): void {
    if (this.desafio) {
      this.desafiosService.abandonarDesafio(this.desafio.id).subscribe({
        next: () => {
          this.participando = false;
        },
        error: (err) => {
          console.error('Error al abandonar el desafío', err);
        }
      });
    }
  }

  agregarComentario(): void {
    if (this.nuevoComentario.trim() && this.desafio && this.usuarioActual) {
      const nuevoComentario: Omit<Comentario, 'id' | 'fecha'> = {
        texto: this.nuevoComentario,
        usuarioId: this.usuarioActual.id,
        desafioId: this.desafio.id
      };

      this.desafiosService.agregarComentario(nuevoComentario).subscribe({
        next: (comentario) => {
          this.comentarios.unshift(comentario);
          this.nuevoComentario = '';
        },
        error: (err) => {
          console.error('Error al agregar comentario', err);
        }
      });
    }
  }

  private checkParticipacion(desafio: Desafio): boolean {
    return this.usuarioActual 
      ? desafio.participantes?.some(p => p.id === this.usuarioActual?.id) || false
      : false;
  }
}