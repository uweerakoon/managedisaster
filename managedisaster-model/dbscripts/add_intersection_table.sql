create table INTERSECTION
(
ID int auto_increment comment 'Primary Key, auto increment',
X int not null comment 'X coordinate of the intersection',
Y int not null comment 'Y coordinate of the intersection',
constraint INTERSECTION_PK primary key (ID)
) 
comment 'Store data for the road intersections',
ENGINE=InnoDB;

insert into INTERSECTION(X,Y)
values
(0, 0),
(50, 0),
(100, 0),
(0, 50),
(50, 50),
(95, 50),
(0, 100),
(50, 100),
(100, 100)
;

commit;

select * from intersection;