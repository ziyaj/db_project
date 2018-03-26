-- set linesize 2000
-- set wrap off

-- AS TRAVELER --

-- T1. can find postings with
--     I. conditions:
--        a) university
--        b) within date range (fromdate <= STARTDATE AND todate >= ENDDATE)
--        c) daily rate range (daily rate BETWEEN AMT1 AND AMT2)
--    II. display options:
--        a) pid
--        b) date range (fromdate + todate)
--        c) host name
--        d) address (roomno + residencename)
--        e) university
--        f) dailyrate
SELECT PI.pid, PI.fromdate, PI.todate, PI.hostname, PI.roomno, PI.residencename, PI.university, PI.dailyrate
FROM PostingInfo PI
WHERE PI.university LIKE '%British Columbia%'
      AND PI.fromdate <= '2018-01-01' AND PI.todate >= '2018-01-30'
      AND PI.dailyrate BETWEEN 20 AND 40;

-- T2. can view the cheapest/most expensive postings
SELECT *
FROM PostingInfo PI
WHERE PI.dailyrate = (SELECT MIN(dailyrate)
                      FROM PostingInfo);

-- T3. can sign a contract
INSERT INTO Contract_Signs
VALUES(55, '2018-03-25', '2018-03-30', 5, 2);

-- T4. can view his or her contract
SELECT CS.contractid, CS.fromdate, CS.todate, HI.hostid, HI.hostname, HI.university, HI.roomno, HI.residencename
FROM Contract_Signs CS, HostInfo HI
WHERE CS.travelerid = 1 AND CS.hostid = HI.hostid;

-- T5. can cancel his or her contract
DELETE
FROM Contract_Signs CS
WHERE CS.pid = 1;

-- T6. can review a host he hasn't reviewed before, or update his or her reviews
-- can see his reviewable hosts
-- SELECT *
-- FROM HostInfo HI
-- WHERE HI.hostid IN (SELECT CS.hostid
--                     FROM Contract_Signs CS
--                     WHERE CS.travelerid = 1
--                     MINUS
--                     SELECT TR.hostid
--                     FROM Traveler_Reviews TR
--                     WHERE TR.travelerid = 1);
INSERT INTO Traveler_Reviews
VALUES(3, 5, 4);


-- AS HOST --
-- H1. can make a post
INSERT INTO Posting
VALUES(63, '2018-03-20', '2018-04-05', 'clean, neat residence with cheap rate', 3);

-- H2. can see his own posts
SELECT PI.pid, PI.fromdate, PI.todate, PI.roomno, PI.residencename, PI.dailyrate, PI.description
FROM PostingInfo PI
WHERE PI.hostid = 1;

-- H3. can update his post
---    if fromdate > todate, or todate < fromdate, the update should fail
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
SELECT HI.hostid, HI.hostname, HI.university, COUNT(CS.contractid)
FROM HostInfo HI, Contract_Signs CS
WHERE HI.hostid = CS.hostid
GROUP BY HI.hostid
HAVING COUNT(CS.contractid) > 0;

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

