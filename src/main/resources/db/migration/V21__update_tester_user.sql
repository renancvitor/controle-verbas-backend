UPDATE usuarios u
SET 
    senha = '$2a$10$dgt3Og0xuvJDX/5M8hgFfufzFn3SlhewXl4EDXvXMxRxZlCYbyaSK',
    primeiro_acesso = false
FROM pessoas p
WHERE u.pessoa_id = p.id
  AND p.email = 'tester@sistema.com';
