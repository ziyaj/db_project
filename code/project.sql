-- Drop pre-existing tables
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


-- Create tables
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
rid INTEGER,
roomno CHAR(5),
residencename CHAR(30),
gender CHAR(1),
daily_rate INTEGER NOT NULL,
PRIMARY KEY (rid)
);
grant select on Residence to public;


CREATE TABLE Hosts (
cid INTEGER,
is_checked INTEGER NOT NULL,
rid INTEGER NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student,
FOREIGN KEY (rid) REFERENCES Residence
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
rid INTEGER,
rating INTEGER NOT NULL,
PRIMARY KEY (traveler_id, rid),
FOREIGN KEY (rid) REFERENCES Residence
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
FOREIGN KEY (hostid) REFERENCES Hosts (cid),
FOREIGN KEY (traveler_id) REFERENCES Traveler (cid)
);
grant select on Contract_Signs to public;


-- Populate data
insert into University
values(1, 'The University of British Columbia');

insert into University
values(2, 'Simon Fraser University');

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

insert into University
values(11, 'Massachusetts Institute of Technology');

insert into University
values(12, 'Stanford University');

insert into University
values(13, 'University of California, Berkeley');

insert into University
values(14, 'Carnegie Mellon University');

insert into University
values(15, 'University of Washington');

insert into University
values(16, 'Princeton Univeristy');

insert into University
values(17, 'Cornell University');

insert into University
values(18, 'University of Illinois Urbana-Champaign');

insert into University
values(19, 'University of Michigan');

insert into University
values(20, 'Georgia Institute of Technology');

insert into Student
values(1, 'Harry Potter', 'M', 70, 1, 1);

insert into Student
values(2, 'Cersei Lannister', 'F', 35, 2, 1);

insert into Student
values(3, 'Guillaume Lux', 'F', 20, 3, 1);

insert into Student
values(4, 'Jaap Eris', 'M', 24, 4, 1);

insert into Student
values(5, 'Kidlat Urbanus', 'F', 18, 5, 1);

insert into Student
values(6, 'Hannah Abbott', 'F', 5, 6, 1);

insert into Student
values(7, 'Ludo Bagman', 'M', 42, 7, 1);

insert into Student
values(8, 'Bathilda Bagshot', 'F', 51, 9, 1);

insert into Student
values(9, 'Katie Bell', 'F', 33, 8, 1);

insert into Student
values(10, 'Cuthbert Binns', 'F', 9, 10, 1);

insert into Student
values(11, 'Charles Leiserson', 'M', 9, 11, 1);

insert into Student
values(12, 'Robert Floyd', 'M', 29, 12, 0);

insert into Student
values(13, 'Babara Liskov', 'F', 30, 11, 1);

insert into Student
values(14, 'Brian Kernighan', 'M', 55, 16, 1);

insert into Student
values(15, 'Jeffery Dean', 'M', 25, 15, 1);

insert into Student
values(16, 'Guanyao Fu', 'M', 15, 20, 1);

insert into Student
values(17, 'Randal Bryant', 'M', 32, 19, 1);

insert into Student
values(18, 'Ada Lovelace', 'F', 23, 13, 0);

insert into Student
values(19, 'Marie Currie', 'F', 14, 14, 0);

insert into Student
values(20, 'Eric Xing', 'M', 18, 18, 1);

insert into Residence
values (1, '1024', 'Thunderbird Crescent', 'M', 35);

insert into Residence
values (2, '106C', 'Student Residence 1', 'F', 62);

insert into Residence
values (3, '237', 'Totem Park', 'M', 30);

insert into Residence
values (4, '221', 'Place Vanier', 'F', 50);

insert into Residence
values (5, '304', 'Student Residence 5', 'M', 40);

insert into Residence
values (6, '321F', 'Church College', 'F', 32);

insert into Residence
values (7, '2214A', 'University Residence 8', 'M', 28);

