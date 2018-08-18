create table COALITION_FOREST
(
ID int auto_increment comment 'Primary Key, auto increment',
COALITION_ID int comment 'Coalition id. ref: Coalition',
FOREST_ID int comment 'Burning Forest id. ref: Forest',
RESOURCE_AMOUNT double(10,2) comment 'The total number of resources posses by the coalition: water interms of all chemicals',
TASK_AMOUNT double(10,2) comment 'The total amount of resources required by the task, burning forest: fire and smoke',
UTILITY double(10,2) comment 'The utility of the coalition',
ALGORITHM varchar(255) comment 'Utility calculated algorithm: ATV, NECTAR',
STATUS varchar(10) comment 'If the status is cancel then that record is kept for research purposes',
constraint COALFOREST_PK primary key (ID),
constraint COALFOREST_COAL_FK foreign key (COALITION_ID) references COALITION(ID),
constraint COALFOREST_FOREST_FK foreign key (FOREST_ID) references FOREST(ID)
)
comment 'Store the relationship between coalition and burninging forest utilities',
ENGINE=INNODB;

commit;