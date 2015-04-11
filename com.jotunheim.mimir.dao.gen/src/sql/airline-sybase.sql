DROP TABLE reservations
go
DROP TABLE flights
go
DROP TABLE persons
go

CREATE TABLE persons(
 person_id INT NOT NULL,
 first_name VARCHAR(32) NOT NULL,
 last_name VARCHAR(32) NOT NULL,
 PRIMARY KEY( person_id )
)
go

CREATE TABLE flights(
 flight_id INT NOT NULL,
 name VARCHAR(32) NOT NULL,
 departure_utc DATETIME NOT NULL,
 arrival_utc DATETIME NOT NULL,
 PRIMARY KEY( flight_id )
)
go

CREATE TABLE reservations(
 reservation_id INT NOT NULL,
 person_id_fk INT NOT NULL,
 flight_id_fk INT NOT NULL,
 registration_utc DATETIME NOT NULL,
 comment TEXT,
 PRIMARY KEY(reservation_id,person_id_fk,flight_id_fk),
 FOREIGN KEY (person_id_fk) REFERENCES persons(person_id),
 FOREIGN KEY (flight_id_fk) REFERENCES flights(flight_id)
)
go

INSERT INTO persons VALUES(1,'Aslak','Hellesøy')
go
INSERT INTO persons VALUES(2,'Jesus','Christ')
go
INSERT INTO persons VALUES(3,'Allah','Akhbar')
go

INSERT INTO flights VALUES(1,'SK000','1971-02-28 23:45:00.0','2002-02-14 16:51:00.0' )
go
INSERT INTO flights VALUES(2,'HV000','1901-12-24 00:00:00.0','1935-06-19 21:07:00.0' )
go
INSERT INTO flights VALUES(3,'MY000','1834-02-28 23:45:00.0','2005-02-14 16:51:00.0' )
go

SELECT * FROM persons
go
