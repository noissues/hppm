

drop table if exists notation;
create table notation
(
    id bigint auto_increment,
    notation_name varchar(256),
    author varchar(64),
    notation_category_code varchar(8),
    play_category_code varchar(8),
    create_date datetime,
    update_date datetime,
    song_name varchar(256),
    artist varchar(64),
    constraint NOTATION_PK
        primary key (id)
);

drop table if exists notation_file;
create table notation_file
(
    id bigint auto_increment,
    notation_id bigint,
    seq int,
    path varchar(256),
    constraint NOTATION_FILE_PK
        primary key (id)
);
