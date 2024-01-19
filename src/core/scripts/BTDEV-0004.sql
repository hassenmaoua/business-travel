create table entreprises
(
    idEntreprise  bigint auto_increment primary key,
    raisonSociale varchar(100) not null unique,
    adresse       varchar(60),
    dateC         DATETIME,
    dateU         DATETIME
);
