INSERT INTO status_orcamentos (id, nome) VALUES
    (1, 'ENVIADO'),
    (2, 'APROVADO'),
    (3, 'REPROVADO')
ON CONFLICT (id) DO NOTHING;
