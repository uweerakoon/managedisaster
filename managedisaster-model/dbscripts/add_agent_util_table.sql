create table AGENT_UTILITY
(
ID int auto_increment comment 'Primary Key, auto increment',
TIME int not null comment 'The time that agent earned the utility',
AGENT_ID int not null comment 'Agent id, refer: AGENT',
COALITION_ID int not null comment 'Coalition id. ref: Coalition',
UTILITY double(12,2) not null comment 'The utility earned by the agent',
constraint AGENT_UTIL_PK primary key (ID),
constraint AGENTUTIL_COAL_FK foreign key (COALITION_ID) references COALITION(ID),
constraint AGENTUTIL_AGENT_FK foreign key (AGENT_ID) references AGENT(ID)
)
comment 'Store the utility earned by the agent of the coalition after executing the task',
ENGINE=INNODB;