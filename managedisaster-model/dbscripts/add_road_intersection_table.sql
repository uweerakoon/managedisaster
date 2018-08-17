create table ROAD_INTERSECTION
(
ID int auto_increment comment 'Primary Key, auto increment',
ROAD_ID int not null comment 'The corresponding road id - foreign key into road',
SOURCE_INTERSECTION_ID int not null comment 'Source instersection of the given road - foreign key into intersection',
DESTINATION_INTERSECTION_ID int not null comment 'Destination intersection of the given road - foreign key into intersection',
constraint ROAD_INTERSECTION_PK primary key (ID),
constraint ROAD_ID_FK foreign key (ROAD_ID) references ROAD(ID),
constraint SRC_INTERSN_FK foreign key (SOURCE_INTERSECTION_ID) references INTERSECTION(ID),
constraint DEST_INTERSN_FK foreign key (DESTINATION_INTERSECTION_ID) references INTERSECTION(ID)
) 
comment 'Store data for the road intersection connection',
ENGINE=InnoDB;

insert into ROAD_INTERSECTION(ROAD_ID, SOURCE_INTERSECTION_ID, DESTINATION_INTERSECTION_ID)
values
(1, 1, 2),
(2, 2, 3),
(3, 1, 4),
(4, 4, 7),
(5, 4, 5),
(6, 5, 6),
(7, 2, 5),
(8, 5, 8),
(9, 7, 8),
(10, 8, 9),
(11, 3, 6),
(12, 6, 9)
;

commit;
