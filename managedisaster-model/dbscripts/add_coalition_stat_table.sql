create table COALITION_STAT
(
ID int auto_increment comment 'Primary Key, auto increment',
COALITION_ID int not null comment 'Coalition id. refer: COALITION',
BUILDING_ID int comment 'Building (Burning) id. ref: Building',
TIME_STAMP int not null comment 'Time stamp of the simulation',
RESOURCE_AMOUNT double(10,2) not null default 0.0 comment 'The total number of resources posses by the coalition: water interms of all chemicals',
TASK_AMOUNT double(10,2) not null default 0.0 comment 'The total amount of resources required by the task, burning building: fire and smoke',
constraint COALSTAT_PK primary key (ID),
constraint COALSTAT_COAL_FK foreign key (COALITION_ID) references COALITION(ID),
constraint COALSTAT_BUILD_FK foreign key (BUILDING_ID) references BUILDING(ID)
)
comment 'Store the statistical information of the active coalition',
ENGINE=INNODB;

commit;
