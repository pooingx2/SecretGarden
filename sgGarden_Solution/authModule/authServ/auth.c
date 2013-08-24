#include "auth.h"

int 
auth(MYSQL *con, char *id, char *pwd, char user_info[])
{
	int type;
	int length;
	byte dataBuf[DATASIZE];

	char query[255];
	MYSQL_ROW row;
		
	sprintf(query,"SELECT user_id, name, email FROM User WHERE user_id='%s' && pwd='%s'",id,pwd);
	//printf("query is %s \n", query);

	MYSQL_RES *result = mysql_store_result(con);

	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}

	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	if (result == NULL) {
			
			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;
	}



	//printf("질의 \n");
	while((row = mysql_fetch_row(result)) != NULL)
	{
		//printf("질의 완료 \n");
		strcat(user_info, row[0]);
		strcat(user_info, "\t" );
		strcat(user_info, row[1]);
		strcat(user_info, "\t" );
		strcat(user_info, row[2]);
		printf("login user id : %s, user name : %s, user email : %s \n", row[0], row[1], row[2]);
		return 1;
	}

	return 0;


	/*
	if (result == NULL) {
			
			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;
	}

	int num_fields = mysql_num_fields(result);
	*/

	/* ID, NAME, EMAIL */
	
	/*
	row = mysql_fetch_row(result);

	if(row == NULL){
			
			strcpy(dataBuf,"Incorrect ID or Password");
			length = strlen(dataBuf);
			return 0;
	}
	else 
	{
			
			strcpy(dataBuf,"");
			return 1;
	
	}
	*/

	return 1;
}


int 
signUp(MYSQL *con, char *id, char *pwd, char *name, char *email)
{
	char query[255];
	byte dataBuf[DATASIZE];
	int type;
	int length;
	printf("id : %s, pwd : %s, name : %s, email : %s \n", id, pwd, name, email);
	sprintf(query,"INSERT INTO SecretGarden.User(user_id, pwd, name, email) VALUES ('%s', '%s', '%s', '%s')",id,pwd,name,email);

	if (mysql_query(con, query)) 
	{
			fprintf(stderr,"%s\n",mysql_error(con));
			strcpy(dataBuf,"This ID is already taken");
			return 0;
	}
	else{
			strcpy(dataBuf,"");
			return 1;
	}

	return 1;
}


int 
getElements(byte *dataBuf, char *token, char *tokenBuf[])
{
	//printf("getElement 진입 \n");
	int   i=0;
	char *str;
	
	str = dataBuf;
	str = strtok(str, "\t");	

	while(str != NULL)
	{	
		tokenBuf[i++] = str;
		//printf("token %d : %s \n", i-1, tokenBuf[i-1]);
		str = strtok(NULL, "\t");	
	}
        
	return i;	
}








