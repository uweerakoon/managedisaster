create table AGENT_STAT
(
ID int auto_increment comment 'Primary Key, auto increment',
AGENT_ID int not null comment 'Agent id. ref: AGENT',
TIME_STAMP int not null comment 'Time stamp of the simulation',
STATUS varchar(255) not null comment 'The current status of the agent',
constraint AGST_PK primary key (ID),
constraint AGST_AG_FK foreign key (AGENT_ID) references AGENT(ID)
)
comment 'Store the statistical information of the agent behavior',
ENGINE=INNODB;

commit;