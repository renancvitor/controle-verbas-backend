-- 1. Adiciona a nova coluna permitindo valores nulos inicialmente
ALTER TABLE usuarios ADD COLUMN primeiro_acesso BOOLEAN;

-- 2. Define 'true' para todos os usuários já existentes
UPDATE usuarios SET primeiro_acesso = true;

-- 3. Define o valor padrão como 'true' para novos usuários
ALTER TABLE usuarios ALTER COLUMN primeiro_acesso SET DEFAULT true;

-- 4. (Opcional, mas recomendado) Garante que o campo nunca seja nulo
ALTER TABLE usuarios ALTER COLUMN primeiro_acesso SET NOT NULL;
