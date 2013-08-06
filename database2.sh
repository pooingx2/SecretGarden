#!/bin/bash
#
##########################
# USA Linux Users Group  #
# http://www.usalug.org  #
# http://bashscripts.org #
##########################

#######################################################
#     mysqlbash.sh
#######################################################
#
#
#    FILE: mysqlbash.sh
# VERSION: 1.1
#    DATE: 06-07-2006
#
#  AUTHOR: Crouse - Please visit bashscripts.org and usalug.org
#
#
########################################################

####### VARIABLES THAT CAN BE CHANGED IF NEEDED ##############

# /usr/bin/mysql --host=localhost --user=root --password= -e "create database david"

mysqlbash_path='/usr/bin/mysql'
mysqbash_lhost="localhost"
mysqlbash_user="crouse"
mysqlbash_userpassword=""
defaultdb=""

#Do not edit the line below !
mysqlbash="${mysqlbash_path} --host="${mysqbash_lhost}" --user="${mysqlbash_user}" --password="${mysqlbash_userpassword}" -e"

####### SANDBOX ##########
#/usr/bin/mysql -e "show databases;"       
# $mysqlbash_path --host="$mysqbash_lhost" --user="$mysqlbash_user" --password="$mysqlbash_userpassword" -e "show databases;"



#############################################################

####### DATABASE FUNCTIONS #######

createmysqluser ()
{
		read -p "What username would you like to create ?" newusername
				read -p "What password would you like to use for the new user $username ?" newpassword
				${mysqlbash} "show databases;"
				read -p "Granting all privledges for which database ?" grantfordatabase
				${mysqlbash} "GRANT ALL ON ${grantfordatabase}.* TO $newusernamehere IDENTIFIED BY '$newpassword';"
}

list_databases ()
{
		echo "Listing Current Databases"
				${mysqlbash} "show databases;"
				read -p "Hit any key to continue. " temp
}

create_database ()
{
		${mysqlbash} "show databases;"
				read -p  "Enter new database name: " DATABASENAME
				${mysqlbash} "create database $DATABASENAME"
				echo "Database $DATABASENAME created"
				read -p "Hit any key to continue. " temp
}

delete_database ()
{
		${mysqlbash} "show databases;"
				read -p  "Enter the name of the database you wish to remove: " DATABASENAME
				${mysqlbash} "drop database $DATABASENAME"
				echo "Database $DATABASENAME removed"
				read -p "Hit any key to continue. " temp
}


####### TABLE FUNCTIONS #######

viewtables ()
{
		${mysqlbash} "show databases;"; read -p "Which database would you like to use : " DATABASENAME;
		${mysqlbash} "use $DATABASENAME; show tables";read -p "Hit any key to continue : " temp;
}

defaultdb ()
{

		${mysqlbash} "show databases;"; read -p  "Enter the name of the database you wish to set as the default: " Default_DB
				${mysqlbash} "use $Default_DB;";
}

createtable ()
{
		read -p  "Enter the name of the table you wish to create in database $Default_DB: " newtable
				${mysqlbash} "USE $Default_DB; CREATE TABLE ${newtable} ( field1 INT, field2 VARCHAR(50) );";
}


####### MENU FUNCTIONS #######
headerfile ()
{
		clear;
		echo "*****************************************************"; 
		echo "*             MYSQL BASH INTERFACE                  *";
		echo "*****************************************************"; 
		echo " ";
		echo "                    The Current Default Database is $Default_DB";
		echo "----------------------------------------------------------------------";
		echo "";
}


databasemenu ()
{
		headerfile
				menu="     1)show databases 2)create database  3)delete database  4) Set default database 5) Create mysqluser0)main menu"
				while true; do 

						headerfile; 
		echo -e "$menu"
				echo "";
		read -p "Please choose one of the options above : " option

				case $option in
				1)   list_databases; ;;
		2)   create_database; ;;
		3)   delete_database; ;;
		4)   defaultdb;;
		5)   createmysqluser;;
		0)   mainmenu; ;;


		*)   echo "Sorry, that isn't an option, try again. "; sleep 2;  ;;
		esac
				done; 
}


tablemenu ()
{
		headerfile
				menu="     1) Show Databases 2) View Tables  3) Create Table  0) Main Menu"
