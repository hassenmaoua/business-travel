create table categories
(
    idCategorie bigint auto_increment
        primary key,
    nom         varchar(100) not null,
    dateC       datetime     null,
    dateU       datetime     null
);

create definer = root@localhost trigger categories_insert
    before insert
    on categories
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger categories_update
    before update
    on categories
    for each row
    SET NEW.dateU = NOW();

create table companions
(
    idCompanion              bigint auto_increment
        primary key,
    evenementId              bigint       not null,
    employeeId               bigint       not null,
    voyageId                 bigint       null,
    nom                      varchar(60)  not null,
    domaineActivite          varchar(255) null,
    age                      int          null,
    besoinsSpeciaux          varchar(255) null,
    contactUrgenceNom        varchar(255) null,
    contactUrgenceTel        varchar(20)  null,
    restrictionsAlimentaires varchar(255) null,
    numPassport              varchar(20)  not null,
    notesSupplementaires     varchar(255) null,
    dateC                    date         null,
    dateU                    date         null,
    constraint unique_combination
        unique (evenementId, employeeId)
);

create index employeeId
    on companions (employeeId);

create index voyageId
    on companions (voyageId);

create definer = root@localhost trigger companions_insert
    before insert
    on companions
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger companions_update
    before update
    on companions
    for each row
    SET NEW.dateU = NOW();

create table entreprises
(
    idEntreprise  bigint auto_increment
        primary key,
    nomEntreprise varchar(100) null,
    raisonSociale varchar(100) not null,
    adresse       varchar(60)  null,
    domaine       varchar(60)  null,
    email         varchar(200) null,
    telephone     varchar(20)  null,
    nbEmployee    varchar(100) null,
    dateC         datetime     null,
    dateU         datetime     null,
    constraint raisonSociale
        unique (raisonSociale)
);

create definer = root@localhost trigger entreprises_insert
    before insert
    on entreprises
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger entreprises_update
    before update
    on entreprises
    for each row
    SET NEW.dateU = NOW();

create table evenements
(
    idEvenement bigint auto_increment
        primary key,
    titre       varchar(100)                    not null,
    description text                            null,
    dateDebut   datetime                        not null,
    dateFin     datetime                        not null,
    region      varchar(60)                     null,
    adresse     varchar(200)                    null,
    etat        varchar(100) default 'NOUVEAUX' not null,
    idCategory  bigint                          null,
    dateC       datetime                        null,
    dateU       datetime                        null
);

create index idCategory
    on evenements (idCategory);

create definer = root@localhost trigger evenementss_insert
    before insert
    on evenements
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger evenementss_update
    before update
    on evenements
    for each row
    SET NEW.dateU = NOW();

create table users
(
    idUser        bigint auto_increment
        primary key,
    email         varchar(191)                                                 not null,
    pswd          varchar(255)                                                 not null,
    role          enum ('ADMIN', 'COORDINATOR', 'EMPLOYER') default 'EMPLOYER' not null,
    nom           varchar(30)                                                  not null,
    prenom        varchar(30)                                                  not null,
    adresse       varchar(200)                                                 null,
    dateNaissance date                                                         null,
    telephone     varchar(20)                                                  null,
    dateC         datetime                                                     null,
    dateU         datetime                                                     null,
    entrepriseId  bigint                                                       null,
    constraint email
        unique (email),
    constraint chk_password_length
        check (length(`pswd`) >= 8),
    check (length(`nom`) >= 3),
    check (length(`prenom`) >= 3)
);

create index fk_entrepriseId
    on users (entrepriseId);

create definer = root@localhost trigger users_insert
    before insert
    on users
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger users_update
    before update
    on users
    for each row
    SET NEW.dateU = NOW();

create table voyages
(
    idVoyage    bigint auto_increment
        primary key,
    nom         varchar(100) not null,
    depart      varchar(100) not null,
    destination varchar(100) not null,
    dateDepart  date         null,
    classe      varchar(100) not null,
    avion       varchar(100) not null,
    dateC       datetime     null,
    dateU       datetime     null
);

create definer = root@localhost trigger voyages_insert
    before insert
    on voyages
    for each row
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

create definer = root@localhost trigger voyages_update
    before update
    on voyages
    for each row
    SET NEW.dateU = NOW();


