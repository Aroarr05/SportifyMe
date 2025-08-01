// modules/desafios/services/desafios.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/enviroment.port';
import { Desafio } from '../../../shared/models/desafio.model';
import { CrearDesafioDto } from '../dto/crear-desafio.dto';

@Injectable({
  providedIn: 'root'
})
export class DesafiosService {
  private apiUrl = `${environment.apiUrl}/desafios`;

  constructor(private http: HttpClient) {}

  getDesafios(): Observable<Desafio[]> {
    return this.http.get<Desafio[]>(this.apiUrl);
  }

  getDesafioById(id: number): Observable<Desafio> {
    return this.http.get<Desafio>(`${this.apiUrl}/${id}`);
  }

  crearDesafio(desafioData: CrearDesafioDto): Observable<Desafio> {
    return this.http.post<Desafio>(this.apiUrl, desafioData);
  }

  unirseADesafio(desafioId: number): Observable<Desafio> {
    return this.http.post<Desafio>(`${this.apiUrl}/${desafioId}/unirse`, {});
  }

  abandonarDesafio(desafioId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${desafioId}/abandonar`);
  }

  getDesafiosDelUsuario(): Observable<Desafio[]> {
    return this.http.get<Desafio[]>(`${this.apiUrl}/mis-desafios`);
  }
}