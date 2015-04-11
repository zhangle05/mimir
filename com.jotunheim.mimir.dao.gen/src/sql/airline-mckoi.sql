CREATE SCHEMA spantax;
SET SCHEMA spantax;

CREATE TABLE persons(
 person_id INT NOT NULL,
 first_name VARCHAR NOT NULL,
 last_name VARCHAR NOT NULL,
 PRIMARY KEY( person_id )
);

CREATE TABLE flights(
 flight_id INT NOT NULL,
 name VARCHAR(32) NOT NULL,
 departure_utc TIMESTAMP NOT NULL,
 arrival_utc TIMESTAMP NOT NULL,
 PRIMARY KEY( flight_id )
);

CREATE TABLE reservations(
 reservation_id INT NOT NULL,
 person_id_fk INT NOT NULL,
 flight_id_fk INT NOT NULL,
 registration_utc TIMESTAMP NOT NULL,
 comment VARCHAR,
 PRIMARY KEY(reservation_id,person_id_fk,flight_id_fk),
 FOREIGN KEY (person_id_fk) REFERENCES persons(person_id),
 FOREIGN KEY (flight_id_fk) REFERENCES flights(flight_id)
);

INSERT INTO persons VALUES(1,'Aslak','Hellesøy');
INSERT INTO persons VALUES(2,'Eivind','Waaler');
INSERT INTO persons VALUES(3,'Ludovic','Claude');
