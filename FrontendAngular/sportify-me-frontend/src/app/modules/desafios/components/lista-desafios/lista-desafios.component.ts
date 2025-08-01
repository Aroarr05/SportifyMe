// Ejemplo para lista-desafios.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio } from '../../../../shared/models/desafio.model';

@Component({
  selector: 'app-lista-desafios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-desafios.component.html',
  styleUrls: ['./lista-desafios.component.scss']
})
export class ListaDesafiosComponent {
  desafios: Desafio[] = [];

  constructor(private desafiosService: DesafiosService) {}

  ngOnInit(): void {
    this.cargarDesafios();
  }

  cargarDesafios() {
    this.desafiosService.getDesafios().subscribe({
      next: (desafios) => this.desafios = desafios,
      error: (err) => console.error('Error al cargar desafíos:', err)
    });
  }
}