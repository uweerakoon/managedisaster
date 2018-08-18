create table FOREST
(
ID int auto_increment comment 'Primary Key, auto increment',
NAME varchar(255) comment 'Name of the forest',
SHAPE polygon NOT NULL,
COORDINATES varchar(255) comment 'Help to understand the location coordinates of the forest',
LABEL_X int comment 'X Coordinate of the named label',
LABEL_Y int comment 'Y Coordinate of the named label',
constraint BUILDING_PK primary key (ID)
)
comment 'Store data for the forest',
ENGINE=InnoDB;

-- 1st Block

set @pines = 'POLYGON((5 5, 15 5, 15 15, 5 15, 5 5))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@pines), 'pines', '5 5, 15 5, 15 15, 5 15, 5 5', 6, 10);

set @aspens = 'POLYGON((20 5, 40 5, 40 20, 27 20, 27 13, 20 13, 20 5))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@aspens), 'aspens', '20 5, 40 5, 40 20, 27 20, 27 13, 20 13, 20 5', 21, 9);

set @spruces = 'POLYGON((5 20, 20 20, 20 30, 5 30, 5 20))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@spruces), 'spruces', '5 20, 20 20, 20 30, 5 30, 5 20', 6, 25);

set @oaks = 'POLYGON((27 23, 45 23, 45 37, 37 37, 37 45, 30 45, 30 35, 27 35, 27 23))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@oaks), 'oaks', '27 23, 45 23, 45 37, 37 37, 37 45, 30 45, 30 35, 27 35, 27 23', 28, 29);

-- 2nd Block

set @cottonwoods = 'POLYGON((63 5, 76 5, 76 25, 63 25, 63 5))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@cottonwoods), 'cottonwoods', '63 5, 76 5, 76 25, 63 25, 63 5', 63, 14);

set @currantbushes = 'POLYGON((83 21, 93 21, 93 27, 83 27, 83 21))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@currantbushes), 'currantbushes', '83 22, 93 22, 93 27, 83 27, 83 22', 85, 24);

set @chokecherries = 'POLYGON((78 30, 90 30, 90 37, 78 37, 78 30))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@chokecherries), 'chokecherries', '78 30, 90 30, 90 37, 78 37, 78 30', 79, 33);

set @ashes = 'POLYGON((55 28, 75 28, 75 48, 65 48, 65 38, 55 38, 55 28))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@ashes), 'ashes', '55 28, 75 28, 75 48, 65 48, 65 38, 55 38, 55 28', 56, 33);

-- 3rd Block

set @willows = 'POLYGON((5 65, 16 65, 16 75, 5 75, 5 65))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@willows), 'willows', '5 65, 16 65, 16 75, 5 75, 5 65', 6, 70);

set @junipers = 'POLYGON((18 58, 38 58, 38 78, 18 78, 18 58))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@junipers), 'junipers', '18 58, 38 58, 38 78, 18 78, 18 58', 19, 69);

set @firs = 'POLYGON((5 85, 10 85, 10 80, 30 80, 30 95, 5 95, 5 85))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@firs), 'firs', '5 85, 10 85, 10 80, 30 80, 30 95, 5 95, 5 85', 6, 90);

-- 4th Block

set @maples = 'POLYGON((70 70, 80 70, 80 80, 70 80, 70 70))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@maples), 'maples', '70 70, 80 70, 80 80, 70 80, 70 70', 71, 74);

set @poplers = 'POLYGON((85 70, 95 70, 95 80, 85 80, 85 70))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@poplers), 'poplers', '85 70, 95 70, 95 80, 85 80, 85 70', 86, 74);

set @boxelders = 'POLYGON((70 85, 80 85, 80 95, 70 95, 70 85))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@boxelders), 'boxelders', '70 85, 80 85, 80 95, 70 95, 70 85', 71, 91);

set @pinyons = 'POLYGON((85 85, 95 85, 95 95, 85 95, 85 85))';
insert into FOREST(SHAPE, NAME, COORDINATES, LABEL_X, LABEL_Y) VALUE (GeomFromText(@pinyons), 'pinyons', '85 85, 95 85, 95 95, 85 95, 85 85', 86, 91);

commit;