insert into Residence
values (8, '104', 'Finnerty Residence 5', 'F', 30);

insert into Residence
values (9, '332', 'Student Residence 3', 'M', 20);

insert into Residence
values (10, '157', 'Marine Drive', 'M', 25);

insert into Residence
values (11, '246B', 'Saint George College', 'M', 40);

insert into Residence
values (12, '5213D', 'Serra Mall', 'F', 80);

insert into Residence
values (13, '307C', 'Berkeley College', 'M', 65);

insert into Residence
values (14, '5152D', 'Forbes House', 'M', 42);

insert into Residence
values (15, '1005', 'Fraser Hall', 'M', 38);

insert into Residence
values (16, '603', 'Princeton Place', 'F', 28);

insert into Residence
values (17, '221', 'Ithaca Park', 'M', 24);

insert into Hosts
values(1, 0, 1);

insert into Hosts
values(2, 1, 2);

insert into Hosts
values(3, 0, 3);

insert into Hosts
values(4, 1, 4);

insert into Hosts
values(5, 1, 5);

insert into Hosts
values(6, 1, 6);

insert into Hosts
values(7, 1, 7);

insert into Hosts
values(8, 0, 8);

insert into Hosts
values(9, 1, 9);

insert into Hosts
values(10, 0, 10);

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

insert into Posting
values(1, '2018-01-01', '2018-01-30', 'test1', 1);

insert into Posting
values(2, '2018-02-01', '2018-02-28', 'test2', 2);

insert into Posting
values(3, '2017-03-01', '2017-03-30', 'test3', 2);

insert into Posting
values(4, '2017-04-01', '2017-04-30', 'test4', 3);

insert into Posting
values(5, '2017-05-01', '2017-05-30', 'test5', 3);

insert into Posting
values(6, '2017-06-01', '2017-06-30', 'test6', 3);

insert into Posting
values(7, '2017-07-01', '2017-07-30', 'test7', 4);

insert into Posting
values(8, '2017-08-01', '2017-08-30', 'test8', 4);

insert into Posting
values(9, '2017-09-01', '2017-09-30', 'test9', 4);

insert into Posting
values(10, '2017-10-01', '2017-10-30', 'test10', 4);

insert into Host_Reviews
values(1, 2, 3);

insert into Host_Reviews
values(3, 2, 2);

insert into Host_Reviews
values(4, 2, 5);

insert into Host_Reviews
values(5, 2, 1);

insert into Host_Reviews
values(6, 2, 3);

insert into Host_Reviews
values(7, 2, 9);

insert into Host_Reviews
values(2, 4, 3);

insert into Host_Reviews
values(3, 4, 8);

insert into Host_Reviews
values(5, 4, 10);

insert into Host_Reviews
values(8, 4, 9);

insert into Host_Reviews
values(9, 4, 5);

insert into Traveler_Reviews
values(9, 1, 8);

insert into Traveler_Reviews
values(8, 2, 5);

insert into Traveler_Reviews
values(7, 3, 3);

insert into Traveler_Reviews
values(6, 4, 9);

insert into Traveler_Reviews
values(5, 5, 2);

insert into Traveler_Reviews
values(4, 6, 7);

insert into Traveler_Reviews
values(3, 7, 9);

insert into Traveler_Reviews
values(2, 8, 6);

insert into Traveler_Reviews
values(1, 9, 8);

insert into Traveler_Reviews
values(5, 10, 7);

insert into Contract_Signs
values(1, '2018-01-01', '2018-01-30', 0, 1, 2);

insert into Contract_Signs
values(2, '2018-01-01', '2018-01-30', 0, 1, 2);

insert into Contract_Signs
values(3, '2018-01-01', '2018-01-30', 0, 1, 2);

insert into Contract_Signs
values(4, '2018-01-01', '2018-01-30', 0, 1, 2);

insert into Contract_Signs
values(5, '2018-01-01', '2018-01-30', 0, 1, 2);



