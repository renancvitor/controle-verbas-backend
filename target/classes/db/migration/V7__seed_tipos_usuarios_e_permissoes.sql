-- Permissões
INSERT INTO permissoes (id, nome) VALUES
(1, 'GERENCIAR_USUARIOS'),
(2, 'REALIZAR_ORCAMENTOS'),
(3, 'VISUALIZAR_TODOS_ORCAMENTOS'),
(4, 'APROVAR_REPROVAR_ORCAMENTOS');

-- Tipos de usuário
INSERT INTO tipos_usuarios (id, nome) VALUES
(1, 'ADMIN'),
(2, 'GESTOR'),
(3, 'TESOUREIRO'),
(4, 'COMUM');

-- Associação entre tipos e permissões
INSERT INTO tipos_usuarios_permissoes (tipo_usuario_id, permissao_id) VALUES
-- ADMIN (todas as permissões)
(1, 1), (1, 2), (1, 3), (1, 4),

-- GESTOR
(2, 2), (2, 3), (2, 4),

-- TESOUREIRO
(3, 2), (3, 3),

-- COMUM
(4, 2);
