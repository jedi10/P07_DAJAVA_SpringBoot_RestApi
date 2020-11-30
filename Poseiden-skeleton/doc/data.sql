
CREATE TABLE BidList (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bidQuantity DOUBLE,
  askQuantity DOUBLE,
  bid DOUBLE ,
  ask DOUBLE,
  benchmark VARCHAR(125),
  bidListDate TIMESTAMP DEFAULT current_timestamp(),
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creationName VARCHAR(125),
  creationDate TIMESTAMP DEFAULT current_timestamp(),
  revisionName VARCHAR(125),
  revisionDate TIMESTAMP DEFAULT current_timestamp(),
  dealName VARCHAR(125),
  dealType VARCHAR(125),
  sourceListId VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Trade (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buyQuantity DOUBLE,
  sellQuantity DOUBLE,
  buyPrice DOUBLE ,
  sellPrice DOUBLE,
  tradeDate TIMESTAMP DEFAULT current_timestamp(),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creationName VARCHAR(125),
  creationDate TIMESTAMP DEFAULT current_timestamp(),
  revisionName VARCHAR(125),
  revisionDate TIMESTAMP DEFAULT current_timestamp(),
  dealName VARCHAR(125),
  dealType VARCHAR(125),
  sourceListId VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE CurvePoint (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  CurveId tinyint,
  asOfDate TIMESTAMP DEFAULT current_timestamp(),
  term DOUBLE ,
  value DOUBLE ,
  creationDate TIMESTAMP DEFAULT current_timestamp(),

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Rating (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  moodysRating VARCHAR(125),
  sandPRating VARCHAR(125),
  fitchRating VARCHAR(125),
  orderNumber tinyint,

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE RuleName (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sqlStr VARCHAR(125),
  sqlPart VARCHAR(125),

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Users (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(125),
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),

  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into Users(fullname, username, password, role) values("Administrator", "admin", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "ADMIN");
insert into Users(fullname, username, password, role) values("User", "user", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "USER");