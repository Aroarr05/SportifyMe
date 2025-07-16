import { Component, OnInit } from '@angular/core';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio } from '../../shared/models/desafio.model';

@Component({
  selector: 'app-lista-desafios',
  templateUrl: './lista-desafios.component.html',
  styleUrls: ['./lista-desafios.component.scss']
})
export class ListaDesafiosComponent implements OnInit {
  desafios: Desafio[] = [];

  constructor(private desafiosService: DesafiosService) {}

  ngOnInit(): void {
    this.cargarDesafios();
  }

  cargarDesafios() {
    this.desafiosService.obtenerDesafios().subscribe({
      next: (desafios) => this.desafios = desafios,
      error: (err) => console.error('Error al cargar desafíos:', err)
    });
  }
}