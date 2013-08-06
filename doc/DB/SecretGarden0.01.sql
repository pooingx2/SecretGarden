
create Database SecretGarden;
GRANT ALL ON SecretGarden.* To pooingx2@localhost IDENTIFIED BY 'SSM2013';

/*
CREATE TABLE User
(
	idUser			VARCHAR(20) NOT NULL,
	pwd				VARCHAR(20) NOT NULL,
	name			VARCHAR(20) NOT NULL,
	email			VARCHAR(50) NOT NULL,
	
	PRIMARY KEY (idUser)
);

CREATE TABLE Root
(
	idRoot		INTEGER			NOT NULL AUTO_INCREMENT,
	name		VARCHAR(20)		NOT NULL,
	accessKey	VARCHAR(255)	NOT NULL,

	PRIMARY KEY (idRoot)
);

CREATE TABLE File
(
	idFile		INTEGER			NOT NULL AUTO_INCREMENT,
	name		VARCHAR(20)		NOT NULL,
	path		VARCHAR(255)	NOT NULL,
	metaPath	VARCHAR(255)	NOT NULL,

	PRIMARY KEY (idFile)
);

CREATE TABLE User_Root
(
	idUser		VARCHAR(20) 	NOT NULL,
	idRoot		INTEGER			NOT NULL,

	FOREIGN KEY (idUser) REFERENCES User (idUser),
	FOREIGN KEY (idRoot) REFERENCES Root (idRoot)
);

CREATE TABLE Root_File
(
	idRoot		INTEGER		 	NOT NULL,
	idFile		INTEGER			NOT NULL,
	
	FOREIGN KEY (idRoot) REFERENCES Root (idRoot),
	FOREIGN KEY (idFile) REFERENCES File (idFile)
);
*/
/*
ALTER TABLE User_Root
ADD FOREIGN KEY (idFile) REFERENCES User (idFile);
*/
ALTER DATABASE SecretGarden DEFAULT CHARACTER SET utf8;

show variables like 'c%';
ALTER TABLE User charset=utf8;

select * from User WHERE idUser = 'test' && pwd = 'test';
