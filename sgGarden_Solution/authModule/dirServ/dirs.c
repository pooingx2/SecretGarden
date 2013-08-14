#include "dirs.h"

int get_dir_size(MYSQL *con, char *dir_id, char dataBuf[])
{
    char query[255];
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select sum(size) From Directory Natural join File  Where dir_id = '%s' ", dir_id);

	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	while((row = mysql_fetch_row(result)) != NULL)
	{
		strcat(dataBuf, row[0]);
	}
	return 1;


}

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
	
	sprintf(query, "select Directory.dir_id, Directory.name, Directory.claudRate From Directory, User, File  Where user_id = '%s' && master = '%s' && private = '%s' && public = '%s' Order By dir_id", user_id, user_id, private, public);
	
	/*
	sprintf(query, "select Directory.dir_id, Directory.name, Directory.claudRate, sum(size) From User ,Directory Natural join File Where user_id = '%s' && master = '%s' && private = '%s' && public = '%s' group by dir_id", user_id, user_id, private, public);
    */
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
		strcat(dataBuf, row[0]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[1]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[2]);
		strcat(dataBuf, "," );	
		get_dir_size(con, row[0], dataBuf);
		strcat(dataBuf, "\t" );
	}
	
	return 1;
}

int 
getNodeIndex(MYSQL *con, char *dirName, char *private, char *public, char *user_id, char directory_id[])
{
	char query[255];
	char tempString[20];
	int  nodeIndex;

	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select Directory.dir_id From Directory, User Where user_id = '%s' && master = '%s' && Directory.name = '%s' && private = '%s' && public = '%s'", user_id, user_id, dirName, private, public);
	printf("query is %s \n", query);

	if(result = mysql_query(con, query) != NULL)
	{
		fprintf(stderr, "질의 실패 %s \n", mysql_error(con));
		return 0;
	}
	result = mysql_store_result(con);	

	if((row = mysql_fetch_row(result)) == NULL)
	{
		printf("결과 조회 실패 \n");
		return 0;
	} 
	
	strcat(directory_id, row[0]);
	printf("index is %s \n", directory_id);
	return 1;
}

int
createRootDir(MYSQL *con, char *name, char *private, char *public,char *accessKey, char *user_id, char *claudRate)
{
	char query[255];
	char *dir_id = "NULL";
	
	printf("dir_name : %s, private address : %s, public_address : %s, user_id : %s \n",
		name, private, public, user_id);

	sprintf(query,"INSERT INTO SecretGarden.Directory(dir_id, name, private, public, accessKey, mastera, claudRate) VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s')",dir_id , name, private, public, accessKey, user_id, claudRate);

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
get_access_Key(MYSQL *con, char *dirid, char keyString[])
{
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select Directory.accessKey From Directory  Where dir_id = '%s' ", dirid);

	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	while((row = mysql_fetch_row(result)) != NULL)
	{
		
		strcat(keyString, row[0]);
		strcat(keyString, "\t" );

	}
	printf("get Access Key : %s \n", keyString);
	return 1;

}

int
check_access_Key(MYSQL *con, char *dirid, char *enc_message, char *de_key)
{
	char *msg_data;
	int enc_data_len;
	printf("암호화된 데이터 : %s \n", enc_message);

	//decryptAES(byte *enc_msg, int enc_msg_len, byte *key, byte **msg, int *msg_len)
	decryptAES(enc_message, strlen(enc_message)+1, de_key, &msg_data, &enc_data_len);
	printf("decrytMSG : %s \n", msg_data);

	return 1;
}

int
getFileList(MYSQL *con, char *dirId, char fileList[])
{
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select File.type, File.name, File.parent, File.depth, File.root From Directory, File Where dir_id = '%s' && root = '%s' Order by depth", dirId, dirId);

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
		
		strcat(fileList, row[0]);
		strcat(fileList, "," );	
		strcat(fileList, row[1]);
		strcat(fileList, "," );
		strcat(fileList, row[2]);
		strcat(fileList, "," );
		strcat(fileList, row[3]);
		strcat(fileList, "," );
		strcat(fileList, row[4]);
		strcat(fileList, "\t" );

	}
	printf("fileList : %s \n", fileList);
	return 1;
}

int 
createFolder(MYSQL *con, char *name, char *parent, char *depth, char *root)
{
	/* insert Query */
	char *file_id = "NULL";
	char *type    = "folder";
	char *metaPath= "none";
	char query[255];
	byte dataBuf[DATASIZE];
	printf("name : %s, parent : %s, depth : %s, root : %s", name, parent, depth, root);	

	sprintf(query,"INSERT INTO SecretGarden.File(file_id, type, name, parent, metaPath, depth ,root) VALUES ('%s', '%s', '%s','%s','%s','%s','%s')",file_id, type, name, parent,metaPath, depth, root);

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
get_file_id(MYSQL *con, char *name, char *parent, char *depth, char *root, char file_id[])
{
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select File.file_id From File Where name = '%s' && parent = '%s' && depth = '%s' && root = '%s'",
			name, parent, depth, root);

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
		printf("row : %s \n", row[0]);
		strcat(file_id, row[0]);
		strcat(file_id, "\t" );

	}
	printf("file id : %s \n", file_id);

	return 1;
}


