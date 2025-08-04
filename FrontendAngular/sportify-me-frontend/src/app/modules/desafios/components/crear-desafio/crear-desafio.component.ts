import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DesafiosService } from '../../services/desafios.service';
import { TipoActividad } from '../../../../shared/models';
import { ErrorAlertComponent } from '../../../../shared/components/error-alert/error-alert.component';

@Component({
  standalone: true,
  selector: 'app-crear-desafio',
  templateUrl: './crear-desafio.component.html',
  styleUrls: ['./crear-desafio.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    ErrorAlertComponent
  ]
})
export class CrearDesafioComponent implements OnInit {
  desafioForm: FormGroup;
  tiposActividad = Object.values(TipoActividad);
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private desafiosService: DesafiosService,
    private router: Router
  ) {
    this.desafioForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: ['', [Validators.required, Validators.maxLength(500)]],
      tipoActividad: [TipoActividad.CARRERA, Validators.required],
      objetivo: ['', [Validators.required, Validators.maxLength(200)]],
      fechaLimite: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.desafioForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const desafioDto = this.desafioForm.value;
    this.desafiosService.crearDesafio(desafioDto).subscribe({
      next: (desafio) => {
        this.loading = false;
        this.router.navigate(['/desafios', desafio.id]);
      },
      error: (err) => {
        this.error = 'Error al crear el desafío. Por favor, inténtalo de nuevo.';
        this.loading = false;
        console.error(err);
      }
    });
  }
}