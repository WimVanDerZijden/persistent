PRAGMA foreign_keys = ON;
CREATE TABLE User
(
	name TEXT NOT NULL
	,email TEXT PRIMARY KEY
	,password TEXT NOT NULL
);
INSERT INTO User (name, email, password)
VALUES ('wim','wim@wim.com','wim');