-- Creación de la base de datos
CREATE DATABASE outsourcing_db;
USE outsourcing_db;

-- Tabla Cliente
CREATE TABLE Cliente (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Representante VARCHAR(100) NOT NULL,
    Correo VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20) NOT NULL,
    Direccion VARCHAR(200) NOT NULL,
    Sector ENUM('Tecnologia', 'Salud', 'Educacion', 'Comercio', 'Manufactura') NOT NULL
);

-- Tabla Servicio
CREATE TABLE Servicio (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    PrecioPorHora DECIMAL(10,2) NOT NULL,
    Categoria ENUM('TI', 'Limpieza', 'Seguridad', 'Administracion') NOT NULL
);

-- Tabla Proyecto
CREATE TABLE Proyecto (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cliente INT NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE,
    Estado ENUM('En curso', 'Completado', 'Cancelado') NOT NULL,
    FOREIGN KEY (ID_Cliente) REFERENCES Cliente(ID)
);

-- Tabla Empleado
CREATE TABLE Empleado (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Cargo VARCHAR(100) NOT NULL,
    Salario DECIMAL(10,2) NOT NULL,
    Especialidad ENUM('TI', 'Administracion', 'Limpieza', 'Seguridad') NOT NULL,
    ID_Proyecto INT,
    FOREIGN KEY (ID_Proyecto) REFERENCES Proyecto(ID)
);

-- Tabla Contrato
CREATE TABLE Contrato (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cliente INT NOT NULL,
    ID_Servicio INT NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE,
    CostoTotal DECIMAL(12,2),
    Estado ENUM('Activo', 'En espera', 'Finalizado') NOT NULL,
    FOREIGN KEY (ID_Cliente) REFERENCES Cliente(ID),
    FOREIGN KEY (ID_Servicio) REFERENCES Servicio(ID)
);

-- Tabla Asignacion
CREATE TABLE Asignacion (
    ID_Empleado INT NOT NULL,
    ID_Proyecto INT NOT NULL,
    HorasTrabajadas INT NOT NULL,
    FechaAsignacion DATE NOT NULL,
    PRIMARY KEY (ID_Empleado, ID_Proyecto),
    FOREIGN KEY (ID_Empleado) REFERENCES Empleado(ID),
    FOREIGN KEY (ID_Proyecto) REFERENCES Proyecto(ID)
);