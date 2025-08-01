CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    senha VARCHAR(100) NOT NULL,

    pessoa_id BIGINT NOT NULL UNIQUE,
    tipo_usuario_id INT NOT NULL,

    CONSTRAINT fk_usuario_pessoa
        FOREIGN KEY (pessoa_id)
        REFERENCES pessoas(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_tipo
        FOREIGN KEY (tipo_usuario_id)
        REFERENCES tipos_usuarios(id)
        ON DELETE CASCADE
);
