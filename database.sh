#!/bin/bash

# MySQL settings
mysql_user="root"
mysql_password=""

# Read MySQL password from stdin if empty
if [ -z "${mysql_password}" ]; then
	echo -n "Enter MySQL ${mysql_user} password: "
	read -s mysql_password
	echo
fi

# Check MySQL password
echo exit | mysql -u"${mysql_user}" -p"${mysql_password}"
if [ "$?" -gt 0 ]; then
	echo "MySQL ${mysql_user} password incorrect"
	exit 1
else
	echo "MySQL ${mysql_user} password correct."
fi

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE DATABASE SecretGarden;"
echo "Create Database ScretGarden"

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
GRANT ALL ON SecretGarden.* TO 'secret'@'localhost' IDENTIFIED BY 'garden';"
echo "Create and Grant Database User \"secret\""

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE TABLE SecretGarden.User (
	user_id VARCHAR(20) NOT NULL,
	pwd VARCHAR(20) NOT NULL,
	name VARCHAR(20) NOT NULL,
	email VARCHAR(50) NOT NULL,
	
	PRIMARY KEY (user_id)
)DEFAULT CHARSET=utf8;"
echo "Create Table User"

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE TABLE SecretGarden.Directory (
	dir_id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	private VARCHAR(20) NOT NULL,
	public VARCHAR(20) NOT NULL,
	accessKey VARCHAR(255) NOT NULL,
	master VARCHAR(20) NOT NULL,

	PRIMARY KEY (dir_id),
	FOREIGN KEY (master) REFERENCES User (user_id)
)DEFAULT CHARSET=utf8;"
echo "Create Tabel Directory"

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE TABLE SecretGarden.File (
	file_id INTEGER NOT NULL AUTO_INCREMENT,
	type VARCHAR(20) NOT NULL,
	name VARCHAR(255) NOT NULL,
	parent VARCHAR(255) NOT NULL,
	metaPath VARCHAR(255) NOT NULL,
	depth INTEGER NOT NULL,    
	root INTEGER NOT NULL,

	PRIMARY KEY (file_id),
	FOREIGN KEY (root) REFERENCES Directory (dir_id)
)DEFAULT CHARSET=utf8;"
echo "Create Tabel File"

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE TABLE SecretGarden.Share (
	share_id INTEGER NOT NULL AUTO_INCREMENT,
	status VARCHAR(20) NOT NULL,
	requster VARCHAR(20) NOT NULL,
	dir INTEGER NOT NULL,
	target VARCHAR(20) NOT NULL,

	PRIMARY KEY (share_id),
	FOREIGN KEY (target) REFERENCES User (user_id),
	FOREIGN KEY (dir) REFERENCES Directory (dir_id)
)DEFAULT CHARSET=utf8;"
echo "Create Tabel Share"

mysql -u"${mysql_user}" -p"${mysql_password}" -e"
CREATE TABLE SecretGarden.Nonce (
	nonce   VARCHAR(255) NOT NULL,
	time    VARCHAR(255) NOT NULL,

	PRIMARY KEY (nonce)
)DEFAULT CHARSET=utf8;"
echo "Create Tabel Nonce"

