CREATE TABLE Event (
                       idEvent BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       dateDebut DATETIME,
                       dateFin DATETIME,
                       region VARCHAR(255),
                       adresse VARCHAR(255),
                       status VARCHAR(50), -- Vous devez définir le type correct pour Etat (status)
                       idCategory BIGINT,
                       FOREIGN KEY (idCategory) REFERENCES Category(idCategory)
);