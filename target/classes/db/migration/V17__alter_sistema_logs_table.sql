ALTER TABLE sistema_logs
    ADD COLUMN entidade VARCHAR(100),
    ADD COLUMN payload TEXT;
