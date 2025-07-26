import { Component } from '@angular/core';
import { DesafiosService } from '../../services/desafios.service';
import { Router } from '@angular/router';
import { Desafio, TipoActividad } from '../../../shared/models/desafio.model';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-crear-desafio',
  imports:[],
  templateUrl: './crear-desafio.component.html',
  styleUrls: ['./crear-desafio.component.scss']
})

export class CrearDesafioComponent {
  desafio: Partial<Desafio> = {
    tipoActividad: TipoActividad.CARRERA,
    fechaLimite: new Date() // Inicializa con Date object
  };

  tiposActividad = Object.values(TipoActividad);
  minDate = new Date().toISOString().split('T')[0]; // Para la validación HTML
  isLoading = false;

  constructor(
    private desafiosService: DesafiosService,
    public router: Router
  ) {}

  onSubmit(form: NgForm) {
    if (form.invalid) {
      Object.values(form.controls).forEach(control => {
        control.markAsTouched();
      });
      return;
    }

    this.isLoading = true;
    
    // Asegurar que fechaLimite es Date
    const fechaLimite = typeof this.desafio.fechaLimite === 'string' 
      ? new Date(this.desafio.fechaLimite) 
      : this.desafio.fechaLimite;

    const desafioToSend: Desafio = {
      ...this.desafio,
      fechaLimite: fechaLimite!.toISOString() // Conversión a string ISO para el backend
    } as Desafio;

    this.desafiosService.crearDesafio(desafioToSend).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/desafios'], {
          queryParams: { created: true }
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error al crear desafío:', err);
        alert(`Error: ${err.error?.message || 'Por favor intente más tarde'}`);
      }
    });
  }
}