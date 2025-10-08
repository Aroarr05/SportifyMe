import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ]
})

export class RegistroComponent implements OnInit {
  registroForm!: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registroForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.registroForm.valid) {
      this.loading = true;
      this.error = null;
      
      const userData = {
        nombre: this.registroForm.get('nombre')?.value || '',
        email: this.registroForm.get('email')?.value || '',
        password: this.registroForm.get('password')?.value || ''
      };

      this.authService.register(userData).subscribe({
        next: () => {
          this.loading = false;
          alert('Registro exitoso. Por favor inicia sesión.');
          this.router.navigate(['/auth/login']);
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Error en el registro. Por favor, intenta nuevamente.';
          console.error('Error en registro:', err);
        }
      });
    }
  }
}