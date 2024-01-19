create table domaines
(
    idDomaine bigint auto_increment primary key,
    nom       varchar(100) not null unique,
    dateC         DATETIME,
    dateU         DATETIME
);