DROP TABLE IF EXISTS game;

CREATE TABLE game (
  id INTEGER AUTO_INCREMENT  PRIMARY KEY,
  player VARCHAR(250),
  score INTEGER NOT NULL CHECK ("SCORE">=1),
  time TIMESTAMP  NOT NULL
);