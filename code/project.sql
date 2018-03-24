-- Drop pre-existing tables
-- drop table salesdetails;
-- drop table sales;
-- drop table titleditors;
-- drop table titleauthors;
-- drop table titles;
-- drop table publishers;
-- drop table authors;
-- drop table editors;
-- drop table EXAM;
-- drop table LICENSE;
-- drop table DRIVER;
-- drop table BRANCH;

drop table Host_Reviews;
drop table Traveler_Reviews;
drop table Contract_Signs;
drop table Posting;
drop table Hosts;
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


CREATE TABLE Hosts (
cid INTEGER,
is_checked INTEGER NOT NULL,
roomno CHAR(5),
residencename CHAR(30),
daily_rate INTEGER NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student
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
hostid INTEGER,
rating INTEGER NOT NULL,
PRIMARY KEY (traveler_id, hostid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
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
values(13, 'Babara Liskov', 'F', 30, 17, 1);

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

insert into Hosts
values(1, 0, '1024', 'Thunderbird Crescent', 35);

insert into Hosts
values(2, 1, '106C', 'Student Residence 1', 62);

insert into Hosts
values(3, 0, '237', 'Totem Park', 30);

insert into Hosts
values(4, 1, '221', 'Place Vanier', 50);

insert into Hosts
values(5, 1, '304', 'Student Residence 5', 40);

insert into Hosts
values(6, 1, '321F', 'Church College', 32);

insert into Hosts
values(7, 1, '2214A', 'University Residence 8', 28);

insert into Hosts
values(8, 0, '104', 'Finnerty Residence 5', 30);

insert into Hosts
values(9, 1, '332', 'Student Residence 3', 20);

insert into Hosts
values(10, 1, '157', 'Marine Drive', 25);

insert into Hosts
values(11, 0, '246B', 'Saint George College', 40);

insert into Hosts
values(12, 1, '5213D', 'Serra Mall', 80);

insert into Hosts
values(13, 0, '307C', 'Berkeley College', 65);

insert into Hosts
values(14, 1, '5152D', 'Forbes House', 42);

insert into Hosts
values(15, 0, '1005', 'Fraser Hall', 38);

insert into Hosts
values(16, 1, '603', 'Princeton Place', 28);

insert into Hosts
values(17, 0, '221', 'Ithaca Park', 24);

insert into Hosts
values(18, 0, '221', 'Ruce College', 24);

insert into Hosts
values(19, 1, '213', 'Haystack Hall', 34);

insert into Hosts
values(20, 0, '121', 'Mellon House', 50);

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
values(1, '2018-01-01', '2018-01-30', 0, 2, 1);

insert into Contract_Signs
values(2, '2018-01-01', '2018-01-30', 0, 3, 1);

insert into Contract_Signs
values(3, '2018-01-01', '2018-01-30', 0, 4, 1);

insert into Contract_Signs
values(4, '2018-01-01', '2018-01-30', 0, 5, 1);

insert into Contract_Signs
values(5, '2018-01-01', '2018-01-30', 0, 6, 1);

insert into Contract_Signs
values(6, '2018-01-01', '2018-01-30', 0, 7, 1);

insert into Contract_Signs
values(7, '2018-01-01', '2018-01-30', 0, 8, 1);

insert into Contract_Signs
values(8, '2018-01-01', '2018-01-30', 0, 9, 1);

insert into Contract_Signs
values(9, '2018-01-01', '2018-01-30', 0, 10, 1);

insert into Contract_Signs
values(10, '2018-01-01', '2018-01-30', 0, 11, 1);

insert into Contract_Signs
values(11, '2018-01-01', '2018-01-30', 0, 12, 1);

insert into Contract_Signs
values(12, '2018-01-01', '2018-01-30', 0, 13, 1);

insert into Contract_Signs
values(13, '2018-01-01', '2018-01-30', 0, 14, 1);

insert into Contract_Signs
values(14, '2018-01-01', '2018-01-30', 0, 15, 1);

insert into Contract_Signs
values(15, '2018-01-01', '2018-01-30', 0, 16, 1);

insert into Contract_Signs
values(16, '2018-01-01', '2018-01-30', 0, 17, 1);

insert into Contract_Signs
values(17, '2018-01-01', '2018-01-30', 0, 18, 1);

insert into Contract_Signs
values(18, '2018-01-01', '2018-01-30', 0, 19, 1);

insert into Contract_Signs
values(19, '2018-01-01', '2018-01-30', 0, 20, 1);

insert into Contract_Signs
values(20, '2018-01-01', '2018-01-30', 0, 1, 2);




