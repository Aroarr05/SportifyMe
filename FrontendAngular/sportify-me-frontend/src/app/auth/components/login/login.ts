import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  credentials = {
    email: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.credentials).subscribe({
      next: () => this.router.navigate(['/desafios']),
      error: (err) => alert(err.error.message)
    });
  }

  loginWithGoogle() {
    this.authService.loginWithGoogle().subscribe({
      next: () => this.router.navigate(['/desafios']),
      error: (err) => alert(err.error.message)
    });
  }

  loginWithGithub() {
    this.authService.loginWithGithub().subscribe({
      next: () => this.router.navigate(['/desafios']),
      error: (err) => alert(err.error.message)
    });
  }
}