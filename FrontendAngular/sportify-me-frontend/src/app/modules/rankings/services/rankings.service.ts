import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Ranking } from '../../../shared/models/ranking.model';

@Injectable({
  providedIn: 'root'
})
export class RankingsService {
  // ✅ CORREGIDO: Usa el endpoint REAL que existe en tu backend
  private apiUrl = `${environment.apiUrl}/progresos`;

  constructor(private http: HttpClient) { }

  // ✅ CORREGIDO: Este es el endpoint que SÍ existe en tu Spring Boot
  obtenerRankingDesafio(desafioId: number): Observable<Ranking[]> {
    return this.http.get<Ranking[]>(`${this.apiUrl}/ranking/${desafioId}`);
  }

 
}