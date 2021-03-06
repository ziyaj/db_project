-- set linesize 2000
-- set wrap off

-- LOGIN --
-- check password
SELECT S.cid
FROM Student S
WHERE S.cid = 1 AND S.password = "123456";

-- AS TRAVELER --

-- T0. Traveler login
-- check if the cid exists for the traveler
SELECT T.cid
FROM Student S, Traveler T
WHERE T.cid = S.cid AND T.cid = 1;

-- T1. can find postings with
--     I. conditions:
--        a) university
--        b) within date range (fromdate <= STARTDATE AND todate >= ENDDATE)
--        c) daily rate range (daily rate BETWEEN AMT1 AND AMT2)
--    II. display options: pid, description [must display]
--        a) date range (fromdate + todate)
--        b) host name
--        c) address (roomno + residencename)
--        d) university
--        f) dailyrate
SELECT PI.pid, PI.fromdate, PI.todate, PI.hostname, PI.roomno, PI.residencename, PI.university, PI.dailyrate
FROM PostingInfo PI
WHERE PI.university LIKE '%Toronto%'
      AND PI.fromdate >= '2018-01-01' AND PI.todate <= '2018-01-30'
      AND PI.dailyrate BETWEEN 20 AND 40;

-- T2. can view the cheapest/most expensive postings
SELECT *
FROM PostingInfo PI
WHERE PI.dailyrate = (SELECT MIN(dailyrate)
                      FROM PostingInfo);

SELECT *
FROM PostingInfo PI
WHERE PI.dailyrate = (SELECT MAX(dailyrate)
                      FROM PostingInfo);

-- T3. can sign a contract
SELECT PI.hostid, PI.fromdate, PI.todate
FROM PostingInfo PI
WHERE PI.pid = 1;

INSERT INTO Contract_Signs
VALUES(55, '2018-03-25', '2018-03-30', 5, 2);

-- T4. can view his or her contract
SELECT CS.contractid, CS.fromdate, CS.todate, HI.hostid, HI.hostname, HI.university, HI.roomno, HI.residencename
FROM Contract_Signs CS, HostInfo HI
WHERE CS.travelerid = 1 AND CS.hostid = HI.hostid;

-- T5. can cancel his or her contract
-- first of all, check whether the contract is in the future
SELECT CS.fromdate
FROM Contract_Signs CS
WHERE CS.contractid = 1;
-- if the contract is in the future, then the traveler can delete it
DELETE
FROM Contract_Signs CS
WHERE CS.contractid = 1;

-- T6. can review a host
-- if there is already a review existing, the review will be updated
-- if there is no review existing, a new review will be created
INSERT INTO Traveler_Reviews
VALUES(3, 5, 4);

-- AS HOST --

-- H0. Host login
SELECT H.cid
FROM Student S, Hosts H
WHERE H.cid = S.cid AND H.cid = 1;

-- H1. can make a post
INSERT INTO Posting
VALUES(63, '2018-03-20', '2018-04-05', 'clean, neat residence with cheap rate', 3);

-- H2. can see his own posts
SELECT PI.pid, PI.fromdate, PI.todate, PI.roomno, PI.residencename, PI.dailyrate, PI.description
FROM PostingInfo PI
WHERE PI.hostid = 1;

-- H3. can update his post
--- if fromdate > todate, or todate < fromdate, the update should fail
UPDATE Posting
SET fromdate = '2018-01-05', description = 'a great place to live'
WHERE pid = 1;

-- H4. can see all his contracts
SELECT CS.contractid, CS.fromdate, CS.todate, CS.travelerid, S.name, U.name
FROM Contract_Signs CS, Traveler T, Student S, University U
WHERE CS.hostid = 1 AND T.cid = CS.travelerid AND T.cid = S.cid AND S.unid = U.unid;

-- H5. can see all the ratings the host has done
SELECT HR.travelerid, S.name ,HR.rating
FROM Host_Reviews HR, Traveler T, Student S
WHERE HR.hostid = 1 AND HR.travelerid = T.cid AND T.cid = S.cid;

-- H6. can review a traveler
-- if the reivew exists, do an update
SELECT COUNT(*)
FROM Host_Reviews
WHERE hostid = 1 AND travelerid = 2;
-- other wise do an insert travelerid, hostid, rating
INSERT INTO Host_Reviews
VALUES(1, 8, 5);

-- H7. can delete his post
DELETE
FROM Posting P
WHERE P.pid = 1;

-- AS ADMIN --
-- A1. can see hosts with contracts
SELECT HI.hostid, COUNT(CS.contractid)
FROM HostInfo HI LEFT OUTER JOIN Contract_Signs CS ON HI.hostid = CS.hostid
GROUP BY HI.hostid
ORDER BY HI.hostid;

-- A2. can find highest/lowest rated hosts
WITH HostRating(hostid, rating) AS
     (SELECT TR.hostid, AVG(TR.rating)
      FROM Traveler_Reviews TR
      GROUP BY TR.hostid)
SELECT HI.hostid, HI.hostname, HI.university, HR.rating
FROM HostInfo HI, HostRating HR
WHERE HI.hostid = HR.hostid
      AND HR.rating = (SELECT MAX(rating)
                       FROM HostRating);

WITH HostRating(hostid, rating) AS
     (SELECT TR.hostid, AVG(TR.rating)
      FROM Traveler_Reviews TR
      GROUP BY TR.hostid)
SELECT HI.hostid, HI.hostname, HI.university, HR.rating
FROM HostInfo HI, HostRating HR
WHERE HI.hostid = HR.hostid
      AND HR.rating = (SELECT MIN(rating)
                       FROM HostRating);

-- A3. can delete a host
--    a) If the host does not have contract, posts, reviews, delete the host without cascade
--    b) If the host does not have contract but has posts, and reviews, deleting the host will cascade on posts and reviews
--    c) If the host has a current contract, then the deletion will be blocked
DELETE
FROM Hosts H
WHERE H.cid = 21;

-- A4. can find travelers who have been to every university
SELECT S.cid, S.name
FROM Traveler T, Student S
WHERE T.cid = S.cid
AND NOT EXISTS
    ((SELECT U1.unid
      FROM University U1
      WHERE U1.unid NOT IN (SELECT U2.unid
                            FROM University U2
                            WHERE S.unid = U2.unid))
      MINUS
     (SELECT U3.unid
      FROM Contract_Signs CS, Hosts H, Student S2, University U3
      WHERE CS.travelerid = T.cid AND CS.hostid = H.cid
            AND H.cid = S2.cid AND S2.unid = U3.unid));

-- A5. can find all hosts
SELECT *
FROM HostInfo;

-- A6. can find all travelers
SELECT S.cid, S.name, S.gender, U.name AS UNIVERSITY
FROM Traveler T, Student S, University U
WHERE T.cid = S.cid AND S.unid = U.unid;

