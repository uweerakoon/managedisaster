create table COALITION
(
ID int auto_increment comment 'Primary Key, auto increment',
X int not null comment 'X coordinate of the coalition in the positioning canvas',
Y int not null comment 'Y coordinate of the coalition in the positioning canvas',
STATUS varchar(255) not null default 'INITIATING' comment 'The current status of the coalition',
FEASIBLE boolean comment 'Specifies whether the coalition is feasible: 0 - false and 1 - true',
FIRE_STATION_ID int comment 'Fire Station id. ref: Fire_Station',
ALLOCATED_FOREST_ID int comment 'Allocated Burning Forest id. ref: Forest',
constraint COALITION_PK primary key (ID),
constraint COALITION_STATUS_CK check (STATUS in ('INITIATING','FORMING','OPTIMIZING','EXECUTING','CANCEL','TERMINATE','DISMISS')),
constraint COALITION_FIRE_STN_FK foreign key(FIRE_STATION_ID) references FIRE_STATION(ID),
constraint COALITION_FOREST_FK foreign key (ALLOCATED_FOREST_ID) references FOREST(ID)
)
comment 'Store data for the coalition',
ENGINE=InnoDB;

commit;
