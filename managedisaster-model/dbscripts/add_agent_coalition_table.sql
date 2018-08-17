create table AGENT_COALITION
(
ID int auto_increment comment 'Primary Key, auto increment',
AGENT_ID int not null comment 'Agent id. ref: AGENT',
COALITION_ID int not null comment 'Coalition id. ref: Coalition',
TIME datetime default current_timestamp on update current_timestamp comment 'Time stamp of the record created or updated',
constraint FORMCOAL_PK primary key (ID),
constraint FORMCOAL_AG_FK foreign key (AGENT_ID) references AGENT(ID),
constraint FORMCOAL_COAL_FK foreign key (COALITION_ID) references COALITION(ID)
)
comment 'When agent forming coalition, a single agent be a part of many coalitions',
ENGINE=INNODB;