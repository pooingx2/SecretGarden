/*
create Database SecretGarden;
GRANT ALL ON SecretGarden.* To 'pooingx2' IDENTIFIED BY 'SSM2013';
*/


CREATE TABLE User (
    user_id VARCHAR(20) NOT NULL,
    pwd VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,

    PRIMARY KEY (user_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Directory (
    dir_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    accessKey VARCHAR(255) NOT NULL,
    master VARCHAR(20) NOT NULL,

    PRIMARY KEY (dir_id),
    FOREIGN KEY (master) REFERENCES User (user_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE File (
    file_id INTEGER NOT NULL AUTO_INCREMENT,
    type VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    path VARCHAR(255) NOT NULL,
    metaPath VARCHAR(255) NOT NULL,
    root INTEGER NOT NULL,

    PRIMARY KEY (file_id),
    FOREIGN KEY (root) REFERENCES Directory (dir_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Share (
    idUser VARCHAR(20) NOT NULL,
    idRoot INTEGER NOT NULL,
    FOREIGN KEY (idUser)
        REFERENCES User (idUser),
    FOREIGN KEY (idRoot)
        REFERENCES Root (idRoot)
)DEFAULT CHARSET=utf8;

CREATE TABLE Root_File (
    idRoot INTEGER NOT NULL,
    idFile INTEGER NOT NULL,
    FOREIGN KEY (idRoot)
        REFERENCES Root (idRoot),
    FOREIGN KEY (idFile)
        REFERENCES File (idFile)
)DEFAULT CHARSET=utf8;

/*
ALTER TABLE User_Root
ADD FOREIGN KEY (idFile) REFERENCES User (idFile);
*/

