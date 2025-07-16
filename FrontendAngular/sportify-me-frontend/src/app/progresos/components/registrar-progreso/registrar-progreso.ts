import { Component, OnInit } from '@angular/core';
import { ProgresosService } from '../../services/progresos.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DesafiosService } from '../../../desafios/services/desafios.service';
import { Desafio } from '../../../shared/models/desafio.model';

@Component({
  selector: 'app-registrar-progreso',
  templateUrl: './registrar-progreso.component.html',
  styleUrls: ['./registrar-progreso.component.scss']
})
export class RegistrarProgresoComponent implements OnInit {
  desafio?: Desafio;
  progreso: any = {
    valor: null,
    unidad: 'km',
    fecha: new Date().toISOString().split('T')[0],
    comentario: ''
  };

  constructor(
    private progresosService: ProgresosService,
    private desafiosService: DesafiosService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const desafioId = this.route.snapshot.paramMap.get('desafioId');
    if (desafioId) {
      this.desafiosService.obtenerDesafio(desafioId).subscribe({
        next: (desafio) => this.desafio = desafio,
        error: (err) => console.error('Error al cargar desafío:', err)
      });
    }
  }

  onSubmit() {
    if (this.desafio?.id) {
      this.progresosService.registrarProgreso(this.desafio.id, this.progreso).subscribe({
        next: () => {
          alert('Progreso registrado con éxito');
          this.router.navigate(['/desafio', this.desafio?.id]);
        },
        error: (err) => alert('Error al registrar progreso: ' + err.error.message)
      });
    }
  }

  cancelar() {
    if (this.desafio?.id) {
      this.router.navigate(['/desafio', this.desafio.id]);
    } else {
      this.router.navigate(['/desafios']);
    }
  }
}