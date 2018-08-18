create table TWEET
(
ID int auto_increment comment 'Primary Key, auto increment',
SEVERITY varchar(255) not null comment 'Severity of the information given: SEVERE, NORMAL, MILD',
MESSAGE_TYPE varchar(255) not null comment 'Type of the information given: FIRE, AGENT',
FOREST_ID int not null comment 'Forest that the information regards: References to FOREST(ID)',
TWEET_TIME int not null comment 'Pre-defined time that the tweet will be affecting the simulation',
POSTED tinyint(1) not null default 0 comment 'Specifies whether tweet is posted or not: 0 - false (default) and 1 - true',
POSTED_TIME int comment 'Time that the tweet is posted on twitter.com',
READ_TIME int comment 'Time that the tweet is read on twitter.com',
active boolean not null default 0 comment 'Specifies whether the tweet is active or inactive: 0 - false and 1 - true',
constraint TWEET_PK primary key (ID),
constraint TWEET_FOREST_FK foreign key (FOREST_ID) references FOREST(ID),
constraint TWEET_SEVERITY_CK check (SEVERITY in ('SEVERE', 'NORMAL', 'MILD')),
constraint TWEET_MSG_TYPE_CK check (MESSAGE_TYPE in ('FIRE', 'AGENT'))
)
comment 'Store the tweets that agents post in the twitter',
ENGINE=INNODB;

insert into TWEET(SEVERITY, MESSAGE_TYPE, FOREST_ID, TWEET_TIME, POSTED)
value ('MILD','FIRE',4, 120, 0)
;

commit;
