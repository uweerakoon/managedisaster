create table COMMUNICATIVE
(
ID int auto_increment comment 'Primary Key, auto increment',
FROM_AGENT int not null comment 'The first agent who is willing to communicate',
TO_AGENT int not null comment 'The othe agent who is willing to communicate to',
WILLINGNESS double not null comment 'Willingness to communicate with each other',
constraint COMMUNICATIVE_PK primary key (ID),
constraint COMMUNICATIVE_FROM_FK foreign key (FROM_AGENT) references AGENT(ID),
constraint COMMUNICATIVE_TO_FK foreign key (TO_AGENT) references AGENT(ID)
)
comment 'Store the information about willingness to communicate with each other',
ENGINE=INNODB;

insert into COMMUNICATIVE(FROM_AGENT,TO_AGENT,WILLINGNESS)
values
(1, 2, 0.1),
(1, 3, 0.2),
(1, 4, 0.4),
(1, 5, 0.5),
(2, 3, 0.65),
(2, 4, 0.78),
(2, 5, 0.23),
(3, 4, 0.34),
(3, 5, 0.2),
(4, 5, 0.1)
;

commit;