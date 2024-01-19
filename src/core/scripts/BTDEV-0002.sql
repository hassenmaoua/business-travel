ALTER TABLE users
    ADD COLUMN nom           VARCHAR(30) NOT NULL CHECK (LENGTH(nom) >= 3),
    ADD COLUMN prenom        VARCHAR(30)  NOT NULL CHECK (LENGTH(prenom) >= 3),
    ADD COLUMN adresse       VARCHAR(200),
    ADD COLUMN dateNaissance DATE,
    ADD COLUMN telephone     VARCHAR(20),
    ADD COLUMN dateC         DATETIME,
    ADD COLUMN dateU         DATETIME;