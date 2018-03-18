drop table University;
drop table Student;
drop table Hosts;
drop table Traveler;
drop table Residence;
drop table Host_Reviews;
drop table Traveler_Reviews;
drop table Posting;
drop table Contract_Signs;


CREATE TABLE University (
unid INTEGER,
name CHAR(50),
PRIMARY KEY (unid)
)

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
ON UPDATE CASCADE
)

grant select on Student to public;

CREATE TABLE Hosts (
cid INTEGER,
is_checked INTEGER NOT NULL,
postal_code CHAR(6) NOT NULL,
address CHAR(30) NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student,
FOREIGN KEY (postal_code, address) REFERENCES Residence
)

grant select on Hosts to public;

CREATE TABLE Traveler (
cid INTEGER,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student
)

grant select on Traveler to public;


CREATE TABLE Residence (
postal_code CHAR(6),
address CHAR(30),
link CHAR(50),
gender CHAR(1),
daily_rate INTEGER NOT NULL,
PRIMARY KEY (postal_code, address)
)

grant select on Residence to public;

CREATE TABLE Host_Reviews (
traveler_id INTEGER,
hostid INTEGER,
rating INTEGER,
PRIMARY KEY (traveler_id, hostid),
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid),
FOREIGN KEY (hostid) REFERENCES Host (cid)
ON DELETE CASCADE
)

grant select on Host_Reviews to public;

CREATE TABLE Traveler_Reviews (
traveler_id INTEGER,
postal_code CHAR(6),
address CHAR(30),
rating INTEGER NOT NULL,
PRIMARY KEY (traveler_id, postal_code, address),
FOREIGN KEY (postal_code, address) REFERENCES Residence
ON DELETE CASCADE,
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid)
)

grant select on Traveler_Reviews to public;

CREATE TABLE Posting (
pid INTEGER,
fromdate DATE NOT NULL,
todate DATE NOT NULL,
description CHAR(140),
hostid INTEGER NOT NULL,
PRIMARY KEY (pid),
FOREIGN KEY (hostid) REFERENCES Host (cid)
ON DELETE CASCADE
)

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
FOREIGN KEY (hostid) REFERENCES Host (cid),
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid)
)

grant select on Contract_Signs to public;


