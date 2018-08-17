create table BUILDING
(
ID int auto_increment comment 'Primary Key, auto increment',
NAME varchar(255) comment 'Name of the building',
SHAPE polygon NOT NULL,
COORDINATES varchar(255) comment 'Help to understand the location coordinates of the building',
LABEL_X int comment 'X Coordinate of the named label',
LABEL_Y int comment 'Y Coordinate of the named label',
constraint BUILDING_PK primary key (ID)
)
comment 'Store data for the building',
ENGINE=InnoDB;

-- 1st Block

set @books = 'POLYGON((5 5, 15 5, 15 15, 5 15, 5 5))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@seagull), 'books', '5 5, 15 5, 15 15, 5 15, 5 5', 6, 10);

set @clothing = 'POLYGON((20 5, 40 5, 40 20, 27 20, 27 13, 20 13, 20 5))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@kohls), 'clothing', '20 5, 40 5, 40 20, 27 20, 27 13, 20 13, 20 5', 21, 9);

set @jewelry = 'POLYGON((5 20, 20 20, 20 30, 5 30, 5 20))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@tj_maxx), 'jewelry', '5 20, 20 20, 20 30, 5 30, 5 20', 6, 25);

set @fitness = 'POLYGON((27 23, 45 23, 45 37, 37 37, 37 45, 30 45, 30 35, 27 35, 27 23))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@fitness), 'fitness', '27 23, 45 23, 45 37, 37 37, 37 45, 30 45, 30 35, 27 35, 27 23', 28, 29);

-- 2nd Block

set @bedding = 'POLYGON((63 5, 76 5, 76 25, 63 25, 63 5))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@jcpenney), 'bedding', '63 5, 76 5, 76 25, 63 25, 63 5', 63, 14);

set @salon = 'POLYGON((83 21, 93 21, 93 27, 83 27, 83 21))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@salon), 'salon', '83 22, 93 22, 93 27, 83 27, 83 22', 85, 24);

set @pets = 'POLYGON((78 30, 90 30, 90 37, 78 37, 78 30))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@petsmart), 'pets', '78 30, 90 30, 90 37, 78 37, 78 30', 79, 33);

set @beauty = 'POLYGON((55 28, 75 28, 75 48, 65 48, 65 38, 55 38, 55 28))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@macy), 'beauty', '55 28, 75 28, 75 48, 65 48, 65 38, 55 38, 55 28', 56, 33);

-- 3rd Block

set @tea = 'POLYGON((5 65, 16 65, 16 75, 5 75, 5 65))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@starbucks), 'tea', '5 65, 16 65, 16 75, 5 75, 5 65', 6, 70);

set @grocery = 'POLYGON((18 58, 38 58, 38 78, 18 78, 18 58))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@walmart), 'grocery', '18 58, 38 58, 38 78, 18 78, 18 58', 19, 69);

set @furniture = 'POLYGON((5 85, 10 85, 10 80, 30 80, 30 95, 5 95, 5 85))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@kmart), 'furniture', '5 85, 10 85, 10 80, 30 80, 30 95, 5 95, 5 85', 6, 90);

-- 4th Block

set @house1 = 'POLYGON((70 70, 80 70, 80 80, 70 80, 70 70))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@house1), 'house1', '70 70, 80 70, 80 80, 70 80, 70 70', 71, 74);

set @house2 = 'POLYGON((85 70, 95 70, 95 80, 85 80, 85 70))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@house2), 'house2', '85 70, 95 70, 95 80, 85 80, 85 70', 86, 74);

set @house3 = 'POLYGON((70 85, 80 85, 80 95, 70 95, 70 85))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@house3), 'house3', '70 85, 80 85, 80 95, 70 95, 70 85', 71, 91);

set @house4 = 'POLYGON((85 85, 95 85, 95 95, 85 95, 85 85))';
insert into BUILDING(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@house4), 'house4', '85 85, 95 85, 95 95, 85 95, 85 85', 86, 91);

commit;