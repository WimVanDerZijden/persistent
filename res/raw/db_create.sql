PRAGMA foreign_keys = ON;
DROP TABLE User;
DROP TABLE Locations;
DROP TABLE Event;

CREATE TABLE User
(
	name TEXT NOT NULL
	,email TEXT PRIMARY KEY
	,password TEXT NOT NULL
	,photo BLOB
);
CREATE TABLE Locations
(
	name TEXT PRIMARY KEY
	,capacity INTEGER NOT NULL
);
CREATE TABLE Event 
(
	id INTEGER PRIMARY KEY AUTOINCREMENT
	,name TEXT NOT NULL 
	,datetime INTEGER 
	,duration INTEGER 
	,maxparticipants INTEGER
	,minparticipants INTEGER
	,description TEXT
	,datetime1 INTEGER
	,datetime2 INTEGER
	,datetime3 INTEGER
);


INSERT INTO User (name, email, password) VALUES
('wim','wim@wim.com','wim');
INSERT INTO User (name, email, password) VALUES
('Test Account','@','q');
INSERT INTO Locations (name, capacity)
VALUES ('megazaal', 100);
INSERT INTO Locations (name, capacity)
VALUES ('pizzazaal', 20);
INSERT INTO Locations (name, capacity)
VALUES ('Extra Zaal', 50);

INSERT INTO event(name) VALUES
('Pizzasessie');
INSERT INTO event(name,duration,maxparicipants,minparticipants,description,datetime1,datetime2,datetime3) VALUES
('voetbalsessie',60,20,6,'We willen graag minimaal 6 personen en kom optijd',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP+1000000,CURRENT_TIMESTAMP+5000000);
