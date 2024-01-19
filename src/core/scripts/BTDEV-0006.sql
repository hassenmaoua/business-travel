ALTER TABLE users
    ADD COLUMN entrepriseId BIGINT,
    ADD CONSTRAINT fk_entrepriseId
        FOREIGN KEY (entrepriseId)
            REFERENCES Entreprises(idEntreprise)
            ON DELETE SET NULL;