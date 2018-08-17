create table FIRE_STATION
(
ID int auto_increment comment 'Primary Key, auto increment',
STATION_X int not null comment 'X coordinate of the station in the fire hydrants and water mains canvas',
STATION_Y int not null comment 'Y coordinate of the station in the fire hydrants and water mains canvas',
ROAD_X int not null comment 'X coordinate of the road that helps to agent to route themselves',
ROAD_Y int not null comment 'Y coordinate of the road that helps to agent to route themselves',
CHEMICAL_AMOUNT double(15,2) not null comment 'The amount of chemical the station has',
INITIAL_CHEMICAL_AMOUNT double(15,2) not null comment 'The initial amount of chemical the station has',
ACTIVE boolean not null default 0 comment 'Specifies whether the fire station is active or inactive: 0 - false and 1 - true',
OUT_OF_SERVICE boolean not null default 0 comment 'Specifies whether the fire station has chemicals or not: 0 - false and 1 - true',
constraint FS_PK primary key (ID),
constraint FS_ACTIVE_CK check (ACTIVE in (0,1))
)
comment 'Store the fire station information with chemicals',
ENGINE=INNODB;

insert into FIRE_STATION (STATION_X, STATION_Y, ROAD_X, ROAD_Y, CHEMICAL_AMOUNT, INITIAL_CHEMICAL_AMOUNT, ACTIVE, OUT_OF_SERVICE)
values 
(13, 47, 15, 50, 10000000.00, 10000000.00, 1, 0),
(53, 10, 52, 11, 10000000.00, 10000000.00, 1, 0),
(72, 55, 73, 54, 10000000.00, 10000000.00, 1, 0),
(45, 79, 48, 80, 10000000.00, 10000000.00, 1, 0)
;

commit;