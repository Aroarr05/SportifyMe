-- Tabla de Usuarios (completa)
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    biografia TEXT,
    ubicacion VARCHAR(100),
    fecha_nacimiento DATE,
    genero ENUM('MASCULINO', 'FEMENINO', 'OTRO', 'NO_ESPECIFICADO'),
    peso DECIMAL(5,2),
    altura INT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_login DATETIME
);

-- Tabla de Desafíos (completa)
CREATE TABLE desafios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo_actividad ENUM('correr', 'ciclismo', 'natación', 'gimnasio', 'otros') NOT NULL,
    objetivo DECIMAL(10,2),
    unidad_objetivo VARCHAR(20),
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    creador_id INT NOT NULL,
    es_publico BOOLEAN DEFAULT TRUE,
    imagen_url VARCHAR(255),
    dificultad ENUM('PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO'),
    max_participantes INT,
    FOREIGN KEY (creador_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de Participación (sin cambios)
CREATE TABLE participaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    fecha_union DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE,
    UNIQUE KEY (usuario_id, desafio_id)
);

-- Tabla de Progresos (mejorada)
CREATE TABLE progresos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    valor_actual DECIMAL(10,2) NOT NULL,
    unidad VARCHAR(20) NOT NULL, -- Nueva columna
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    comentario TEXT,
    dispositivo VARCHAR(50), -- Ej: 'APP_IOS', 'APP_ANDROID', 'WEARABLE'
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE
);

-- Tabla de Comentarios (mejorada)
CREATE TABLE comentarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    editado BOOLEAN DEFAULT FALSE,
    fecha_edicion DATETIME,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE
);

-- Tabla de Logros (nueva)
CREATE TABLE logros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    icono_url VARCHAR(255),
    criterio VARCHAR(50) NOT NULL,
    valor_requerido INT,
    categoria ENUM('PROGRESO', 'SOCIAL', 'DEDICACION')
);

-- Tabla de Logros de Usuario (nueva)
CREATE TABLE usuario_logros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    logro_id INT NOT NULL,
    fecha_obtencion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (logro_id) REFERENCES logros(id) ON DELETE CASCADE,
    UNIQUE KEY (usuario_id, logro_id)  -- Evitar duplicados
);

-- Tabla de Notificaciones (nueva)
CREATE TABLE notificaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    tipo ENUM('LOGRO', 'COMENTARIO', 'PROGRESO', 'DESAFIO', 'SISTEMA') NOT NULL,
    mensaje TEXT NOT NULL,
    enlace VARCHAR(255),
    leida BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE VIEW ranking_desafios AS
SELECT 
    p.desafio_id,
    p.usuario_id,
    u.nombre,
    u.avatar_url,
    CASE 
        WHEN d.tipo_actividad IN ('correr', 'ciclismo', 'natación') THEN MIN(p.valor_actual) -- Para actividades de tiempo
        ELSE MAX(p.valor_actual) -- Para actividades de distancia/repeticiones
    END AS progreso_relevante,
    RANK() OVER (
        PARTITION BY p.desafio_id 
        ORDER BY CASE 
            WHEN d.tipo_actividad IN ('correr', 'ciclismo', 'natación') THEN p.valor_actual ASC -- Menor es mejor
            ELSE p.valor_actual DESC -- Mayor es mejor
        END
    ) AS posicion,
    d.titulo AS desafio_titulo,
    d.tipo_actividad
FROM 
    progresos p
JOIN 
    usuarios u ON p.usuario_id = u.id
JOIN 
    desafios d ON p.desafio_id = d.id
GROUP BY 
    p.desafio_id, p.usuario_id, u.nombre, u.avatar_url, d.titulo, d.tipo_actividad;