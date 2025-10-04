import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Usuario } from '../../shared/models';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterData {
  nombre: string;
  email: string;
  password: string;
  confirmPassword?: string;
}

export interface AuthResponse {
  token: string;
  user: Usuario;
  message?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient) {
    this.initializeAuth();
  }

  /**
   * Inicializa el estado de autenticación al cargar el servicio
   */
  private initializeAuth(): void {
    const token = this.getToken();
    if (token && !this.isTokenExpired(token)) {
      this.loadUserFromToken();
    } else {
      this.clearAuth();
    }
  }

  /**
   * Verifica si existe un token en localStorage
   */
  private hasToken(): boolean {
    return !!localStorage.getItem('authToken');
  }

  /**
   * Obtiene el token del localStorage
   */
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  /**
   * Verifica si el token está expirado (implementación básica)
   */
  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiration = payload.exp * 1000; // Convertir a milisegundos
      return Date.now() > expiration;
    } catch (error) {
      return true; // Si hay error, considerar como expirado
    }
  }

  /**
   * Login de usuario
   */
  login(credentials: LoginCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: AuthResponse) => {
        this.handleAuthentication(response);
      })
    );
  }

  /**
   * Registro de nuevo usuario
   */
  register(userData: RegisterData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      tap((response: AuthResponse) => {
        this.handleAuthentication(response);
      })
    );
  }

  /**
   * Maneja la respuesta de autenticación exitosa
   */
  private handleAuthentication(response: AuthResponse): void {
    // Guardar token en localStorage
    localStorage.setItem('authToken', response.token);
    
    // Actualizar usuario actual
    this.currentUserSubject.next(response.user);
    this.isLoggedInSubject.next(true);
    
    console.log('Usuario autenticado:', response.user.nombre);
  }

  /**
   * Carga información del usuario desde el token (simulación)
   */
  private loadUserFromToken(): void {
    const token = this.getToken();
    if (token) {
      // En una app real, harías una llamada al backend para verificar el token
      // y obtener los datos del usuario
      const mockUser: Usuario = {
        id: 1,
        nombre: 'Usuario Demo',
        email: 'usuario@demo.com',
        fechaRegistro: new Date()
      };
      this.currentUserSubject.next(mockUser);
      this.isLoggedInSubject.next(true);
    }
  }

  /**
   * Cierra la sesión del usuario
   */
  logout(): void {
    this.clearAuth();
    console.log('Sesión cerrada');
  }

  /**
   * Limpia todos los datos de autenticación
   */
  private clearAuth(): void {
    localStorage.removeItem('authToken');
    this.currentUserSubject.next(null);
    this.isLoggedInSubject.next(false);
  }

  /**
   * Verifica si el usuario está autenticado (método síncrono)
   */
  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }

  /**
   * Observable del estado de autenticación
   */
  get isLoggedIn$(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
  }

  /**
   * Verifica si el usuario está autenticado (alias de isLoggedIn)
   */
  isAuthenticated(): boolean {
    return this.isLoggedIn();
  }

  /**
   * Obtiene el usuario actual (síncrono)
   */
  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  /**
   * Actualiza los datos del usuario actual
   */
  updateCurrentUser(user: Usuario): void {
    this.currentUserSubject.next(user);
  }

  /**
   * Verifica si el usuario tiene un rol específico (para futuras implementaciones)
   */
  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    // Implementar lógica de roles si es necesaria
    return user !== null;
  }

  /**
   * Login con redes sociales (para futuras implementaciones)
   */
  loginWithGoogle(): Observable<AuthResponse> {
    // Implementar lógica de Google OAuth
    return this.http.post<AuthResponse>(`${this.apiUrl}/google`, {});
  }

  loginWithFacebook(): Observable<AuthResponse> {
    // Implementar lógica de Facebook OAuth
    return this.http.post<AuthResponse>(`${this.apiUrl}/facebook`, {});
  }

  /**
   * Solicitar recuperación de contraseña
   */
  forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/forgot-password`, { email });
  }

  /**
   * Restablecer contraseña
   */
  resetPassword(token: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/reset-password`, {
      token,
      newPassword
    });
  }

  /**
   * Cambiar contraseña del usuario actual
   */
  changePassword(currentPassword: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/change-password`, {
      currentPassword,
      newPassword
    });
  }
}