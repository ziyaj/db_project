== Short descriptions of project ==

We designed and implemented an application for university students to exchange eaach other's residence.

== Accomplishment ==

a) Our application enables users to log in as traveler, host or Administrator. Users can also register as a host.
b) After successfully loging in, travellers can 
   - Find postings with different criteria
   - Sign/Cancel contracts 
   - Rate Hosts  
c) After successfully loging in, Hosts can 
   - Make a post
   - Manage his own posts
   - See his own contracts
   - Rate a traveler
d) After successfully loging in, Administrator can do basic management tasks such as 
   - Browsing host/traveller info, 
   - Search hosts 
   - Delete hosts by ID
   - Award travelers who has have been to every universities


== Changes in database schema ==

a) We merged "Residence" with "Host" because we assume that each host, who is a university student, can only live in one place. 
So "Host" and "Residence" have a one-to-one relationship. 
In order to simplify database design, we deicided to merge "Residence" to "Host".

b) We removed "credit" and "is_active" attributes from "Student" relation because "is_active" provides extra complexity 
in query impelmentation, and due to the limited amount of time, we decided not to deal with "credit" and money associated 
with it.

c) We removed "is_cancelled" from "Contract" because that simplifies the query design and if a contract is cancelled, 
it should no longer be stored due to limited amount of storage space. There is really no good reason to store the 
unwanted contracts.

d) We changed "postal_code", "address", "link", "gender" in "Residence" to "roomno", "dailyrate", 
"residencename" in "Host" because the new schema better illustrates on-campus residence for university students, 
which better suits the application.

e) We renamed the attributes that used to use "_" with no symbol because some of our attributes 
turned out to be keywords in SQL, so we renamed them to avoid keywords. 
Also we renamed "Host" to "Hosts" because "Host" is also a keyword.

In general, we simplified the database to ease the design and implementation of SQL queries, 
such that our query is not too complicated that burdens performance penalty.

