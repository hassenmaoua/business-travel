create table voyages( idVoyage bigint auto_increment primary key,
                     nom varchar(100)                not null,
                     depart varchar(100)             not null,
                     destination varchar(100)        not null,
                     dateDepart DATE         not null,
                     class varchar(100)    not null,
                     avion varchar(100) not null
);