import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../enviroments/environment';
import { Ranking,RankingDesafio } from '../../../../shared/models';

@Injectable({
  providedIn: 'root'
})

export class RankingGlobalComponent {
  private apiUrl = `${environment.apiUrl}/rankings`;

  constructor(private http: HttpClient) { }

  obtenerRankingGlobal(tipo: string = 'todos'): Observable<Ranking[]> {
    let params = new HttpParams();
    if (tipo !== 'todos') {
      params = params.set('tipo', tipo);
    }
    
    return this.http.get<Ranking[]>(`${this.apiUrl}/global`, { params });
  }

  obtenerRankingDesafio(desafioId: number): Observable<RankingDesafio> {
    return this.http.get<RankingDesafio>(`${this.apiUrl}/desafio/${desafioId}`);
  }
}