import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { DesafiosService } from '../../services/desafios.service';
import { AuthService } from '../../../../auth/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/components/loading-spinner/loading-spinner.component';
import { ErrorAlertComponent } from '../../../../shared/components/error-alert/error-alert.component';

@Component({
  standalone: true,
  selector: 'app-detalle-desafio',
  templateUrl: './detalle-desafio.component.html',
  styleUrls: ['./detalle-desafio.component.scss'],
  imports: [
    CommonModule,
    RouterModule,
    LoadingSpinnerComponent,
    ErrorAlertComponent
  ]
})
export class DetalleDesafioComponent implements OnInit, OnDestroy {
  desafio: any = null;
  participantes: any[] = [];
  loading = false;
  unirseLoading = false;
  error: string | null = null;
  usuarioActualId: number | null = null;
  desafioId!: number;
  
  private subscriptions: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private desafiosService: DesafiosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.obtenerIdDesafio();
    this.obtenerUsuarioActual();
  }

  private obtenerIdDesafio(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.desafioId = +id;
      this.cargarDesafio();
      this.cargarParticipantes();
    } else {
      this.error = 'ID de desafío no válido';
    }
  }

  cargarDesafio(): void {
    this.loading = true;
    this.error = null;
    
    const sub = this.desafiosService.obtenerDesafioPorId(this.desafioId).subscribe({
      next: (desafio: any) => {
        this.desafio = desafio;
        this.loading = false;
        console.log('✅ Desafío cargado:', desafio);
      },
      error: (err: any) => {
        this.error = 'Error al cargar el desafío. Intenta nuevamente.';
        this.loading = false;
        console.error('❌ Error cargando desafío:', err);
      }
    });
    
    this.subscriptions.add(sub);
  }

  cargarParticipantes(): void {
    const sub = this.desafiosService.obtenerParticipantes(this.desafioId).subscribe({
      next: (participantes: any[]) => {
        this.participantes = participantes;
        console.log('✅ Participantes cargados:', participantes);
      },
      error: (err: any) => {
        console.error('❌ Error cargando participantes:', err);
        // En caso de error, mostrar lista vacía
        this.participantes = [];
      }
    });
    
    this.subscriptions.add(sub);
  }

  obtenerUsuarioActual(): void {
    const usuario = this.authService.getCurrentUser();
    this.usuarioActualId = usuario?.id || null;
    console.log('👤 Usuario actual ID:', this.usuarioActualId);
  }

  getNombreCreador(): string {
    if (!this.desafio) return 'Usuario';
    
    // Si el backend devuelve el objeto creador completo
    if (this.desafio.creador && this.desafio.creador.nombre) {
      return this.desafio.creador.nombre;
    }
    
    // Si solo viene el ID del creador
    if (this.desafio.creador_id) {
      return `Usuario ${this.desafio.creador_id}`;
    }
    
    return 'Usuario';
  }

  esDesafioExpirado(): boolean {
    if (!this.desafio?.fecha_fin && !this.desafio?.fechaFin) return false;
    
    const fechaFin = this.desafio.fecha_fin || this.desafio.fechaFin;
    return new Date(fechaFin) < new Date();
  }

  esDesafioCompleto(): boolean {
    if (!this.desafio?.max_participantes && !this.desafio?.maxParticipantes) return false;
    
    const maxParticipantes = this.desafio.max_participantes || this.desafio.maxParticipantes;
    return this.participantes.length >= maxParticipantes;
  }

  yaEsParticipante(): boolean {
    if (!this.usuarioActualId || !this.participantes.length) return false;
    return this.participantes.some((p: any) => p.id === this.usuarioActualId);
  }

  getTotalParticipantes(): number {
    return this.participantes.length;
  }

  getClaseDificultad(): string {
    const dificultad = (this.desafio?.dificultad || '').toLowerCase();
    switch(dificultad) {
      case 'principiante': return 'bg-green-100 text-green-800 border border-green-200';
      case 'intermedio': return 'bg-yellow-100 text-yellow-800 border border-yellow-200';
      case 'avanzado': return 'bg-red-100 text-red-800 border border-red-200';
      default: return 'bg-gray-100 text-gray-800 border border-gray-200';
    }
  }

  // ✅ MÉTODO AGREGADO: getClaseBotonUnirse()
  getClaseBotonUnirse(): string {
    if (this.yaEsParticipante()) {
      return 'bg-gray-400 text-white cursor-not-allowed';
    }
    if (this.esDesafioExpirado() || this.esDesafioCompleto()) {
      return 'bg-red-400 text-white cursor-not-allowed';
    }
    if (this.unirseLoading) {
      return 'bg-blue-400 text-white cursor-wait';
    }
    return 'bg-green-500 hover:bg-green-600 text-white cursor-pointer';
  }

  getTextoBoton(): string {
    if (this.yaEsParticipante()) return '🎯 Ya participas';
    if (this.esDesafioExpirado()) return '⏰ Desafío expirado';
    if (this.esDesafioCompleto()) return '🚫 Cupo completo';
    if (this.unirseLoading) return '⏳ Uniéndose...';
    return '✅ Unirse al desafío';
  }

  getIconoTipoActividad(): string {
    const tipo = (this.desafio?.tipo_actividad || this.desafio?.tipoActividad || '').toLowerCase();
    switch(tipo) {
      case 'correr': return '🏃‍♂️';
      case 'ciclismo': return '🚴‍♂️';
      case 'nadar': return '🏊‍♂️';
      case 'gimnasio': return '💪';
      case 'yoga': return '🧘';
      case 'senderismo': return '🥾';
      default: return '🎯';
    }
  }

  unirseADesafio(): void {
    if (this.yaEsParticipante() || this.esDesafioExpirado() || this.esDesafioCompleto()) {
      return;
    }

    this.unirseLoading = true;
    this.error = null;
    
    const sub = this.desafiosService.unirseADesafio(this.desafioId).subscribe({
      next: (response: any) => {
        this.unirseLoading = false;
        console.log('✅ Unido al desafío:', response);
        
        // Recargar participantes para actualizar la lista
        this.cargarParticipantes();
        
        // Mostrar mensaje de éxito
        this.error = null;
      },
      error: (err: any) => {
        this.unirseLoading = false;
        const mensajeError = err.error?.message || err.error?.error || 'Error al unirse al desafío';
        this.error = `❌ ${mensajeError}`;
        console.error('❌ Error uniéndose al desafío:', err);
      }
    });
    
    this.subscriptions.add(sub);
  }

  recargarDatos(): void {
    this.error = null;
    this.cargarDesafio();
    this.cargarParticipantes();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}