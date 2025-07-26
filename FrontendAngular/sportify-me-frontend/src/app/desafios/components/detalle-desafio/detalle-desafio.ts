import { Component, OnInit } from '@angular/core';
import { DesafiosService } from '../../services/desafios.service';
import { ActivatedRoute } from '@angular/router';
import { Desafio } from '../../../shared/models';
import { AuthService } from '../../../auth/auth-module';

@Component({
  selector: 'app-detalle-desafio',
  templateUrl: './detalle-desafio.component.html',
  styleUrls: ['./detalle-desafio.component.scss']
})
export class DetalleDesafioComponent implements OnInit {
  desafio?: Desafio;
  ranking: any[] = [];
  comentarios: any[] = [];
  nuevoComentario: string = '';
  yaParticipando: boolean = false;

  constructor(
    private desafiosService: DesafiosService,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarDesafio(id);
      this.cargarRanking(id);
      this.cargarComentarios(id);
      this.verificarParticipacion(id);
    }
  }

  cargarDesafio(id: string) {
    this.desafiosService.obtenerDesafio(id).subscribe({
      next: (desafio) => this.desafio = desafio,
      error: (err) => console.error('Error al cargar desafío:', err)
    });
  }

  cargarRanking(id: string) {
    this.desafiosService.obtenerRankingDesafio(id).subscribe({
      next: (ranking) => this.ranking = ranking,
      error: (err) => console.error('Error al cargar ranking:', err)
    });
  }

  cargarComentarios(id: string) {
    this.desafiosService.obtenerComentariosDesafio(id).subscribe({
      next: (comentarios) => this.comentarios = comentarios,
      error: (err) => console.error('Error al cargar comentarios:', err)
    });
  }

  verificarParticipacion(id: string) {
    this.desafiosService.verificarParticipacion(id).subscribe({
      next: (participando) => this.yaParticipando = participando,
      error: (err) => console.error('Error al verificar participación:', err)
    });
  }

  unirseAlDesafio() {
    if (this.desafio?.id) {
      this.desafiosService.unirseADesafio(this.desafio.id).subscribe({
        next: () => {
          this.yaParticipando = true;
          if (this.desafio) {
            this.cargarDesafio(this.desafio.id);
          }
        },
        error: (err) => alert('Error al unirse al desafío: ' + err.error.message)
      });
    }
  }

  agregarComentario() {
    if (this.desafio?.id && this.nuevoComentario.trim()) {
      this.desafiosService.agregarComentario(this.desafio.id, this.nuevoComentario).subscribe({
        next: () => {
          this.nuevoComentario = '';
          if (this.desafio) {
            this.cargarComentarios(this.desafio.id);
          }
        },
        error: (err) => alert('Error al agregar comentario: ' + err.error.message)
      });
    }
  }
}