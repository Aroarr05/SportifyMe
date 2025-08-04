import { Component, OnInit } from '@angular/core';
import { DesafiosService } from '../../services/desafios.service';
import { Desafio } from '../../../../shared/models';

@Component({
  standalone: true,
  selector: 'app-lista-desafios',
  templateUrl: './lista-desafios.component.html',
  styleUrls: ['./lista-desafios.component.scss']
})

export class ListaDesafiosComponent implements OnInit {
  desafios: Desafio[] = [];
  loading = true;
  error: string | null = null;

  constructor(private desafiosService: DesafiosService) { }

  ngOnInit(): void {
    this.cargarDesafios();
  }

  cargarDesafios(): void {
    this.loading = true;
    this.desafiosService.getDesafios().subscribe({
      next: (desafios) => {
        this.desafios = desafios;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los desafíos. Por favor, inténtalo de nuevo más tarde.';
        this.loading = false;
        console.error(err);
      }
    });
  }
}