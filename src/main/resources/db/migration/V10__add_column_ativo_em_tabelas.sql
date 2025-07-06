-- Adiciona coluna ativo nas 4 tabelas
-- Tabela pessoa
ALTER TABLE pessoa ADD COLUMN ativo BOOLEAN;
UPDATE pessoa SET ativo = true;
ALTER TABLE pessoa ALTER COLUMN ativo SET DEFAULT true;

-- Tabela usuario
ALTER TABLE usuario ADD COLUMN ativo BOOLEAN;
UPDATE usuario SET ativo = true;
ALTER TABLE usuario ALTER COLUMN ativo SET DEFAULT true;

-- Tabela departamento
ALTER TABLE departamento ADD COLUMN ativo BOOLEAN;
UPDATE departamento SET ativo = true;
ALTER TABLE departamento ALTER COLUMN ativo SET DEFAULT true;

-- Tabela cargo
ALTER TABLE cargo ADD COLUMN ativo BOOLEAN;
UPDATE cargo SET ativo = true;
ALTER TABLE cargo ALTER COLUMN ativo SET DEFAULT true;
