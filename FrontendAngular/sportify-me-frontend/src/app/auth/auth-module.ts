import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { Usuario } from '../../shared/models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://tu-backend-api.com/api/auth'; // Reemplaza con tu URL
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  currentUser = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // Intenta cargar el usuario desde localStorage al iniciar
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        this.handleAuthentication(response.user, response.token);
      })
    );
  }

  register(user: { nombre: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  loginWithGoogle(): Observable<any> {
    return this.http.get(`${this.apiUrl}/google`).pipe(
      tap((response: any) => {
        this.handleAuthentication(response.user, response.token);
      })
    );
  }

  loginWithGithub(): Observable<any> {
    return this.http.get(`${this.apiUrl}/github`).pipe(
      tap((response: any) => {
        this.handleAuthentication(response.user, response.token);
      })
    );
  }

  private handleAuthentication(user: Usuario, token: string): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    localStorage.setItem('token', token);
    this.currentUserSubject.next(user);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUserValue(): Usuario | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}