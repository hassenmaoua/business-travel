ALTER TABLE entreprises
    ADD COLUMN domaineId BIGINT NOT NULL,
    ADD CONSTRAINT fk_domaineId
        FOREIGN KEY (domaineId)
            REFERENCES Domaines(idDomaine);