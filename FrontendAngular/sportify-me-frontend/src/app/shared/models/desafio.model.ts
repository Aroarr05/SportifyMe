import { Usuario } from "./usuario.model";
import { Progreso } from "./progreso.model";
import { Comentario } from "./comentario.model";

export interface Desafio {
  id: number;
  nombre: string;
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: string;
  fechaCreacion: Date | string;
  fechaLimite: Date | string;
  creador: Usuario;
  participantes?: Usuario[];
  progresos?: Progreso[];
  comentarios?: Comentario[];
}

export interface CrearDesafioDto {
  nombre: string;
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: string;
  fechaLimite: string; // Formato ISO
}

export enum TipoActividad {
  CARRERA = 'CARRERA',
  CICLISMO = 'CICLISMO',
  NATACION = 'NATACION',
  GIMNASIO = 'GIMNASIO',
  OTRO = 'OTRO'
}