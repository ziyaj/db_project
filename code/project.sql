drop table salesdetails;
drop table sales;
drop table titleditors;
drop table titleauthors;
drop table titles;
drop table publishers;
drop table authors;
drop table editors;
drop table EXAM;
drop table LICENSE;
drop table DRIVER;
drop table BRANCH;

drop table Host_Reviews;
drop table Traveler_Reviews;
drop table Contract_Signs;
drop table Posting;
drop table Hosts;
drop table Residence;
drop table Traveler;
drop table Student;
drop table University;


CREATE TABLE University (
unid INTEGER,
name CHAR(50),
PRIMARY KEY (unid)
);
grant select on University to public;


CREATE TABLE Student (
cid INTEGER,
name CHAR(20),
gender CHAR(1),
credit INTEGER,
unid INTEGER NOT NULL,
is_active INTEGER NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (unid) REFERENCES University
--ON UPDATE CASCADE ?????
);
grant select on Student to public;


CREATE TABLE Residence (
postal_code CHAR(6),
address CHAR(50),
link CHAR(50),
gender CHAR(1),
daily_rate INTEGER NOT NULL,
PRIMARY KEY (postal_code, address)
);
grant select on Residence to public;


CREATE TABLE Hosts (
cid INTEGER,
is_checked INTEGER NOT NULL,
postal_code CHAR(6) NOT NULL,
address CHAR(50) NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student,
FOREIGN KEY (postal_code, address) REFERENCES Residence
);
grant select on Hosts to public;


CREATE TABLE Traveler (
cid INTEGER,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student
);
grant select on Traveler to public;


CREATE TABLE Host_Reviews (
traveler_id INTEGER,
hostid INTEGER,
rating INTEGER,
PRIMARY KEY (traveler_id, hostid),
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
ON DELETE CASCADE
);
grant select on Host_Reviews to public;


CREATE TABLE Traveler_Reviews (
traveler_id INTEGER,
postal_code CHAR(6),
address CHAR(50),
rating INTEGER NOT NULL,
PRIMARY KEY (traveler_id, postal_code, address),
FOREIGN KEY (postal_code, address) REFERENCES Residence
ON DELETE CASCADE,
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid)
);
grant select on Traveler_Reviews to public;


CREATE TABLE Posting (
pid INTEGER,
fromdate DATE NOT NULL,
todate DATE NOT NULL,
description CHAR(140),
hostid INTEGER NOT NULL,
PRIMARY KEY (pid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
ON DELETE CASCADE
);
grant select on Posting to public;


CREATE TABLE Contract_Signs (
contract_id INTEGER,
fromdate DATE NOT NULL,
todate DATE NOT NULL,
is_cancelled INTEGER NOT NULL,
hostid INTEGER NOT NULL,
traveler_id INTEGER NOT NULL,
PRIMARY KEY (contract_id),
FOREIGN KEY (postal_code, address) REFERENCES Residence,
FOREIGN KEY (hostid) REFERENCES Hosts (cid),
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid)
);
grant select on Contract_Signs to public;

insert into University
values(1, 'UBC');

insert into University
values(2, 'SFU');

insert into University
values(3, 'University of Victoria');

insert into University
values(4, 'Kwantlen Polytechnic University');

insert into University
values(5, 'University of Toronto');

insert into University
values(6, 'McGill University');

insert into University
values(7, 'University of Alberta');

insert into University
values(8, 'University of Calgary');

insert into University
values(9, 'University of Manitoba');

insert into University
values(10, 'University of Waterloo');

insert into Student
values(1, 'Harry Potter', 'M', 70, 1, 1);

insert into Student
values(2, 'Cersei Lannister', 'F', 5, 2, 1);

insert into Student
values(3, 'Guillaume Lux', 'F', 5, 3, 1);

insert into Student
values(4, 'Jaap Eris', 'M', 5, 4, 1);

insert into Student
values(5, 'Kidlat Urbanus', 'F', 5, 5, 1);

insert into Student
values(6, 'Hannah Abbott', 'F', 5, 6, 1);

insert into Student
values(7, 'Ludo Bagman', 'F', 5, 7, 1);

insert into Student
values(8, 'Bathilda Bagshot', 'F', 5, 9, 1);

insert into Student
values(9, 'Katie Bell', 'F', 5, 8, 1);

insert into Student
values(10, 'Cuthbert Binns', 'F', 5, 10, 1);

insert into Hosts
values(1, 0, 'V6T1Z4', '2324 Wesbrook Mall');

insert into Hosts
values(2, 1, 'M5S3G3', '100 St. George St.');

insert into Hosts
values(3, 0, 'N2L3G1', '200 University Ave W');

insert into Hosts
values(4, 1, 'H3A0G4', '845 Sherbrooke St W');

insert into Hosts
values(5, 1, 'L8S4L8', '1280 Main St W');

insert into Hosts
values(6, 1, 'K7L3N6', '99 University Ave');

insert into Hosts
values(7, 1, 'V5A1S6', '8888 University Dr');

insert into Hosts
values(8, 0, 'V8P5C2', '3800 Finnerty Rd');

insert into Hosts
values(9, 1, 'T6G2R3', '116 St and 85 Ave');

insert into Hosts
values(10, 0, 'T2N1N4', '2500 University Dr NW');

insert into Traveler
values(1);

insert into Traveler
values(2);

insert into Traveler
values(3);

insert into Traveler
values(4);

insert into Traveler
values(5);

insert into Traveler
values(6);

insert into Traveler
values(7);

insert into Traveler
values(8);

insert into Traveler
values(9);

insert into Traveler
values(10);

