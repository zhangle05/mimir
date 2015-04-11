// The quotes are just a workaround to make hsqldb use lowercase

CREATE TABLE "persons"(
 "person_id" INT NOT NULL,
 "first_name" VARCHAR NOT NULL,
 "last_name" VARCHAR NOT NULL,
 PRIMARY KEY( "person_id" )
);

CREATE TABLE "flights"(
 "flight_id" INT NOT NULL,
 "name" VARCHAR(32) NOT NULL,
 "departure_utc" DATETIME NOT NULL,
 "arrival_utc" DATETIME NOT NULL,
 PRIMARY KEY( "flight_id" )
);

CREATE TABLE "reservations"(
 "reservation_id" INT NOT NULL,
 "person_id_fk" INT NOT NULL,
 "flight_id_fk" INT NOT NULL,
 "registration_utc" DATETIME NOT NULL,
 "comment" VARCHAR,
 PRIMARY KEY ("reservation_id","person_id_fk","flight_id_fk"),
 FOREIGN KEY ("person_id_fk") REFERENCES "persons"("person_id"),
 FOREIGN KEY ("flight_id_fk") REFERENCES "flights"("flight_id")
);

// insert the persons
INSERT INTO "persons" VALUES( 1, 'Aslak', 'Helles�y' );
INSERT INTO "persons" VALUES( 2, 'Eivind', 'Waaler' );
INSERT INTO "persons" VALUES( 3, 'Ludovic', 'Claude' );

// this is just to test the indexed predicate
CREATE INDEX "res_reg_utc" ON "reservations"("registration_utc");
