
create Database SecretGarden;
GRANT ALL ON SecretGarden.* To 'pooingx2' IDENTIFIED BY 'SSM2013';


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
    private VARCHAR(20) NOT NULL,
    public VARCHAR(20) NOT NULL,
    accessKey VARCHAR(255) NOT NULL,
	cloudRate VARCHAR(20) NOT NULL,
    master VARCHAR(20) NOT NULL,

    PRIMARY KEY (dir_id),
    FOREIGN KEY (master) REFERENCES User (user_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE File (
    file_id INTEGER NOT NULL AUTO_INCREMENT,
    type VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent VARCHAR(255) NOT NULL,
    metaPath VARCHAR(255) NOT NULL,
    depth INTEGER NOT NULL,
	size LONG NOT NULL,
    root INTEGER NOT NULL,

    PRIMARY KEY (file_id),
    FOREIGN KEY (root) REFERENCES Directory (dir_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Share (
    share_id INTEGER NOT NULL AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL,
    requster VARCHAR(20) NOT NULL,
    dir INTEGER NOT NULL,
    target VARCHAR(20) NOT NULL,

    PRIMARY KEY (share_id)
   
)DEFAULT CHARSET=utf8;

CREATE TABLE Nonce (
    nonce   VARCHAR(255) NOT NULL,
    time    VARCHAR(255) NOT NULL,

    PRIMARY KEY (nonce)
)DEFAULT CHARSET=utf8;

ALTER TABLE Share
ADD FOREIGN KEY (target) REFERENCES User (user_id);
ALTER TABLE Share
ADD FOREIGN KEY (dir) REFERENCES Directory (dir_id);

drop table User;
SELECT * FROM User;
INSERT INTO `SecretGarden`.`User` (`user_id`, `pwd`, `name`, `email`) VALUES ('test', 'test', '테스트', 'test@gmail.com');

Select dir_id, name, master, cloudRate, sum(size) from directory natural join file
group by dir_id;
/*
ALTER TABLE User_Root
ADD FOREIGN KEY (idFile) REFERENCES User (idFile);
*/

