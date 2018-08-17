create table FIRE_TRUCK
(
ID int auto_increment comment 'Primary Key, auto increment',
X int not null comment 'X coordinate of the fire truck in the positioning canvas',
Y int not null comment 'Y coordinate of the fire truck in the positioning canvas',
SPEED double(4,2) not null comment 'Speed of the fire truck',
CHEMICAL varchar(255) not null comment 'The chemical that fire truck used to extinguish fire: CABONDIOXIDE, WATER, HALON',
CHEMICAL_AMOUNT double(7,2) not null comment 'The amount of chemical the fire truck has',
INITIAL_CHEMICAL_AMOUNT double (7,2) not null comment 'The initial amount of the chemical',
MINIMUM_FIRE_PROXIMITY int not null comment 'The effective proximity to release the chemical',
SQUIRT_PRESSURE int not null comment 'The amount of chemical that can be ejected in a single unit of time as a fast steam',
FILLING_UP_PRESSURE int not null comment 'The amount of chemical that can be filled up in a single unit of time',
ACTIVE boolean not null default 0 comment 'Specifies whether the fire truck is active or inactive: 0 - false and 1 - true',
POWER double(8,2) not null comment 'The amount of power that the fire truck has',
INITIAL_POWER double(8,2) not null comment 'The initial amount of power that the fire truck has',
PASSENGER_CAPACITY int not null comment 'The number of agents that fire truck can facilitate',

constraint FIRE_TRUCK_PK primary key (ID)
)
comment 'Store the generic fire truck information',
ENGINE=INNODB;

insert into FIRE_TRUCK (X, Y, SPEED, CHEMICAL, CHEMICAL_AMOUNT, INITIAL_CHEMICAL_AMOUNT, MINIMUM_FIRE_PROXIMITY, SQUIRT_PRESSURE,
	FILLING_UP_PRESSURE, ACTIVE, POWER, INITIAL_POWER, PASSENGER_CAPACITY)
values 
(49, 11, 5, 'WATER',1000.00,1000.00, 20, 10, 30, 1, 1000.00, 1000.00, 4),
(14, 54, 5, 'WATER',1000.00,1000.00, 20, 10, 30, 1, 1000.00, 1000.00, 4),
(51, 80, 5, 'WATER',1000.00,1000.00, 20, 10, 30, 1, 1000.00, 1000.00, 4),
(73, 51, 5, 'WATER',1000.00,1000.00, 20, 10, 30, 1, 1000.00, 1000.00, 4)
;

commit;