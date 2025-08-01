// modules/desafios/dto/crear-desafio.dto.ts
import { TipoActividad } from "../../../shared/models";

export interface CrearDesafioDto {
  nombre: string;
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: string;
  fechaLimite: string; // Formato ISO 8601
}