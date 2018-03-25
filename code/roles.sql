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



-- AS HOST --


-- AS ADMIN --
-- 1. can delete a host
--    a) If the host does not have contract, then he/she can be deleted
--    b) If the host has a contracct, then he/she cannot be deleted
DELETE
FROM Hosts H
WHERE H.cid = 21;