create table COALITION_BUILDING
(
ID int auto_increment comment 'Primary Key, auto increment',
COALITION_ID int comment 'Coalition id. ref: Coalition',
BUILDING_ID int comment 'Burning Building id. ref: Building',
RESOURCE_AMOUNT double(10,2) comment 'The total number of resources posses by the coalition: water interms of all chemicals',
TASK_AMOUNT double(10,2) comment 'The total amount of resources required by the task, burning building: fire and smoke',
UTILITY double(10,2) comment 'The utility of the coalition',
ALGORITHM varchar(255) comment 'Utility calculated algorithm: ATV, NECTAR',
STATUS varchar(10) comment 'If the status is cancel then that record is kept for research purposes',
constraint COALBUILD_PK primary key (ID),
constraint COALBUILD_COAL_FK foreign key (COALITION_ID) references COALITION(ID),
constraint COALBUILD_BUILD_FK foreign key (BUILDING_ID) references BUILDING(ID)
)
comment 'Store the relationship between coalition and burninging building utilities',
ENGINE=INNODB;

commit;