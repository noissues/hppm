
-- h2 数据库不要用 drop table 建表!!，不然每次启动应用都会被 drop 掉清空数据

create table if not exists notation  
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

create table if not exists notation_file
(
    id bigint auto_increment,
    notation_id bigint,
    seq int,
    path varchar(256),
    constraint NOTATION_FILE_PK
        primary key (id)
);
