// rankings.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AuthService } from '../../../auth/services/auth.service';
import { RankingDesafio,Ranking } from '../../../shared/models';
@Injectable({
  providedIn: 'root'
})
export class RankingsService {
  private apiUrl = `${environment.apiUrl}/progresos`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  obtenerRankingDesafio(desafioId: number): Observable<RankingDesafio> {
    const token = this.authService.getToken();
    
    if (!token) {
      throw new Error('No hay token de autenticación disponible');
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.get<RankingDesafio>(`${this.apiUrl}/ranking/${desafioId}`, { headers });
  }
}