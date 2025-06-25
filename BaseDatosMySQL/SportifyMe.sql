-- Tabla de Usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,  -- Almacenar hash, no texto plano
    avatar_url VARCHAR(255),            -- Opcional: para foto de perfil
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Desafíos
CREATE TABLE desafios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo_actividad ENUM('correr', 'ciclismo', 'natación', 'gimnasio') NOT NULL,
    objetivo DECIMAL(10,2),             -- Ej: 5 km, 30 minutos
    unidad_objetivo VARCHAR(20),        -- Ej: "km", "minutos"
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    creador_id INT NOT NULL,
    es_publico BOOLEAN DEFAULT TRUE,    -- Desafíos públicos o privados
    FOREIGN KEY (creador_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de Participación en Desafíos (relación muchos-a-muchos)
CREATE TABLE participaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    fecha_union DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE,
    UNIQUE KEY (usuario_id, desafio_id)  -- Evitar duplicados
);

-- Tabla de Progresos (registros de actividad)
CREATE TABLE progresos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    valor_actual DECIMAL(10,2) NOT NULL, -- Ej: 4.5 km, 25 minutos
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    comentario TEXT,                     -- Opcional: notas del usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE
);

-- Tabla de Comentarios (interacción entre usuarios)
CREATE TABLE comentarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (desafio_id) REFERENCES desafios(id) ON DELETE CASCADE
);

-- Tabla de Rankings (cálculo dinámico o almacenado)
CREATE VIEW ranking_desafios AS
    SELECT 
        p.desafio_id,
        p.usuario_id,
        u.nombre,
        MAX(p.valor_actual) AS progreso_maximo,
        RANK() OVER (PARTITION BY p.desafio_id ORDER BY MAX(p.valor_actual) DESC) AS posicion
    FROM 
        progresos p
    JOIN 
        usuarios u ON p.usuario_id = u.id
    GROUP BY 
        p.desafio_id, p.usuario_id, u.nombre;