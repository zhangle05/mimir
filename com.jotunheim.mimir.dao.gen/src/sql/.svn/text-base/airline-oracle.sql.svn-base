--
-- This complicated drop code fragment is to 
-- fulfill ant's target 'create-database' 
--
-- a simple drop table won't work if this script is run the
-- first time. Ant's sql-Task will be choked.
--
-- in mysql it would be done by the clause 'if exists'
-- but oracle does not provide such option
-- 
-- FIXME: This does currently not work, if anyone knows
-- how to get it right, just go ahead and let me know.
-- Thanks, Marian
--
-- CREATE OR REPLACE PROCEDURE drop_table (tbl_nm IN VARCHAR2) 
-- IS
--  TABLE_NOT_EXISTS EXCEPTION;
--  PRAGMA EXCEPTION_INIT( TABLE_NOT_EXISTS, -942 );
--  vc_drop     VARCHAR2( 4000 ) := NULL;
-- BEGIN
--  vc_drop := 'DROP TABLE '||tbl_nm;
--  BEGIN
--   DBMS_OUTPUT.put_line('Executing ' || vc_drop);
--   EXECUTE IMMEDIATE vc_drop;
--  EXCEPTION
--   WHEN TABLE_NOT_EXISTS THEN /* ignore */
--    NULL;
--   WHEN OTHERS THEN  /* raise to upper */
--    DBMS_OUTPUT.put_line( vc_drop || ' failed!' );
--    RAISE;
--  END;
-- END;

--EXECUTE drop_table('RESERVATIONS');
--EXECUTE drop_table('FLIGHTS');
--EXECUTE drop_table('PERSONS');

DROP TABLE RESERVATIONS;
DROP TABLE FLIGHTS;
DROP TABLE PERSONS;

--PROMPT Creating Table 'PERSONS'
CREATE TABLE PERSONS(
 PERSON_ID  NUMBER(8)    NOT NULL,
 FIRST_NAME VARCHAR2(32) NOT NULL,
 LAST_NAME  VARCHAR2(32) NOT NULL
);


--PROMPT Creating Table 'FLIGHTS'
CREATE TABLE FLIGHTS(
 FLIGHT_ID     NUMBER(8)    NOT NULL,
 NAME          VARCHAR2(32) NOT NULL,
 DEPARTURE_UTC DATE         NOT NULL,
 ARRIVAL_UTC   DATE         NOT NULL
);


--PROMPT Creating Table 'RESERVATIONS'
CREATE TABLE RESERVATIONS(
 RESERVATION_ID   NUMBER(8)     NOT NULL,
 PERSON_ID        NUMBER(8)     NOT NULL,
 FLIGHT_ID        NUMBER(8)     NOT NULL,
 REGISTRATION_UTC DATE          NOT NULL,
 COMMENTARY       VARCHAR2(200) NULL
);


--PROMPT Creating Table 'SEQ_BLOCK'
--CREATE TABLE SEQ_BLOCK(
-- IDX  NUMBER(8)    NOT NULL,
-- NAME VARCHAR2(50) NOT NULL
--);

--PROMPT Creating Primary Key on 'PERSONS'
ALTER TABLE PERSONS
 ADD CONSTRAINT PERSON_PK PRIMARY KEY (PERSON_ID);

--PROMPT Creating Primary Key on 'FLIGHTS'
ALTER TABLE FLIGHTS
 ADD CONSTRAINT FLIGHT_PK PRIMARY KEY (FLIGHT_ID);


--PROMPT Creating Primary Key on 'RESERVATIONS'
ALTER TABLE RESERVATIONS
 ADD CONSTRAINT RESERVATION_PK PRIMARY KEY 
  (RESERVATION_ID, 
   PERSON_ID, 
   FLIGHT_ID);

--PROMPT Creating Foreign Keys on 'RESERVATIONS'
ALTER TABLE RESERVATIONS 
 ADD CONSTRAINT FLIGHT_ID_FK FOREIGN KEY (FLIGHT_ID) REFERENCES FLIGHTS
 ADD CONSTRAINT PERSON_ID_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSONS;

INSERT INTO PERSONS VALUES(1,'Aslak','Hellesøy');
INSERT INTO PERSONS VALUES(2,'Eivind','Waaler');
INSERT INTO PERSONS VALUES(3,'Ludovic','Claude');
