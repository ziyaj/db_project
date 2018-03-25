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
drop view PostingInfo;
drop view HostRating;
drop view HostInfo;

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
unid INTEGER NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (unid) REFERENCES University
--ON UPDATE CASCADE ?????
);
grant select on Student to public;


CREATE TABLE Hosts (
cid INTEGER,
roomno CHAR(5),
residencename CHAR(30),
dailyrate INTEGER NOT NULL,
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
travelerid INTEGER,
hostid INTEGER,
rating INTEGER,
PRIMARY KEY (travelerid, hostid),
FOREIGN KEY (travelerid) REFERENCES Traveler (cid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
ON DELETE CASCADE
);
grant select on Host_Reviews to public;


CREATE TABLE Traveler_Reviews (
travelerid INTEGER,
hostid INTEGER,
rating INTEGER NOT NULL,
PRIMARY KEY (travelerid, hostid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
ON DELETE CASCADE,
FOREIGN KEY (travelerid) REFERENCES Traveler (cid)
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
ON DELETE CASCADE,
CHECK (pid > 0 AND fromdate < todate)
);
grant select on Posting to public;


CREATE TABLE Contract_Signs (
contractid INTEGER,
fromdate DATE NOT NULL,
todate DATE NOT NULL,
hostid INTEGER NOT NULL,
travelerid INTEGER NOT NULL,
PRIMARY KEY (contractid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid),
FOREIGN KEY (travelerid) REFERENCES Traveler (cid)
);
grant select on Contract_Signs to public;

-- special view for posting info
CREATE VIEW PostingInfo(pid, fromdate, todate, hostid, hostname, roomno, residencename, university, dailyrate) AS
SELECT P.pid, P.fromdate, P.todate, H.cid, S.name, H.roomno, H.residencename, U.name, H.dailyrate
FROM Posting P, Hosts H, Student S, University U
WHERE P.hostid = H.cid AND H.cid = S.cid AND S.unid = U.unid;

-- special view for host rating
CREATE VIEW HostRating(hostid, hostname, rating) AS
SELECT H.cid, S.name, HR.rating
FROM Hosts H, Student S, (SELECT TR.hostid AS id, AVG(TR.rating) AS rating
                          FROM Traveler_Reviews TR, Hosts H
                          WHERE TR.hostid = H.cid
                          GROUP BY TR.hostid) HR
WHERE H.cid = S.cid AND H.cid = HR.id;

-- speical view for hosts
CREATE VIEW HostInfo(hostid, hostname, gender, university, roomno, residencename, dailyrate) AS
SELECT S.cid, S.name, S.gender, U.name, H.roomno, H.residencename, H.dailyrate
FROM Student S, Hosts H, University U
WHERE S.cid = H.cid AND S.unid = U.unid;


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
values(1, 'Harry Potter', 'M', 1);

insert into Student
values(2, 'Cersei Lannister', 'F', 2);

insert into Student
values(3, 'Guillaume Lux', 'F', 3);

insert into Student
values(4, 'Jaap Eris', 'M', 4);

insert into Student
values(5, 'Kidlat Urbanus', 'F', 5);

insert into Student
values(6, 'Hannah Abbott', 'F', 6);

insert into Student
values(7, 'Ludo Bagman', 'M', 7);

insert into Student
values(8, 'Bathilda Bagshot', 'F', 9);

insert into Student
values(9, 'Katie Bell', 'F', 8);

insert into Student
values(10, 'Cuthbert Binns', 'F', 10);

insert into Student
values(11, 'Charles Leiserson', 'M', 11);

insert into Student
values(12, 'Robert Floyd', 'M', 12);

insert into Student
values(13, 'Babara Liskov', 'F', 17);

insert into Student
values(14, 'Brian Kernighan', 'M', 16);

insert into Student
values(15, 'Jeffery Dean', 'M', 15);

insert into Student
values(16, 'Guanyao Fu', 'M', 20);

insert into Student
values(17, 'Randal Bryant', 'M', 19);

insert into Student
values(18, 'Ada Lovelace', 'F', 13);

insert into Student
values(19, 'Marie Currie', 'F', 14);

insert into Student
values(20, 'Eric Xing', 'M', 18);

insert into Student
values(21, 'Paul Friedman', 'M', 12);

insert into Hosts
values(1, '1024', 'Thunderbird Crescent', 35);

insert into Hosts
values(2, '106C', 'Student Residence 1', 62);

insert into Hosts
values(3, '237', 'Totem Park', 30);

insert into Hosts
values(4, '221', 'Place Vanier', 50);

insert into Hosts
values(5, '304', 'Student Residence 5', 40);

insert into Hosts
values(6, '321F', 'Church College', 32);

insert into Hosts
values(7, '2214A', 'University Residence 8', 28);

insert into Hosts
values(8, '104', 'Finnerty Residence 5', 30);

insert into Hosts
values(9, '332', 'Student Residence 3', 20);

insert into Hosts
values(10, '157', 'Marine Drive', 25);

insert into Hosts
values(11, '246B', 'Saint George College', 40);

insert into Hosts
values(12, '5213D', 'Serra Mall', 80);

insert into Hosts
values(13, '307C', 'Berkeley College', 65);

insert into Hosts
values(14, '5152D', 'Forbes House', 42);

insert into Hosts
values(15, '1005', 'Fraser Hall', 38);

insert into Hosts
values(16, '603B', 'Princeton Place', 28);

insert into Hosts
values(17, '221', 'Ithaca Park', 24);

insert into Hosts
values(18, '340', 'Ruce College', 24);

insert into Hosts
values(19, '213', 'Haystack Hall', 34);

insert into Hosts
values(20, '121', 'Mellon House', 50);

insert into Hosts
values(21, '420C', 'Maple House', 24);

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
values(1, '2018-01-01', '2018-01-30', 2, 1);

insert into Contract_Signs
values(2, '2018-01-01', '2018-01-30', 3, 1);

insert into Contract_Signs
values(3, '2018-01-01', '2018-01-30', 4, 1);

insert into Contract_Signs
values(4, '2018-01-01', '2018-01-30', 5, 1);

insert into Contract_Signs
values(5, '2018-01-01', '2018-01-30', 6, 1);

insert into Contract_Signs
values(6, '2018-01-01', '2018-01-30', 7, 1);

insert into Contract_Signs
values(7, '2018-01-01', '2018-01-30', 8, 1);

insert into Contract_Signs
values(8, '2018-01-01', '2018-01-30', 9, 1);

insert into Contract_Signs
values(9, '2018-01-01', '2018-01-30', 10, 1);

insert into Contract_Signs
values(10, '2018-01-01', '2018-01-30', 11, 1);

insert into Contract_Signs
values(11, '2018-01-01', '2018-01-30', 12, 1);

insert into Contract_Signs
values(12, '2018-01-01', '2018-01-30', 13, 1);

insert into Contract_Signs
values(13, '2018-01-01', '2018-01-30', 14, 1);

insert into Contract_Signs
values(14, '2018-01-01', '2018-01-30', 15, 1);

insert into Contract_Signs
values(15, '2018-01-01', '2018-01-30', 16, 1);

insert into Contract_Signs
values(16, '2018-01-01', '2018-01-30', 17, 1);

insert into Contract_Signs
values(17, '2018-01-01', '2018-01-30', 18, 1);

insert into Contract_Signs
values(18, '2018-01-01', '2018-01-30', 19, 1);

insert into Contract_Signs
values(19, '2018-01-01', '2018-01-30', 20, 1);

insert into Contract_Signs
values(20, '2018-01-01', '2018-01-30', 1, 2);

