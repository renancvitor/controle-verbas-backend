CREATE TABLE pessoas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    departamento_id BIGINT,
    cargo_id BIGINT,
    data_cadastro TIMESTAMP,

    CONSTRAINT fk_departamento
        FOREIGN KEY (departamento_id)
        REFERENCES departamentos(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_cargo
        FOREIGN KEY (cargo_id)
        REFERENCES cargos(id)
        ON DELETE SET NULL
);
