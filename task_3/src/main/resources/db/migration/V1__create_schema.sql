create table nodes(
    id bigint primary key,
    username varchar(128) not null,
    latitude double precision not null,
    longitude double precision not null
);

create table tags(
    node_id bigint not null references nodes on delete cascade on update cascade,
    key varchar(128) not null,
    value varchar(256) not null,
    primary key (node_id, key)
);