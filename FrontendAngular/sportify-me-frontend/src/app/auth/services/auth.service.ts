import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs'; // ← Añadir BehaviorSubject
import { tap } from 'rxjs/operators';
import { environment } from '../../../enviroments/environment';
import { Usuario } from '../../shared/models/usuario.model'; // ← Añadir import

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // ← AÑADIR ESTAS DOS LÍNEAS
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // ← AÑADIR: Verificar si hay token al iniciar
    if (this.isAuthenticated()) {
      // Aquí podrías hacer una llamada para obtener datos del usuario actual
      // Por ahora, creamos un usuario básico desde el token
      this.loadUserFromToken();
    }
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token);
        // ← AÑADIR: Actualizar usuario actual
        if (response.user) {
          this.currentUserSubject.next(response.user);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    // ← AÑADIR: Limpiar usuario actual
    this.currentUserSubject.next(null);
  }

  // ← AÑADIR ESTE MÉTODO
  private loadUserFromToken(): void {
    // Si tienes endpoint para obtener usuario actual
    // this.http.get(`${this.apiUrl}/me`).subscribe(user => this.currentUserSubject.next(user));
    
    // Por ahora, usuario básico basado en token
    const token = localStorage.getItem('token');
    if (token) {
      // Crear usuario temporal (reemplaza con datos reales del backend)
      const user: Usuario = {
        id: 1,
        nombre: 'Usuario',
        email: 'usuario@example.com'
      };
      this.currentUserSubject.next(user);
    }
  }

  // ← AÑADIR: Método para establecer usuario
  setCurrentUser(user: Usuario): void {
    this.currentUserSubject.next(user);
  }
}