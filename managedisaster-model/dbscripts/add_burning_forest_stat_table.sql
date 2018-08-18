create table BURNING_FOREST_STAT
(
ID int auto_increment comment 'Primary Key, auto increment',
FOREST_ID int not null comment 'Forest id. refer: Forest',
TIME_STAMP int not null comment 'Time stamp of the simulation',
FIRE_AMOUNT double(10,2) not null default 0.0 comment 'Amount of the fire in the building',
SMOKE_AMOUNT double(10,2) not null default 0.0 comment 'Amount of the smoke in the building',
WATER_AMOUNT double(10,2) not null default 0.0 comment 'Amount of the water in the building',
constraint BBST_PK primary key (ID),
constraint BBST_FOREST_FK foreign key (FOREST_ID) references FOREST(ID) 
)
comment 'Store the statistical information of the burning building',
ENGINE=INNODB;

commit;