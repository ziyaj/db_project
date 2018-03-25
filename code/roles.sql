-- AS TRAVELER --
-- 1. can find a host with
--    I. conditions:
--       a) the host's cid
--       b) the host's name
--       c) the host's gender
--       d) the host's university
--    I. display options:
--       a) cid
--       b) name
--       c) gender
--       d) university
--       e) address (roomno + residencename)
SELECT HI.hostid, HI.hostname, HI.gender, HI.university, HI.roomno, HI.residencename
FROM HostInfo HI
WHERE HI.hostid = 14 AND HI.hostname LIKE '%Brian%'
	  AND HI.gender = 'M'
      AND HI.univeristy LIKE '%Princeton%';
