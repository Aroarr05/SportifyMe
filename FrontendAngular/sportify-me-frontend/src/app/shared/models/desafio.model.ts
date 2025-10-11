export interface Desafio {
  id: number;
  titulo: string;                    
  descripcion: string;
  tipoActividad: string;             
  objetivo: number;                  
  unidadObjetivo: string;            
  fechaInicio: string;               
  fechaFin: string;                 
  creador: {
    id: number;
    nombre: string;
    email: string;
  };
  esPublico: boolean;                
  dificultad: string;                
  maxParticipantes: number;          
  imagenUrl?: string;                
}

export interface CrearDesafioDto {
  nombre: string;
  descripcion: string;
  tipoActividad: TipoActividad;
  objetivo: string;
  fechaLimite: string; 
}

export enum TipoActividad {
  CARRERA = 'CARRERA',
  CICLISMO = 'CICLISMO',
  NATACION = 'NATACION',
  GIMNASIO = 'GIMNASIO',
  OTRO = 'OTRO'
}