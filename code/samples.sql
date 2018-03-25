-- sample querys
-- set linesize 2000
-- set wrap off

-- 1. Selection and projection query
SELECT *
FROM Student
WHERE cid = 1;

SELECT *
FROM Student
WHERE name = 'Harry Potter';

-- 2. Join query, see all posts with host's name
SELECT P.pid, P.fromdate, P.todate, H.cid, S.name, H.roomno, H.residencename, U.name, H.daily_rate
FROM Posting P, Hosts H, Student S, University U
WHERE P.hostid = H.cid AND H.cid = S.cid AND S.unid = U.unid;

CREATE VIEW PostingInfo(pid, fromdate, todate, hostid, hostname, roomno, residencename, university, dailyrate) AS
SELECT P.pid, P.fromdate, P.todate, H.cid, S.name, H.roomno, H.residencename, U.name, H.daily_rate
FROM Posting P, Hosts H, Student S, University U
WHERE P.hostid = H.cid AND H.cid = S.cid AND S.unid = U.unid;


-- 3. Division query
-- find travellers who have been to every university (besides his or her own university)
-- !!! Orcale syntax MINUS means EXCEPT
SELECT S.cid, S.name, S.is_active
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
      WHERE CS.traveler_id = T.cid AND CS.hostid = H.cid AND CS.is_cancelled <> 1
            AND H.cid = S2.cid AND S2.unid = U3.unid));


-- 4. Aggregation query
-- Does this one make much sense?
-- Find the average/max/min rating of all hosts
SELECT AVG(TR.rating)
FROM Traveler_Reviews TR;

SELECT MAX(TR.rating)
FROM Traveler_Reviews TR;

SELECT MIN(TR.rating)
FROM Traveler_Reviews TR;

-- select the cheapest posts
SELECT *
FROM PostingInfo PI
WHERE PI.dailyrate = (SELECT MIN(dailyrate)
                      FROM PostingInfo);

-- 5. Nested Aggregation with group-by
-- Traveler: find heighest rating posts

-- display hostid, hostname, and rating
CREATE VIEW HostRating(hostid, hostname, rating) AS
SELECT H.cid, S.name, HR.rating
FROM Hosts H, Student S, (SELECT TR.hostid AS id, AVG(TR.rating) AS rating
                          FROM Traveler_Reviews TR, Hosts H
                          WHERE TR.hostid = H.cid
                          GROUP BY TR.hostid) HR
WHERE H.cid = S.cid AND H.cid = HR.id;


-- select hosts with highest ratings
SELECT HR.hostid, HR.hostname, HR.rating
FROM HostRating HR
WHERE HR.rating = (SELECT MAX(rating)
                   FROM HostRating);

-- select highest rated postings
SELECT *
FROM PostingInfo PI
WHERE PI.pid IN (SELECT P.pid
                 FROM HostRating HR, Posting P
                 WHERE HR.hostid = P.hostid AND HR.rating = (SELECT MAX(rating)
                                                             FROM HostRating));

-- 6. delete operation
-- a host can view his posts
SELECT *
FROM PostingInfo PI
WHERE PI.hostid = 1;
-- host can delete a post


