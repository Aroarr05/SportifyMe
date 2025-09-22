import { Desafio } from "./desafio.model";
import { Progreso } from "./progreso.model";

export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  fechaNacimiento?: Date;
  fechaRegistro: Date;
  avatar?: string;
  peso?: number;
  altura?: number;
  deportesFavoritos?: string[];
  biografia?: string;
  // Campos para estadísticas
  totalDesafiosCompletados?: number;
  mejorPosicionRanking?: number;
  tiempoTotalEntrenamiento?: number; // en minutos
}
export enum Rol {
  ADMIN = 'ADMIN',
  USER = 'USER',
  PREMIUM = 'PREMIUM'
}