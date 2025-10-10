import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class DesafiosService {
  private apiUrl = 'http://localhost:8080/api/desafios';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  obtenerParticipantes(desafioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${desafioId}/participantes`);
  }

  obtenerDesafioPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // ✅ CORREGIDO: Cambiar de 'participar' a 'unirse'
  unirseADesafio(desafioId: number): Observable<any> {
    // En Spring el endpoint es POST /api/desafios/{desafioId}/unirse
    // Y el usuario se obtiene del token, no del body
    return this.http.post(`${this.apiUrl}/${desafioId}/unirse`, {});
  }

  obtenerDesafiosActivos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activos`);
  }
}