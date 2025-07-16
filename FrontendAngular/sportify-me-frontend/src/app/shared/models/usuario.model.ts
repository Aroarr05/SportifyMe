import { Desafio } from "./desafio.model";
import { Progreso } from "./progreso.model";

export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  password?: string; // Solo para formularios, no debe venir del backend
  avatar?: string;
  fechaRegistro?: Date;
  roles?: Rol[];
  desafiosCreados?: Desafio[];
  desafiosParticipantes?: Desafio[];
  progresos?: Progreso[];
}

export enum Rol {
  ADMIN = 'ADMIN',
  USER = 'USER',
  PREMIUM = 'PREMIUM'
}