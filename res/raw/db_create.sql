PRAGMA foreign_keys = ON;
CREATE TABLE User
(
	name TEXT NOT NULL
	,email TEXT PRIMARY KEY
	,password TEXT NOT NULL
);
CREATE TABLE Locations
(
	name TEXT PRIMARY KEY
	,capacity INTEGER NOT NULL
);
INSERT INTO User (name, email, password)
VALUES ('wim','wim@wim.com','wim');
INSERT INTO Locations (name, capacity)
VALUES ('megazaal', 100);
INSERT INTO Locations (name, capacity)
VALUES ('pizzazaal', 20);