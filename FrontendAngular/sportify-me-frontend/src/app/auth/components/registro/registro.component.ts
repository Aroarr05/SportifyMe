import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent implements OnInit {
  registroForm!: FormGroup;

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
      const userData = {
        nombre: this.registroForm.get('nombre')?.value || '',
        email: this.registroForm.get('email')?.value || '',
        password: this.registroForm.get('password')?.value || ''
      };

      this.authService.register(userData).subscribe({
        next: () => {
          alert('Registro exitoso. Por favor inicia sesión.');
          this.router.navigate(['/auth/login']);
        },
        error: (err) => alert('Error en el registro: ' + (err.error?.message || err.message))
      });
    }
  }
}