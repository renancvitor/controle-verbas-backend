CREATE TABLE orcamentos (
    id BIGSERIAL PRIMARY KEY,
    fornecedor VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    forma_pagamento VARCHAR(255) NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL,
    observacoes_gerais TEXT,

    solicitante_id BIGINT NOT NULL,
    gestor_id BIGINT,
    tesoureiro_id BIGINT,
    status_orcamento_id INT NOT NULL,

    data_criacao DATE NOT NULL DEFAULT CURRENT_DATE,
    data_analise DATE,
    data_liberacao_verba DATE,

    CONSTRAINT fk_solicitante FOREIGN KEY (solicitante_id) REFERENCES usuarios(id),
    CONSTRAINT fk_gestor FOREIGN KEY (gestor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_tesoureiro FOREIGN KEY (tesoureiro_id) REFERENCES usuarios(id),
    CONSTRAINT fk_status_orcamento FOREIGN KEY (status_orcamento_id) REFERENCES status_orcamentos(id)
);
