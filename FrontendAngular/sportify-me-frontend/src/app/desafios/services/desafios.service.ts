import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Desafio } from '../../shared/models';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root'
})
export class DesafiosService {
  private apiUrl = `${environment.apiUrl}/desafios`;

  constructor(private http: HttpClient) { }

  // Crear un nuevo desafío
  crearDesafio(desafio: Desafio): Observable<Desafio> {
    return this.http.post<Desafio>(this.apiUrl, desafio);
  }

  // Obtener todos los desafíos
  getDesafios(): Observable<Desafio[]> {
    return this.http.get<Desafio[]>(this.apiUrl);
  }

  // Obtener un desafío por ID
  getDesafioById(id: number): Observable<Desafio> {
    return this.http.get<Desafio>(`${this.apiUrl}/${id}`);
  }

  // Actualizar un desafío
  actualizarDesafio(id: number, desafio: Desafio): Observable<Desafio> {
    return this.http.put<Desafio>(`${this.apiUrl}/${id}`, desafio);
  }

  // Eliminar un desafío
  eliminarDesafio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}