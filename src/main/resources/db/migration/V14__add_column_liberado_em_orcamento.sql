ALTER TABLE orcamentos ADD COLUMN verba_liberada BOOLEAN;
UPDATE orcamentos SET verba_liberada = false;
ALTER TABLE orcamentos ALTER COLUMN verba_liberada SET DEFAULT false;
ALTER TABLE orcamentos ALTER COLUMN verba_liberada SET NOT NULL;
