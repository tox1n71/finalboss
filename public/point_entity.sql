create table point_entity
(
    id             bigserial
        primary key,
    executed_at    timestamp(6),
    execution_time bigint,
    r              double precision,
    result         boolean,
    x              double precision,
    y              double precision,
    user_id        bigint
        constraint fkpognvqglbn467dcgrvfsybyex
            references user_entity
);

alter table point_entity
    owner to ivan;

