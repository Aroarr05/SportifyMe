import { Component } from '@angular/core';
import { DesafiosService } from '../../services/desafios.service';
import { Router } from '@angular/router';
import { Desafio } from '../../shared/models/desafio.model';

@Component({
  selector: 'app-crear-desafio',
  templateUrl: './crear-desafio.component.html',
  styleUrls: ['./crear-desafio.component.scss']
})
export class CrearDesafioComponent {
  desafio: Partial<Desafio> = {
    tipoActividad: 'Carrera'
  };

  constructor(
    private desafiosService: DesafiosService,
    private router: Router
  ) {}

  onSubmit() {
    this.desafiosService.crearDesafio(this.desafio as Desafio).subscribe({
      next: () => {
        alert('Desafío creado con éxito');
        this.router.navigate(['/desafios']);
      },
      error: (err) => alert('Error al crear el desafío: ' + err.error.message)
    });
  }
}