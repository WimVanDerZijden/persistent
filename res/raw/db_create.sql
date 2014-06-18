PRAGMA foreign_keys = ON;
DROP TABLE IF EXISTS Invite;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Locations;


CREATE TABLE User
(
	name TEXT NOT NULL
	,email TEXT NOT NULL UNIQUE
	,password TEXT NOT NULL
	,photo BLOB
	,id INTEGER PRIMARY KEY AUTOINCREMENT
);

CREATE TABLE Locations
(
	id INTEGER PRIMARY KEY AUTOINCREMENT
	,name TEXT UNIQUE
	,capacity INTEGER NOT NULL
);
CREATE TABLE Event 
(
	id INTEGER PRIMARY KEY AUTOINCREMENT
	,name TEXT NOT NULL 
	,datetime INTEGER NOT NULL
	,duration INTEGER NOT NULL
	,maxparticipants INTEGER
	,minparticipants INTEGER
	,description TEXT
	,datetime1 INTEGER
	,datetime2 INTEGER
	,datetime3 INTEGER
	,user_id INTEGER NOT NULL
	,location_id INTEGER NOT NULL
	,FOREIGN KEY (user_id) REFERENCES User(id)
	,FOREIGN KEY (location_id) REFERENCES Location(id)
);

CREATE TABLE Invite
(
	id INTEGER PRIMARY KEY AUTOINCREMENT
	,user_id INTEGER NOT NULL
	,event_id INTEGER NOT NULL
	,datetime INTEGER NOT NULL DEFAULT 
	,isAccepted INTEGER NOT NULL DEFAULT 1
	,isRejected INTEGER NOT NULL DEFAULT 0
	,FOREIGN KEY (user_id) REFERENCES User(id)
	,FOREIGN KEY (event_id) REFERENCES Event(id)
	,UNIQUE(user_id, event_id)
);

INSERT INTO User (id, name, email, password) VALUES
(1, 'wim','wim@wim.com','wim');
INSERT INTO User (id, name, email, password) VALUES
(2, 'Test Account','@','q');
INSERT INTO Locations (id, name, capacity)
VALUES (1, 'pizzazaal', 20);
INSERT INTO Locations (id, name, capacity)
VALUES (2, 'Extra Zaal', 50);
INSERT INTO Locations (id, name, capacity)
VALUES (3, 'megazaal', 100);

INSERT INTO event(name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
('voetbalsessie',CURRENT_TIMESTAMP,60,20,6,'We willen graag minimaal 6 personen en kom optijd',1,1);
