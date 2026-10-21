CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rol_id BIGINT NOT NULL,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    usuario VARCHAR(40) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(25),
    cargo VARCHAR(80),
    avatar_color VARCHAR(9) DEFAULT '#4f46e5',
    estado INT DEFAULT 1,
    ultimo_acceso TIMESTAMP,
    cliente_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS company_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    ruc VARCHAR(20) NOT NULL,
    razon_social VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(30),
    email VARCHAR(100),
    moneda_simbolo VARCHAR(10) DEFAULT 'S/',
    igv_porcentaje DECIMAL(5,2) DEFAULT 18.00,
    tarifa_hora DECIMAL(10,2) DEFAULT 45.00,
    logo_url VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_backups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tamano_legible VARCHAR(50) NOT NULL,
    tamano_bytes BIGINT NOT NULL,
    tablas_respaldadas INT NOT NULL,
    filas_respaldadas INT NOT NULL,
    checksum_sha256 VARCHAR(64) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'COMPLETADO',
    creado_por VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);
