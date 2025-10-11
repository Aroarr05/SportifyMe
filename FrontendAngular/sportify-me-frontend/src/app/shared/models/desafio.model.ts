export interface Desafio {
  id: number;
  titulo: string;                    
  descripcion: string;
  tipoActividad: string;  // Debe coincidir con los valores de tu BD: 'correr', 'ciclismo', etc.           
  objetivo: number;                  
  unidadObjetivo: string;            
  fechaInicio: string;               
  fechaFin: string;                 
  creadorId: number;  // CAMBIO: De objeto a number (para coincidir con creador_id BIGINT)
  esPublico: boolean;                
  dificultad: string;  // Debe coincidir con: 'principiante', 'intermedio', 'avanzado'            
  maxParticipantes: number;          
  imagenUrl?: string;                
}

export interface CrearDesafioDto {
  titulo: string;  // CAMBIO: de 'nombre' a 'titulo' (para coincidir con BD)
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: number;  // CAMBIO: de string a number
  unidadObjetivo: string;  // AGREGAR: falta en tu interfaz
  fechaInicio: string;  // CAMBIO: de 'fechaLimite' a estructuras separadas
  fechaFin: string;  // AGREGAR: falta en tu interfaz
  creadorId: number;  // AGREGAR: falta en tu interfaz
  esPublico: boolean;  // AGREGAR: falta en tu interfaz
  dificultad: string;  // AGREGAR: falta en tu interfaz
  maxParticipantes: number;  // AGREGAR: falta en tu interfaz
  imagenUrl?: string;  // AGREGAR: falta en tu interfaz
}

export enum TipoActividad {
  CORRER = 'correr',  // CAMBIO: para coincidir con BD
  CICLISMO = 'ciclismo',  // CAMBIO: para coincidir con BD
  NADAR = 'nadar',  // CAMBIO: de NATACION a NADAR
  GIMNASIO = 'gimnasio',  // CAMBIO: para coincidir con BD
  SENDERISMO = 'senderismo',  // AGREGAR: falta en tu enum
  YOGA = 'yoga',  // AGREGAR: falta en tu enum
  OTRO = 'otro'  // CAMBIO: para coincidir con BD
}