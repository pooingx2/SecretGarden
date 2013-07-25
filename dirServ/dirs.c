#include "dirs.h"

int
getdirectoryList(MYSQL *con, char *user_id,char *private, char *public, char dataBuf[])
{
	int num_fields;
	char query[255];
	char *depth = "depth";
	char *order = "file_id";
	int dir_id;
	char list[1024];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select Directory.dir_id, Directory.name From Directory, User Where user_id = '%s' && master = '%s' && private = '%s' && public = '%s' Order By dir_id", user_id, user_id, private, public);


	printf("query is %s \n", query);
 
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	printf("질의 \n");
	while((row = mysql_fetch_row(result)) != NULL)
	{
		printf("%s %s \n",row[0],row[1]);
		
		strcat(dataBuf, row[0]);
		strcat(dataBuf, "\t" );	
		strcat(dataBuf, row[1]);
		strcat(dataBuf, "\t" );
		/*	
		strcat(dataBuf, row[2]);
		strcat(dataBuf, "\t" );	
		strcat(dataBuf, row[3]);
		strcat(dataBuf, "\t" );	
		*/
		printf("dataBuf : %s \n", dataBuf);
	}
	
	
	/*
	//해당 아이디의 Root 디렉토리 id 조회
	memset(query, 0x00, 255);
	sprintf(query, "select dir_id from Directory , User  where user_id = '%s' && master = '%s' ORDER BY '%s'" ,userId, userId,depth);

	printf("query is %s \n", query);
 
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);

	while((row = mysql_fetch_row(result)) != NULL)
	{
		dir_id = atoi(row[0]);
		printf("dir id is %s \n",row[0]);
	}

	//root 이하의 디렉토리 조회
	memset(query, 0x00, 255);	

	sprintf(query,"select type, File.name, depth, parent from Directory , User , File  where user_id = '%s' && master = '%s' &&  root  = '%d' ORDER BY depth, file_id",userId, userId, dir_id);

	printf("query is %s \n", query);
 
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	//list = dataBuf;

	while((row = mysql_fetch_row(result)) != NULL)
	{
		printf("%s %s %s %s \n",row[0],row[1],row[2],row[3]);
		
		strcat(dataBuf, row[0]);
		strcat(dataBuf, "\t" );	
		strcat(dataBuf, row[1]);
		strcat(dataBuf, "\t" );	
		strcat(dataBuf, row[2]);
		strcat(dataBuf, "\t" );	
		strcat(dataBuf, row[3]);
		strcat(dataBuf, "\t" );	
		
		printf("dataBuf : %s \n", dataBuf);
	}
	*/
	return 1;
}

int 
getNode(MYSQL *con, char *userId, char *dirName, char *listBuf)
{
	return 1;
}

int
createRootDir(MYSQL *con, char *name, char *private, char *public, char *user_id)
{
	char query[255];
	char *dir_id = "NULL";
	char *accessKey = "Default";
	printf("dir_name : %s, private address : %s, public_address : %s, user_id : %s \n",
		name, private, public, user_id);

	sprintf(query,"INSERT INTO SecretGarden.Directory(dir_id, name, private, public, accessKey, master) VALUES ('%s','%s', '%s', '%s', '%s', '%s')",dir_id , name, private, public, accessKey, user_id);

	if (mysql_query(con, query)) 
	{

			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;

	}
	else
	{

			return 1;

	}
	return 1;
}

int
createDirectory(MYSQL *con, char* file_id,    char *type,     char *name
			  , char *depth ,     char *parent,   char *root)
{
	char query[255];
	byte dataBuf[DATASIZE];
	printf("file_id : %s, type : %s, name : %s, depth : %s parent : %s, root : %s \n",
		file_id, type, name, depth, parent, root);

	sprintf(query,"INSERT INTO SecretGarden.File(file_id, type, name, depth, parent, root) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",file_id, type, name, depth, parent, root);

	if (mysql_query(con, query)) 
	{

			fprintf(stderr,"%s\n",mysql_error(con));
			strcpy(dataBuf,"This ID is already taken");
			return 0;

	}
	else
	{

			strcpy(dataBuf,"");
			return 1;

	}
	return 1;
}

int 
getElements(byte *dataBuf, char *token, char *tokenBuf[])
{
	printf("getElement 진입 \n");
	int   i=0;
	char *str;
	
	str = dataBuf;
	printf("error01\n");
	str = strtok(str, "\t");	

	printf("error02\n");
	printf("strtok : %s \n", str); 
	while(str != NULL)
	{	
		printf("error03\n");
		tokenBuf[i++] = str;
		printf("token %d : %s \n", i-1, tokenBuf[i-1]);
		str = strtok(NULL, "\t");	
		//printf("strtok : %s \n", token); 
	}
	printf("strtok 2 \n");
        
	return i;	
}

