-- Inserir Cargo se não existir
INSERT INTO cargos (nome)
SELECT 'Administrador'
WHERE NOT EXISTS (
  SELECT 1 FROM cargos WHERE nome = 'Administrador'
);

-- Inserir Departamento se não existir
INSERT INTO departamentos (nome)
SELECT 'Engenharia de Software'
WHERE NOT EXISTS (
  SELECT 1 FROM departamentos WHERE nome = 'Engenharia de Software'
);

-- Inserir Pessoa (assume que cargo e departamento foram inseridos acima)
INSERT INTO pessoas (nome, cpf, email, departamento_id, cargo_id, data_cadastro)
SELECT
  'Administrador do Sistema',
  '516.337.810-46',
  'admin@sistema.com',
  d.id,
  c.id,
  NOW()
FROM departamentos d, cargos c
WHERE d.nome = 'Engenharia de Software'
  AND c.nome = 'Administrador'
  AND NOT EXISTS (
    SELECT 1 FROM pessoas WHERE email = 'admin@sistema.com'
);

-- Inserir Usuário ADMIN
-- senha: 123456 (criptografada com BCrypt)
INSERT INTO usuarios (senha, pessoa_id, tipo_usuario_id)
SELECT
  '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.',
  p.id,
  tu.id
FROM pessoas p, tipos_usuarios tu
WHERE p.email = 'admin@sistema.com'
  AND tu.nome = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM usuarios WHERE pessoa_id = p.id
);
