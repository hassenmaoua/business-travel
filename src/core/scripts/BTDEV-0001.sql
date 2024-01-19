create table users
(
    idUser bigint auto_increment
        primary key,
    email  varchar(191)                                             not null,
    pswd   varchar(255)                                             not null,
    role   enum ('ADMIN', 'COORDINATOR', 'EMPLOYER') default 'EMPLOYER' not null,
    constraint email
        unique (email),
    constraint chk_password_length
        check (length(`pswd`) >= 8)
<<<<<<< HEAD
);