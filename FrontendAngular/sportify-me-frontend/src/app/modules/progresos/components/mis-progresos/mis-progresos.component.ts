import { Component, OnInit } from '@angular/core';
import { ProgresosService } from '../../services/progresos.service';

@Component({
  selector: 'app-mis-progresos',
  templateUrl: './mis-progresos.component.html',
  styleUrls: ['./mis-progresos.component.scss']
})
export class MisProgresosComponent implements OnInit {
  resumen: any;
  progresosPorDesafio: any[] = [];

  constructor(private progresosService: ProgresosService) {}

  ngOnInit(): void {
    this.cargarResumen();
    this.cargarProgresos();
  }

  cargarResumen() {
    this.progresosService.obtenerResumenProgresos().subscribe({
      next: (resumen) => this.resumen = resumen,
      error: (err) => console.error('Error al cargar resumen:', err)
    });
  }

  cargarProgresos() {
    this.progresosService.obtenerProgresosPorDesafio().subscribe({
      next: (progresos) => this.progresosPorDesafio = progresos,
      error: (err) => console.error('Error al cargar progresos:', err)
    });
  }
}