drop table if exists tags;
drop table if exists nodes;

create table nodes(
    id bigint primary key,
    username varchar(128) not null,
    latitude double precision not null,
    longitude double precision not null
);

create table tags(
    node_id bigint not null references nodes,
    key varchar(128) not null,
    value varchar(128) not null,
    primary key (node_id, key)
);