int
get_meta_Path(MYSQL *con, char file_id[], char metaPath[])
{	
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select File.metaPath From File Where file_id = '%s'",
			file_id);

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
		printf("meta Path : %s \n", row[0]);
		strcat(metaPath, row[0]);
		strcat(metaPath, "\t" );

	}

	return 1;
}


int 
createFile(MYSQL *con, char *name, char *parent, char *depth, char *root, char *size)
{
	/* insert Query */
	char *file_id = "NULL";
	char *type    = "file";
	char *metaPath= "none";
	char query[255];
	byte dataBuf[DATASIZE];
	printf("name : %s, parent : %s, depth : %s, root : %s", name, parent, depth, root);	

	sprintf(query,"INSERT INTO SecretGarden.File(file_id, type, name, parent, metaPath, depth ,root, size) VALUES ('%s', '%s', '%s','%s','%s','%s','%s','%s')",file_id, type, name, parent,metaPath, depth, root, size);

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
search_parentNode(MYSQL *con, char *name, char *parent, char *depth, char *root, char fileList[])
{
	/* search Query */
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	/* name, depth */
	/*
	sprintf(query, "select parent.name, parent.depth from File child, File parent where child.name = '%s' &&  child.parent = parent.name  && child.depth = '%s' && child.root = '%s', naname, depth, ");
	*/
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
		
		strcat(fileList, row[0]);
		strcat(fileList, "," );	
		strcat(fileList, row[1]);
		strcat(fileList, "," );
		strcat(fileList, row[2]);
		strcat(fileList, "," );
		strcat(fileList, row[3]);
		strcat(fileList, "," );
		strcat(fileList, row[4]);
		strcat(fileList, "\t" );

	}
	printf("fileList : %s \n", fileList);
	return 1;

}


int
modify_meta_Path(MYSQL *con, char *file_id, char *metaPath)
{

    /* Update Query */
	char query[255];
	printf("file_id : %s, metaPath : %s \n", file_id, metaPath);	

	sprintf(query,"update File set metaPath = '%s' where file_id = '%s'", metaPath, file_id);

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
share_request(MYSQL *con, char *user_id, char *target_id, char *dir_id)
{
	/* insert Query */
	char query[255];
	char *share_id= "NULL";
	char *status  = "waiting";
	
	byte dataBuf[DATASIZE];
	printf("share_id : %s, requester : %s, target : %s, status : %s, dirid : %s \n", share_id, user_id, target_id, status, dir_id);	

	sprintf(query,"INSERT INTO SecretGarden.Share(share_id, status, requster, dir, target ) VALUES ('%s', '%s', '%s','%s','%s')",share_id, status, user_id, dir_id, target_id);

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
get_share_list(MYSQL *con, char *target_id, char *share_list)
{

	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;


	memset(query, 0x00, 255);
//	sprintf(query, "select Share.share_id, Share.requster, Share.target, Directory.name  From Share, Directory  Where target = '%s', ",
//			target_id);

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
		printf("meta Path : %s \n", row[0]);
	//	strcat(metaPath, row[0]);
	//	strcat(metaPath, "\t" );

	}

	return 1;
}
int
share_approve(MYSQL *con, char *share_id)
{
	/* Update Query */
	char query[255];
	char *approve = "approve";
	printf("share_id : %s \n", share_id);	

	sprintf(query,"update Share set status = '%s' where share_id = '%s'", approve, share_id);

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
share_deny(MYSQL *con, char *share_id)
{
	/* Update Query */
	char query[255];
	char *deny = "deny";
	printf("share_id : %s \n", share_id);	

	sprintf(query,"update Share set status = '%s' where share_id = '%s'", deny, share_id);

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
getElements(byte *dataBuf, char *token, char *tokenBuf[])
{
	int   i=0;
	char *str;
	
	str = dataBuf;
	str = strtok(str, "\t");	

	while(str != NULL)
	{	
		tokenBuf[i++] = str;
		str = strtok(NULL, "\t");	
	}
        
	return i;	
}

int
get_Name(char folder_Path[], char **name_Buf)
{
	char *str;
	char *ptr;

	printf("folder_Path, : %s \n", folder_Path);
	str = folder_Path;
	str = strtok(str, "\\/");
	printf("str is : %s \n", str);
	while(str != NULL)
	{
		printf("str is : %s \n", str);

		*name_Buf = str;
		str = strtok(NULL, "\\/");
		printf("name BUf %s \n", *name_Buf);

	}
	
	printf("Seg?\n");
	return 1;
}

