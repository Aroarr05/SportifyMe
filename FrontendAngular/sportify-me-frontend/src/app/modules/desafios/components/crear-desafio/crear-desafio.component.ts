import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { DesafiosService } from '../../services/desafios.service';
import { Router, RouterModule } from '@angular/router';
import { TipoActividad } from '../../../../shared/models/desafio.model';
import { CrearDesafioDto } from '../../dto/crear-desafio.dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-crear-desafio',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './crear-desafio.component.html',
  styleUrls: ['./crear-desafio.component.scss']
})
export class CrearDesafioComponent {
  tiposActividad = Object.values(TipoActividad);
  minDate = new Date().toISOString().split('T')[0]; // Para validación de fecha
  
  desafioForm = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    descripcion: ['', [Validators.required, Validators.maxLength(500)]],
    tipoActividad: [TipoActividad.CARRERA, Validators.required],
    objetivo: ['', [Validators.required, Validators.maxLength(200)]],
    fechaLimite: ['', [Validators.required]]
  });

  constructor(
    private fb: FormBuilder,
    private desafiosService: DesafiosService,
    private router: Router
  ) {}

  get f() {
    return this.desafioForm.controls;
  }

  onSubmit() {
    if (this.desafioForm.valid) {
      const desafioData = this.desafioForm.value as CrearDesafioDto;
      this.desafiosService.crearDesafio(desafioData).subscribe({
        next: (desafioCreado) => {
          this.router.navigate(['/desafios', desafioCreado.id]);
        },
        error: (err) => {
          console.error('Error al crear el desafío', err);
        }
      });
    }
  }
}