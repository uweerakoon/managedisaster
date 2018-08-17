create table ROAD
(
ID int auto_increment comment 'Primary Key, auto increment',
X int not null comment 'Starting x coordinate of the road',
Y int not null comment 'Starting y coordinate of the road',
LENGTH int not null comment 'The length of the road',
WIDTH int not null comment 'The width of the road',
ORIENTATION varchar(15) not null comment 'States the orientation of the road - horizontal, vertical',
KIND varchar(15) not null comment 'indicate the kind of the road - normal, elevated, bridge, tunnel, emergency',
CARS_PASS_TO_HEAD int comment 'The number of cars passed to the head',
CARS_PASS_TO_TAIL int comment 'The number of cars passed to the tail',
HUMANS_PASS_TO_HEAD int comment 'The number of humans passed to the head',
HUMANS_PASS_TO_TAIL int comment 'The number of humans passed to the tail',
MEDIAN_STRIP boolean not null comment 'Whether there is a median strip or not',
LINES_TO_HEAD int not null comment 'The number of traffic lanes from tail to head',
LINES_TO_TAIL int not null comment 'The number of traffic lanes from head to tail',
WIDTH_FOR_WALKERS double(4,2) not null comment 'The width of the side walk for the pedestrians',
constraint ROAD_PK primary key (ID),
constraint ROAD_KIND_CK check (ROAD_KIND in ('NORMAL,ELEVATED,BRIDGE,TUNNEL,EMERGENCY')),
constraint ROAD_ORIENTATION_CK check (ORIENTATION in ('horizontal', 'vertical'))
) 
comment 'Store data for the road',
ENGINE=InnoDB;

insert into ROAD(X,Y,LENGTH, WIDTH, ORIENTATION, KIND, MEDIAN_STRIP, LINES_TO_HEAD, LINES_TO_TAIL, WIDTH_FOR_WALKERS)
values
(0, 0, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(50, 0, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(0, 5, 45, 5, 'VERTICAL','NORMAL', true, 1, 1, 1.0),
(0, 55, 40, 5, 'VERTICAL','NORMAL', true, 1, 1, 1.0),
(0, 50, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(50, 50, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(48, 5, 45, 5, 'VERTICAL', 'NORMAL', true, 1, 1, 1.0),
(48, 55, 40, 5, 'VERTICAL', 'NORMAL', true, 1, 1, 1.0),
(0, 95, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(50, 95, 50, 5, 'HORIZONTAL','NORMAL', true, 1, 1, 1.0),
(95, 5, 45, 5, 'VERTICAL', 'NORMAL', true, 1, 1, 1.0),
(95, 55, 40, 5, 'VERTICAL', 'NORMAL', true, 1, 1, 1.0)
;


commit;

select * from road;