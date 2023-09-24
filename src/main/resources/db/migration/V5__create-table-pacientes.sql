CREATE TABLE pacientes(
     id BIGINT NOT NULL AUTO_INCREMENT,
     nombre VARCHAR(100) NOT NULL,
     email VARCHAR(100) NOT NULL UNIQUE,
     dni VARCHAR(14) NOT NULL UNIQUE,
     telefono VARCHAR(20) NOT NULL,
     calle VARCHAR(100) NOT NULL,
     numero VARCHAR(5) NOT NULL,
     distrito VARCHAR(60) NOT NULL,
     ciudad VARCHAR(60) NOT NULL,
     complemento VARCHAR(100),
     activo TINYINT,
     PRIMARY KEY(id)
);
