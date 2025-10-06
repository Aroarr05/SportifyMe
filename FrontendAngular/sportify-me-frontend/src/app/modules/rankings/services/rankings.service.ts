// rankings.service.ts - Versión simplificada
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { RankingDesafio } from '../../../shared/models';

@Injectable({
  providedIn: 'root'
})
export class RankingsService {
  private apiUrl = `${environment.apiUrl}/progresos`;

  constructor(private http: HttpClient) { }

  obtenerRankingDesafio(desafioId: number): Observable<RankingDesafio> {
    // Ahora es público, no necesita headers de autenticación
    return this.http.get<RankingDesafio>(`${this.apiUrl}/ranking/${desafioId}`);
  }
}