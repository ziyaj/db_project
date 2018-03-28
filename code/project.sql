-- Drop pre-existing tables
drop table Host_Reviews;
drop table Traveler_Reviews;
drop table Contract_Signs;
drop table Posting;
drop table Hosts;
drop table Traveler;
drop table Student;
drop table University;
drop view PostingInfo;
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
password CHAR(10) NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (unid) REFERENCES University,
CHECK (gender = 'M' OR gender = 'F')
);
grant select on Student to public;


CREATE TABLE Hosts (
cid INTEGER,
roomno CHAR(5),
residencename CHAR(30),
dailyrate INTEGER NOT NULL,
PRIMARY KEY (cid),
FOREIGN KEY (cid) REFERENCES Student,
CHECK (dailyrate BETWEEN 0 AND 200)
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
ON DELETE CASCADE,
CHECK (hostid <> travelerid AND rating BETWEEN 1 AND 10)
);
grant select on Host_Reviews to public;


CREATE TABLE Traveler_Reviews (
travelerid INTEGER,
hostid INTEGER,
rating INTEGER NOT NULL,
PRIMARY KEY (travelerid, hostid),
FOREIGN KEY (hostid) REFERENCES Hosts (cid)
ON DELETE CASCADE,
FOREIGN KEY (travelerid) REFERENCES Traveler (cid),
CHECK (travelerid <> hostid AND rating BETWEEN 1 AND 10)
);
grant select on Traveler_Reviews to public;


CREATE TABLE Posting (
pid INTEGER,
fromdate DATE NOT NULL,
todate DATE NOT NULL,
description CHAR(100),
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
FOREIGN KEY (travelerid) REFERENCES Traveler (cid),
CHECK (contractid > 0 AND fromdate < todate AND hostid <> travelerid)
);
grant select on Contract_Signs to public;


CREATE OR REPLACE TRIGGER No_Overlap_Posting
BEFORE INSERT OR UPDATE ON Posting
FOR EACH ROW

DECLARE
    NumExists         NUMBER;
    Overlap_Posting   EXCEPTION;

BEGIN

    SELECT COUNT(*) INTO NumExists FROM Posting P
    WHERE :new.hostid = P.hostid AND :new.pid <> P.pid
          AND ((:new.fromdate >= P.fromdate AND :new.fromdate <= P.todate)
          OR (:new.todate >= P.fromdate AND :new.todate <= P.todate));

    IF (NumExists > 0) THEN
        RAISE Overlap_Posting;
    END IF;

EXCEPTION
   WHEN Overlap_Posting THEN
      Raise_application_error (-20301,
         'The posting is overlapped with an existing one for that host');
END;

/


CREATE OR REPLACE TRIGGER No_Overlap_Contracts
BEFORE INSERT OR UPDATE ON Contract_Signs
FOR EACH ROW

DECLARE
    NumExists          NUMBER;
    Overlap_Contract   EXCEPTION;

BEGIN

    SELECT COUNT(*) INTO NumExists FROM Contract_Signs CS
    WHERE :new.hostid = CS.hostid AND :new.contractid <> CS.contractid
          AND ((:new.fromdate >= CS.fromdate AND :new.fromdate <= CS.todate)
          OR (:new.todate >= CS.fromdate AND :new.todate <= CS.todate));

    IF (NumExists > 0) THEN
        RAISE Overlap_Contract;
    END IF;

EXCEPTION
   WHEN Overlap_Contract THEN
      Raise_application_error (-20300,
         'The contract is overlapped with an existing one');
END;

/

-- special view for posting info
CREATE VIEW PostingInfo(pid, fromdate, todate, hostid, hostname, roomno, residencename, university, dailyrate, description) AS
SELECT P.pid, P.fromdate, P.todate, H.cid, S.name, H.roomno, H.residencename, U.name, H.dailyrate, P.description
FROM Posting P, Hosts H, Student S, University U
WHERE P.hostid = H.cid AND H.cid = S.cid AND S.unid = U.unid;

-- speical view for hosts
CREATE VIEW HostInfo(hostid, hostname, gender, university, roomno, residencename, dailyrate) AS
SELECT S.cid, S.name, S.gender, U.name, H.roomno, H.residencename, H.dailyrate
FROM Student S, Hosts H, University U
WHERE S.cid = H.cid AND S.unid = U.unid;


-- Populate data
insert into University
values(1, 'Hogwarts School of Witchcraft and Wizardry');

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
values(1, 'Harry Potter', 'M', 1, '123456');

insert into Student
values(2, 'Cersei Lannister', 'F', 2, '123456');

insert into Student
values(3, 'Guillaume Lux', 'F', 3, '123456');

insert into Student
values(4, 'Jaap Eris', 'M', 4, '123456');

insert into Student
values(5, 'Kidlat Urbanus', 'F', 5, '123456');

