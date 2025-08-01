CREATE TABLE tipos_usuarios_permissoes (
    tipo_usuario_id INT NOT NULL,
    permissao_id INT NOT NULL,
    PRIMARY KEY (tipo_usuario_id, permissao_id),

    CONSTRAINT fk_tipo_usuario
        FOREIGN KEY (tipo_usuario_id)
        REFERENCES tipos_usuarios(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_permissao
        FOREIGN KEY (permissao_id)
        REFERENCES permissoes(id)
        ON DELETE CASCADE
);
