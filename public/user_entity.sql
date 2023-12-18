create table user_entity
(
    id       bigserial
        primary key,
    password varchar(255),
    username varchar(255),
    role     varchar(255)
);

alter table user_entity
    owner to ivan;

