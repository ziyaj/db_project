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
SELECT S.cid, S.name, S.gender, U.name, H.roomno, H.residencename, H.daily_rate
FROM Student S, Hosts H, University U
WHERE S.cid = Hosts.cid AND S.unid = U.unid;