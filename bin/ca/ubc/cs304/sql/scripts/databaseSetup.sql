CREATE TABLE Branch (
	branch_id integer not null PRIMARY KEY,
	branch_name varchar2(50) not null,
	branch_address varchar2(100) not null
);

create table "PublicArea"
(
    AREA_ID         NUMBER   not null
        constraint PUBLICAREA_PK
            primary key,
    AREA_NAME       CHAR(50) not null,
    AREA_IS_OUTDOOR CHAR     not null
)