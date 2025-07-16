import { Usuario } from './usuario.model';
import { Desafio } from './desafio.model';

export interface Comentario {
  id: number;
  texto: string;
  fecha: Date;
  usuario: Usuario | number;
  desafio: Desafio | number;
  respuestaA?: Comentario | number;
}