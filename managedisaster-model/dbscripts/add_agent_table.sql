create table AGENT
(
ID int auto_increment comment 'Primary Key, auto increment',
X int not null comment 'X coordinate of the agent in the positioning canvas',
Y int not null comment 'Y coordinate of the agent in the positioning canvas',
NAME varchar(255) not null comment 'Name of the agent',
STATUS varchar(255) not null comment 'The current status of the agent: searching',
ROLE varchar(255) not null comment 'The current role of the agent: independent',
COLOR int not null comment 'The color of the agent',
COLOR_NAME varchar(255) comment 'Name of the color for research purposes',
SPEED double(4,2) not null comment 'Speed of the agent',
VICINITY int not null comment 'The distance that the agent can see',
CHEMICAL varchar(255) not null comment 'The chemical that agent used to extinguish fire: CABONDIOXIDE, WATER, HALON',
CHEMICAL_AMOUNT double(5,2) not null comment 'The amount of chemical the agent has',
INITIAL_CHEMICAL_AMOUNT double (5,2) not null comment 'The initial amount of the chemical',
MINIMUM_FIRE_PROXIMITY int not null comment 'The effective proximity to release the chemical',
SQUIRT_PRESSURE int not null comment 'The amount of chemical that can be ejected in a single unit of time as a fast steam',
FILLING_UP_PRESSURE int not null comment 'The amount of chemical that can be filled up in a single unit of time',
ACTIVE boolean not null default 0 comment 'Specifies whether the agent is active or inactive: 0 - false and 1 - true',
INITIAL_STATUS varchar(255) not null default 'SEARCHING' comment 'Determine the initial state of the agent: SEARCHING,
			IDENTIFYING_AND_ASSESSING_RISK,ESTIMATING_RESOURCES,FORMING_COALITIONS,TRAVELING,TRAVELING_TO_FIRE_STATION,
			EXECUTING_TASKS,VALIDATING_THE_EXECUTION,REVIEWING_AND_UPDATING,FILLING_UP_CHEMICAL,NECESSITY_OF_CHEMICAL',
COALITION_ID int comment 'Coalition id. ref: Coalition',
FIRE_STATION_ID int comment 'Fire Station id. ref: Fire_Station',
constraint AGENT_PK primary key (ID),
constraint AGENT_STATUS_CK check (STATUS in ('SEARCHING')),
constraint AGENT_ROLE_CK check (ROLE in ('INDEPENDENT')),
constraint AGENT_INITSTATE_CK check (INITIAL_STATUS in ('SEARCHING','IDENTIFYING_AND_ASSESSING_RISK',
 	'ESTIMATING_RESOURCES','FORMING_COALITIONS','TRAVELING','TRAVELING_TO_FIRE_STATION','EXECUTING_TASKS',
 	'VALIDATING_THE_EXECUTION','REVIEWING_AND_UPDATING','FILLING_UP_CHEMICAL','NECESSITY_OF_CHEMICAL')),
constraint AGENT_COAL_FK foreign key (COALITION_ID) references COALITION(ID),
constraint AGENT_FIRE_STN_FK foreign key(FIRE_STATION_ID) references FIRE_STATION(ID)
)
comment 'Store the generic agent information',
ENGINE=INNODB;

insert into AGENT (X, Y, NAME, STATUS, ROLE, COLOR, COLOR_NAME, SPEED, VICINITY, 
  CHEMICAL, CHEMICAL_AMOUNT, INITIAL_CHEMICAL_AMOUNT, MINIMUM_FIRE_PROXIMITY, SQUIRT_PRESSURE, FILLING_UP_PRESSURE)