insert into Student
values(6, 'Hannah Abbott', 'F', 6, '123456');

insert into Student
values(7, 'Ludo Bagman', 'M', 7, '123456');

insert into Student
values(8, 'Bathilda Bagshot', 'F', 9, '123456');

insert into Student
values(9, 'Katie Bell', 'F', 8, '123456');

insert into Student
values(10, 'Cuthbert Binns', 'F', 10, '123456');

insert into Student
values(11, 'Charles Leiserson', 'M', 11, '123456');

insert into Student
values(12, 'Robert Floyd', 'M', 12, '123456');

insert into Student
values(13, 'Babara Liskov', 'F', 17, '123456');

insert into Student
values(14, 'Brian Kernighan', 'M', 16, '123456');

insert into Student
values(15, 'Jeffery Dean', 'M', 15, '123456');

insert into Student
values(16, 'Guanyao Fu', 'M', 20, '123456');

insert into Student
values(17, 'Randal Bryant', 'M', 19, '123456');

insert into Student
values(18, 'Ada Lovelace', 'F', 13, '123456');

insert into Student
values(19, 'Marie Currie', 'F', 14, '123456');

insert into Student
values(20, 'Eric Xing', 'M', 18, '123456');

insert into Student
values(21, 'Paul Friedman', 'M', 12, '123456');

insert into Student
values(22, 'David Baker', 'M', 12, '123456');

insert into Student
values(23, 'Richard Stallman', 'M', 12, '123456');

insert into Student
values(24, 'Robert Fakerman', 'M', 12, '123456');

insert into Hosts
values(1, '1024', 'Dragon Crescent', 35);

insert into Hosts
values(2, '106C', 'Student Residence 1', 62);

insert into Hosts
values(3, '237', 'Lincoin Park', 30);

insert into Hosts
values(4, '221', 'Place Vampire', 50);

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
values(10, '157', 'Commericial Drive', 25);

insert into Hosts
values(11, '246B', 'Saint George College', 40);

insert into Hosts
values(12, '5213D', 'Serra Mall', 80);

insert into Hosts
values(13, '307C', 'Berkeley College', 65);

insert into Hosts
values(14, '5152D', 'Forbes House', 42);

insert into Hosts
values(15, '1005', 'Fraser Valley', 38);

insert into Hosts
values(16, '603B', 'Princeton Place', 28);

insert into Hosts
values(17, '221', 'Ithaca Park Residence', 24);

insert into Hosts
values(18, '340', 'Royce College', 24);

insert into Hosts
values(19, '213', 'Haystack Hall', 34);

insert into Hosts
values(20, '121', 'Mellon House', 50);

insert into Hosts
values(21, '420C', 'Maple Castle', 24);

insert into Hosts
values(22, '102', 'Grapehawk Village', 24);

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

insert into Traveler
values(11);

insert into Traveler
values(12);

insert into Traveler
values(13);

insert into Traveler
values(14);

insert into Traveler
values(15);

insert into Traveler
values(16);

insert into Traveler
values(17);

insert into Traveler
values(18);

insert into Traveler
values(19);

insert into Traveler
values(20);

insert into Posting
values(1, '2018-01-01', '2018-01-30', 'a good place to live', 1);

insert into Posting
values(2, '2018-02-01', '2018-02-28', 'cheap clean place', 2);

insert into Posting
values(3, '2018-03-28', '2018-03-30', 'small dorm but nice', 2);

insert into Posting
values(4, '2018-12-01', '2018-12-15', 'small dorm but nice', 2);

insert into Posting
values(5, '2018-05-01', '2018-05-05', 'small dorm but nice', 2);

insert into Posting
values(6, '2018-06-05', '2018-06-08', 'small dorm but nice', 2);

insert into Posting
values(7, '2018-07-04', '2018-07-11', 'small dorm but nice', 2);

insert into Posting
values(8, '2018-08-08', '2018-08-10', 'small dorm but nice', 2);

insert into Posting
values(9, '2018-09-12', '2018-09-22', 'small dorm but nice', 2);

insert into Posting
values(10, '2018-10-11', '2018-10-30', 'small dorm but nice', 2);

insert into Posting
values(11, '2018-11-05', '2018-11-08', 'small dorm but nice', 2);

insert into Posting
values(12, '2018-04-04', '2018-04-30', 'please rent it thanks', 3);

insert into Posting
values(13, '2018-06-05', '2018-06-15', 'nice clean room', 3);

insert into Posting
values(14, '2018-07-02', '2018-07-09', 'welcome to rent my place', 4);

insert into Posting
values(15, '2018-09-05', '2018-09-20', 'cheap but comfortable place', 4);

insert into Posting
values(16, '2018-10-01', '2018-10-30', 'try my place and you will love', 4);

insert into Posting
values(17, '2018-05-06', '2018-05-30', 'best time to rent', 5);

