export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  contraseña?: string;              // ✅ Cambiar a "contraseña" (con ñ)
  fechaNacimiento?: string;         // ✅ O mantener Date si usas transformación
  fechaRegistro: string;            // ✅ O Date
  avatarUrl?: string;               // ✅ Cambiar a "avatarUrl" 
  peso?: number;
  altura?: number;
  biografia?: string;
  ubicacion?: string;
  genero?: 'masculino' | 'femenino' | 'otro' | 'no_especificado'; // ✅ Valores exactos de BD
  ultimoLogin?: string;             // ✅ O Date
  rol?: 'usuario' | 'admin' | 'moderador'; // ✅ Solo estos valores, sin PREMIUM
  
  // Campos calculados (opcionales)
  deportesFavoritos?: string[];
  totalDesafiosCompletados?: number;
  mejorPosicionRanking?: number;
  tiempoTotalEntrenamiento?: number;
}

// Si quieres usar enum, debe coincidir exactamente:
export enum Rol {
  USUARIO = 'usuario',
  ADMIN = 'admin', 
  MODERADOR = 'moderador'
  // ❌ ELIMINAR: PREMIUM no existe en tu BD
}