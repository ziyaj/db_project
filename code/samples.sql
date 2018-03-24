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