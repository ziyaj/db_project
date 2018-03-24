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
SELECT P.pid, P.fromdate, P.todate, S.name, R.roomno, R.residencename, R.gender, U.name, R.daily_rate
FROM Posting P, Hosts H, Student S, Residence R, University U
WHERE P.hostid = H.cid AND H.cid = S.cid AND S.unid = U.unid
      AND H.rid = R.rid;

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
