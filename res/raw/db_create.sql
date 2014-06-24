PRAGMA foreign_keys = ON;
DROP TABLE IF EXISTS Invite;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Locations;

DROP VIEW IF EXISTS Event_v;

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
	,FOREIGN KEY (location_id) REFERENCES Locations(id)
);

CREATE TABLE Invite
(
	id INTEGER PRIMARY KEY AUTOINCREMENT
	,user_id INTEGER NOT NULL
	,event_id INTEGER NOT NULL
	,datetime INTEGER NOT NULL
	,isAccepted INTEGER NOT NULL DEFAULT 1
	,isRejected INTEGER NOT NULL DEFAULT 0
	,FOREIGN KEY (user_id) REFERENCES User(id)
	,FOREIGN KEY (event_id) REFERENCES Event(id)
	,UNIQUE(user_id, event_id)
);

CREATE VIEW Event_v AS
SELECT e.name, e.datetime, e.duration, e.maxparticipants, e.minparticipants, e.description, e.datetime1, e.datetime2, e.datetime3, e.id, e.user_id, e.location_id, 
COUNT(i.id) AS registered
FROM Event e
LEFT OUTER JOIN Invite i ON i.event_id = e.id
GROUP BY e.name, e.datetime, e.duration, e.maxparticipants, e.minparticipants, e.description, e.datetime1, e.datetime2, e.datetime3, e.id, e.user_id, e.location_id;

INSERT INTO User (id, name, email, password) VALUES
(1, 'wim','wim@wim.com','wim');
INSERT INTO User (id, name, email, password) VALUES
(2, 'Test Account','@','q');

INSERT INTO Locations (id, name, capacity)
VALUES (1, 'Mondriaan', 60);
INSERT INTO Locations (id, name, capacity)
VALUES (2, 'Appel', 50);
INSERT INTO Locations (id, name, capacity)
VALUES (3, 'Potter', 6);
INSERT INTO Locations (id, name, capacity)
VALUES (4, 'Pieck', 10);
INSERT INTO Locations (id, name, capacity)
VALUES (5, 'Van Gogh', 20);
INSERT INTO Locations (id, name, capacity)
VALUES (6, 'Huisman', 5);
INSERT INTO Locations (id, name, capacity)
VALUES (7, 'Corneille', 10);
INSERT INTO Locations (id, name, capacity)
VALUES (8, 'Escher', 100);


INSERT INTO event(id,name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
(1,'Voetbal', 1406030400000,60 * 60 * 1000,12,6,'Gezellig potje voetbal',1,1);
INSERT INTO event(id,name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
(2,'Social Media',1407030400000,60 * 60 * 1000,50,10,'Interessante lezing over social media impact',1,2);
INSERT INTO event(id,name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
(3,'Cloud Computing',1408030400000,60 * 60 * 1000,20,6,'Trend of hype?',1,2);
INSERT INTO event(id,name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
(4,'Robotics',1409030400000,60 * 60 * 1000,10,2,'Robotics: nieuwe toepassingen',1,3);
INSERT INTO event(id,name,datetime,duration,maxparticipants,minparticipants,description,user_id,location_id) VALUES
(5,'Telematica',1410030400000,60 * 60 * 1000,50,5,'Nieuwe uitdagingen op het gebied van Telematica',1,3);
