import { Usuario } from './usuario.model';
import { Progreso } from './progreso.model';
import { Comentario } from './comentario.model';

export interface Desafio {
  id: number;
  nombre: string;
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: string;
  fechaCreacion: Date;
  fechaLimite: Date | string;
  creador: Usuario | number; // Puede ser el objeto completo o solo el ID
  participantes?: Usuario[] | number[]; // Array de usuarios o IDs
  progresos?: Progreso[];
  comentarios?: Comentario[];
}

export enum TipoActividad {
  CARRERA = 'CARRERA',
  CICLISMO = 'CICLISMO',
  NATACION = 'NATACION',
  GIMNASIO = 'GIMNASIO',
  OTRO = 'OTRO'
}