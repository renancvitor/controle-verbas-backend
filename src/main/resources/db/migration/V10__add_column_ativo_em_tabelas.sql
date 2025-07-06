-- Adiciona coluna ativo nas 4 tabelas
-- Tabela pessoas
ALTER TABLE pessoas ADD COLUMN ativo BOOLEAN;
UPDATE pessoas SET ativo = true;
ALTER TABLE pessoas ALTER COLUMN ativo SET DEFAULT true;

-- Tabela usuarios
ALTER TABLE usuarios ADD COLUMN ativo BOOLEAN;
UPDATE usuarios SET ativo = true;
ALTER TABLE usuarios ALTER COLUMN ativo SET DEFAULT true;

-- Tabela departamentos
ALTER TABLE departamentos ADD COLUMN ativo BOOLEAN;
UPDATE departamentos SET ativo = true;
ALTER TABLE departamentos ALTER COLUMN ativo SET DEFAULT true;

-- Tabela cargos
ALTER TABLE cargos ADD COLUMN ativo BOOLEAN;
UPDATE cargos SET ativo = true;
ALTER TABLE cargos ALTER COLUMN ativo SET DEFAULT true;
