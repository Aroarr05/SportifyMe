
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/enviroment.port';

@Injectable({
  providedIn: 'root'
})
export class ProgresosService {
  private apiUrl = `${environment.apiUrl}/progresos`;

  constructor(private http: HttpClient) { }

  obtenerResumenProgresos(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/resumen`);
  }

  obtenerProgresosPorDesafio(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/por-desafio`);
  }

  registrarProgreso(desafioId: number, progreso: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${desafioId}`, progreso);
  }

  obtenerProgresosDesafio(desafioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/desafio/${desafioId}`);
  }
}