values 
(1, 1, 'UDARA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(1, 2, 'ADITHA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(1, 3, 'ONITHA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(1, 4, 'SUHANI', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 1, 'PRABANJALEE', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 2, 'KAMAL', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(2, 3, 'CHIRANTHI', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(2, 4, 'SAJANA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 1, 'DINALI', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 2, 'METHUJA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 3, 'SATHIRA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(50, 1, 'SAMADHI', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(50, 2, 'CHANAKA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(50, 3, 'GAMINI', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(50, 4, 'CHITHRA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 1, 'ANUPIYA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 2, 'DHARSHIKA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(51, 3, 'YASANGA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(51, 4, 'SANJEEWANEE', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(52, 1, 'PANCHALA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(52, 2, 'MANJALEE', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(52, 3, 'MANJU', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(96, 1, 'GEETHA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(96, 2, 'RANJI', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(96, 3, 'MAHINDA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(96, 4, 'RASIKA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 1, 'LIONEL', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 2, 'MILEE', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(97, 3, 'ANANDA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(97, 4, 'KATHILA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 1, 'INDRAWATHI', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 2, 'JEMIS', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 3, 'OMIS', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(1, 51, 'ASANKA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(1, 52, 'MENAKA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(1, 53, 'ASANKA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(1, 54, 'SAMINDA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 51, 'NISALA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 52, 'GEETH', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(2, 53, 'HEWA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(2, 54, 'RUVAN', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 51, 'MADU', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 52, 'ANOMA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 53, 'NOEL', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(49, 51, 'DANUSHA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(49, 52, 'MARE', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(49, 53, 'BOGA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(49, 54, 'SAJJA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(50, 51, 'SAMAN', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(50, 52, 'SADIQ', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(50, 53, 'RASHAN', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(50, 54, 'DARE', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 51, 'CHANNA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 52, 'DUNETH', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 53, 'NUWAN', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(51, 54, 'NUSRAN', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(96, 51, 'CHATHURIKA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(96, 52, 'NIMESHA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(96, 53, 'MANOJ', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(96, 54, 'SACHINTHA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 51, 'SACHITH', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 52, 'CHAMIL', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(97, 53, 'SESHIKA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(97, 54, 'HARSHANI', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 51, 'ISSA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 52, 'JANAKA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 53, 'SULANI', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(1, 96, 'PARAKRAMA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(1, 97, 'PAWANA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(1, 98, 'PRABAKARA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(1, 99, 'PRABHA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 96, 'PRABODHA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(2, 97, 'PRADEEPA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(2, 98, 'PRAMODHA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(2, 99, 'PRATHAPA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 96, 'PRAVEENA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 97, 'PRAKASHA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(3, 98, 'PASIDU', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(49, 96, 'ANU', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(49, 97, 'AVI', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(49, 98, 'ARAVINDA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(49, 99, 'ASHOKA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(50, 96, 'ATHISHA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(50, 97, 'ABHITHA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(50, 98, 'ACHALA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(50, 99, 'AKILA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 96, 'AMAL', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 97, 'AMARA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(51, 98, 'AMILA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),

(96, 96, 'ABHILASHA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(96, 97, 'ABHIMANI', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(96, 98, 'ACHALA', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(96, 99, 'ACHIRA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 96, 'ASHOKA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(97, 97, 'AHINSA', 'SEARCHING','INDEPENDENT',-16777216, 'Black', 1.0, 80, 'HALON', 100.00, 100.00, 20, 5, 15),
(97, 98, 'AMITHA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30),
(97, 99, 'ANJALI', 'SEARCHING','INDEPENDENT', -65536, 'Red', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 96, 'ANJANA', 'SEARCHING','INDEPENDENT', -256, 'Green', 1.0, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 97, 'ANUJA', 'SEARCHING','INDEPENDENT', -16711936, 'Yellow', 1.5, 80, 'CARBONDIOXIDE', 200.00, 200.00, 20, 7, 21),
(98, 98, 'ANUSHA', 'SEARCHING','INDEPENDENT',-16776961, 'Blue', 1.5, 80, 'WATER', 500.00, 500.00, 30, 10, 30)

;

commit;