insert into Posting
values(18, '2018-08-10', '2018-08-12', 'best place to live in world', 8);

insert into Posting
values(19, '2019-08-10', '2019-08-12', 'please rent my place', 21);

insert into Posting
values(20, '2019-01-04', '2019-01-08', 'an impolite description', 21);

insert into Host_Reviews
values(2, 1, 5);

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
values(9, 2, 7);

insert into Traveler_Reviews
values(9, 3, 6);

insert into Traveler_Reviews
values(9, 4, 5);

insert into Traveler_Reviews
values(8, 2, 5);

insert into Traveler_Reviews
values(7, 3, 3);

insert into Traveler_Reviews
values(6, 4, 9);

insert into Traveler_Reviews
values(5, 8, 2);

insert into Traveler_Reviews
values(4, 6, 7);

insert into Traveler_Reviews
values(3, 7, 9);

insert into Traveler_Reviews
values(5, 7, 4);

insert into Traveler_Reviews
values(2, 8, 6);

insert into Traveler_Reviews
values(4, 8, 7);

insert into Traveler_Reviews
values(1, 9, 8);

insert into Traveler_Reviews
values(1, 5, 10);

insert into Traveler_Reviews
values(5, 10, 7);

insert into Contract_Signs
values(99, '2019-01-01', '2019-01-30', 2, 1);

insert into Contract_Signs
values(1, '2018-01-01', '2018-01-05', 2, 1);

insert into Contract_Signs
values(2, '2018-01-05', '2018-01-10', 3, 1);

insert into Contract_Signs
values(3, '2018-01-11', '2018-01-16', 4, 1);

insert into Contract_Signs
values(4, '2018-02-01', '2018-02-04', 5, 1);

insert into Contract_Signs
values(5, '2018-03-10', '2018-03-15', 6, 1);

insert into Contract_Signs
values(6, '2018-04-01', '2018-04-08', 7, 1);

insert into Contract_Signs
values(7, '2018-04-10', '2018-04-15', 8, 1);

insert into Contract_Signs
values(8, '2018-05-01', '2018-05-25', 9, 1);

insert into Contract_Signs
values(9, '2018-06-01', '2018-06-22', 10, 1);

insert into Contract_Signs
values(10, '2017-01-01', '2017-01-30', 11, 1);

insert into Contract_Signs
values(11, '2017-01-01', '2017-01-30', 12, 1);

insert into Contract_Signs
values(12, '2016-01-01', '2016-01-30', 13, 1);

insert into Contract_Signs
values(13, '2015-01-01', '2015-01-30', 14, 1);

insert into Contract_Signs
values(14, '2014-01-01', '2014-01-30', 15, 1);

insert into Contract_Signs
values(15, '2013-01-01', '2013-01-30', 16, 1);

insert into Contract_Signs
values(16, '2012-01-01', '2012-01-30', 17, 1);

insert into Contract_Signs
values(17, '2011-01-01', '2011-01-30', 18, 1);

insert into Contract_Signs
values(18, '2014-04-03', '2014-04-25', 19, 1);

insert into Contract_Signs
values(19, '2016-08-11', '2016-08-30', 20, 1);

insert into Contract_Signs
values(20, '2018-01-01', '2018-01-30', 1, 2);

insert into Contract_Signs
values(21, '2018-02-05', '2018-02-18', 3, 2);

insert into Contract_Signs
values(22, '2018-02-05', '2018-02-08', 4, 2);

insert into Contract_Signs
values(23, '2018-02-05', '2018-02-10', 5, 2);

insert into Contract_Signs
values(24, '2018-02-11', '2018-02-13', 6, 2);

insert into Contract_Signs
values(25, '2018-03-01', '2018-03-08', 7, 2);

insert into Contract_Signs
values(26, '2018-02-15', '2018-02-22', 8, 2);

insert into Contract_Signs
values(27, '2018-02-15', '2018-02-22', 9, 2);

insert into Contract_Signs
values(28, '2018-02-15', '2018-02-22', 10, 2);

insert into Contract_Signs
values(29, '2018-02-15', '2018-02-22', 11, 2);

insert into Contract_Signs
values(30, '2018-02-15', '2018-02-22', 12, 2);

insert into Contract_Signs
values(31, '2018-02-15', '2018-02-22', 13, 2);

insert into Contract_Signs
values(32, '2018-02-15', '2018-02-22', 14, 2);

insert into Contract_Signs
values(33, '2018-05-04', '2018-05-08', 15, 2);

insert into Contract_Signs
values(34, '2018-10-01', '2018-10-05', 16, 2);

insert into Contract_Signs
values(35, '2018-10-01', '2018-10-05', 17, 2);

insert into Contract_Signs
values(36, '2018-10-01', '2018-10-05', 18, 2);

insert into Contract_Signs
values(37, '2018-10-01', '2018-10-05', 19, 2);



