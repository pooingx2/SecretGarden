#include "dirs.h"

/* 디렉토리에 포함된 파일들의 크기 */
int
get_dir_size(MYSQL *con, char *dir_id, char dataBuf[])
{
    char query[255];
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select sum(size) From Directory ,  File  Where dir_id = '%s' && root = '%s' ", dir_id, dir_id);

	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	result = mysql_store_result(con);

	while((row = mysql_fetch_row(result)) != NULL)
	{
		//printf("size is : %s \n", row[0]);
		if(row[0] == NULL)
		{
			strcat(dataBuf, "0");
			return 1;
		}
		else
		{
			strcat(dataBuf, row[0]);
		}
	}
	return 1;


}

/* 클라우드 연동시 관련된 디렉토리 정보를 반환 */
int
getdirectoryList(MYSQL *con, char *user_id,char *private, char *public, char dataBuf[])
{
	int num_fields;
	char query[255];
	char *depth = "depth";
	char *order = "file_id";
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	
	sprintf(query, "select Directory.dir_id, Directory.name, Directory.master, Directory.claudRate From Directory, User Where user_id = '%s' && master = '%s' && private = '%s' && public = '%s' Order By dir_id", user_id, user_id, private, public);

	/* Query 문 출력 디버깅시 사용 */
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	result = mysql_store_result(con);
	
	while((row = mysql_fetch_row(result)) != NULL)
	{
		strcat(dataBuf, row[0]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[1]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[2]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[3]);
		strcat(dataBuf, ",");
		get_dir_size(con, row[0], dataBuf);
		strcat(dataBuf, "\t" );
	}
	
	return 1;
}

int
get_shared_dir_list(MYSQL *con, char *user_id,char *private, char *public, char dataBuf[])
{
	int num_fields;
	char query[255];
	char *depth = "depth";
	char *order = "file_id";
	char *sharing = "sharing";
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	
	printf("dirs share query\n");
	sprintf(query, "select Directory.dir_id, Directory.name, Directory.master, Directory.claudRate From Directory, Share Where private = '%s' && public = '%s' && status = '%s' && target = '%s' && dir_id = dir Order By dir_id", private, public, sharing, user_id);

	/* Query 문 출력 디버깅시 사용 */
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	result = mysql_store_result(con);
	
	while((row = mysql_fetch_row(result)) != NULL)
	{
		printf("share dir id : %s \n", row[0]); 
		strcat(dataBuf, row[0]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[1]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[2]);
		strcat(dataBuf, "," );	
		strcat(dataBuf, row[3]);
		strcat(dataBuf, ",");
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

/* 디렉토리 생성 */
int
createRootDir(MYSQL *con, char *name, char *private, char *public,char *accessKey, char *user_id, char *claudRate)
{
	char query[255];
	char *dir_id = "NULL";
	

	sprintf(query,"INSERT INTO SecretGarden.Directory(dir_id, name, private, public, accessKey, master, claudRate) VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s')",dir_id , name, private, public, accessKey, user_id, claudRate);

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

/* 디렉토리 관리 키 생성 */
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

/* 디렉토리 접속시 키 일치 여부 확인 */
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

/* 디렉토리 내부의 파일 및 폴더 리스트 반환 */
int
getFileList(MYSQL *con, char *dirId, char fileList[])
{
	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;

	memset(query, 0x00, 255);
	sprintf(query, "select File.type, File.name, File.parent, File.depth, File.root, File.size, File.file_id From Directory, File Where dir_id = '%s' && root = '%s' Order by depth", dirId, dirId);

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
		
		strcat(fileList, row[0]);
		strcat(fileList, "," );	
		strcat(fileList, row[1]);
		strcat(fileList, "," );
		strcat(fileList, row[2]);
		strcat(fileList, "," );
		strcat(fileList, row[3]);
		strcat(fileList, "," );
		strcat(fileList, row[4]);
		strcat(fileList, "," );
		strcat(fileList, row[5]);
		strcat(fileList, "," );
		strcat(fileList, row[6]);
		strcat(fileList, "\t" );

	}
	return 1;
}

/* 폴더 생성 */
int 
createFolder(MYSQL *con, char *name, char *parent, char *depth, char *root)
{
	/* insert Query */
	char *file_id = "NULL";
	char *type    = "folder";
	char *metaPath= "none";
	char *size    = "0";
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

/* 메타데이터 경로와 같은 데이터 조회를 위한 파일 및 폴더의 id획득*/
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

	//printf("query is %s \n", query);
 
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	//printf("질의 \n");
	while((row = mysql_fetch_row(result)) != NULL)
	{
		//printf("row : %s \n", row[0]);
		strcat(file_id, row[0]);
		strcat(file_id, "\t" );

	}

	return 1;
}


/* 메타 데이터 경로 획득 */
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


/* 파일 생성 */
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

	sprintf(query,"INSERT INTO SecretGarden.File(file_id, type, name, parent, metaPath, depth ,root, size) VALUES ('%s', '%s', '%s','%s','%s','%s','%s','%s')",file_id, type, name, parent, metaPath, depth, root, size);

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

/* 메타데이터 경로 수정 */
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

/* 특정 유저와 공유를 시도할시 ID가 존재하는지 체크한다 */
int
id_check(MYSQL *con, char *user_id, char *id_buf)
{

	int type;
	int length;
	byte dataBuf[DATASIZE];

	char query[255];
	MYSQL_ROW row;

	sprintf(query,"SELECT user_id FROM User WHERE user_id='%s' ", user_id);
	//printf("query is %s \n", query);

	MYSQL_RES *result = mysql_store_result(con);

	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}

	//질의를 한 결과를 출력한다.
	result = mysql_store_result(con);
	
	while((row = mysql_fetch_row(result)) != NULL)
	{
		strcat(id_buf, row[0]);
		strcat(id_buf, "\t" );
		return 1;
	}


	return 0;
}

/* 공유 요청 */
int
share_request(MYSQL *con, char *target_id, char *dir_id, char *userNum, char *tokenBuf[])
{
	printf("Share REQ IN\n");
	int type;
	int length;
	byte dataBuf[DATASIZE];

	char query[255];
	/* insert Query */
	memset(query, 0x00, 255);

	char *share_id= "NULL";
	char *status  = "waiting";
	int i=3;
	int num = atoi(userNum);

	//printf("target id : %S , dir_id : %s , userNum : %s , tokenBuf[3] : %s \n", target_id, dir_id, userNum, tokenBuf[3]);

	for(i=3;  i<3+num; i++)
	{
		MYSQL_ROW row;
		memset(query, 0x00, 255);
		//printf("share_id : %s, requester : %s, target : %s, status : %s, dirid : %s \n", share_id, tokenBuf[i], target_id , status, dir_id);

		sprintf(query,"INSERT INTO SecretGarden.Share(share_id, status, requster, dir, target ) VALUES ('%s', '%s', '%s','%s','%s')",share_id, status, target_id, dir_id, tokenBuf[i]);

		if (mysql_query(con, query)) 
		{
			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;
		}
		else
		{

		}

	}

	return 1;
}

/* 현재 공유와 관련된 정보를 반환한다 */
int
get_share_list(MYSQL *con, char *target_id, char *share_list)
{

	int num_fields;
	char query[255];
	
	MYSQL_ROW row;
	MYSQL_RES *result;


	memset(query, 0x00, 255);
	sprintf(query, "select Share.share_id, Share.requster, Share.status, Share.dir, Directory.name  From Share ,Directory Where target = '%s'&& Share.dir = Directory.dir_id "
			,target_id);

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
		strcat(share_list, "Target");
		strcat(share_list, "," );		
		strcat(share_list, row[0]);
		strcat(share_list, "," );	
		strcat(share_list, row[1]);
		strcat(share_list, "," );
		strcat(share_list, row[2]);
		strcat(share_list, "," );
		strcat(share_list, row[3]);
		strcat(share_list, "," );
		strcat(share_list, row[4]);
		strcat(share_list, "\t" );

	}


	memset(query, 0x00, 255);
	sprintf(query, "select Share.share_id,  Share.target, Share.status, Share.dir,  Directory.name From Share,  Directory  Where requster = '%s' && Share.dir = Directory.dir_id ",
			target_id);

 
	if((result = mysql_query(con,query)) != NULL)
	{
		fprintf(stderr,"질의 실패 %s\n",mysql_error(con));
		return 0;
	}
	
	result = mysql_store_result(con);


	while((row = mysql_fetch_row(result)) != NULL)
	{

		strcat(share_list, "Requester");
		strcat(share_list, "," );		
		strcat(share_list, row[0]);
		strcat(share_list, "," );	
		strcat(share_list, row[1]);
		strcat(share_list, "," );
		strcat(share_list, row[2]);
		strcat(share_list, "," );
		strcat(share_list, row[3]);
		strcat(share_list, "," );
		strcat(share_list, row[4]);
		strcat(share_list, "\t" );

	}


	return 1;
}

int
share_approve(MYSQL *con, char *share_id)
{
	/* Update Query */
	char query[255];
	char *approve = "sharing";
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
	char *deny = "refused";
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
share_cancle(MYSQL *con, char *share_id)
{
	/* Update Query */
	char query[255];

	printf("share_id : %s \n", share_id);	

	sprintf(query,"delete From Share where share_id = '%s'", share_id);



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
del_dir(MYSQL *con, char *dir_id, char *access_key, char *user_id, char *private, char *public)
{
	//aDelete From Table이름 [Wherer 조건문]
    /* delete Query */
	char query[255];
	printf("dir_id : %s \n", dir_id);	

	sprintf(query,"delete From Directory where dir_id = '%s'", dir_id);

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
del_file(MYSQL *con, char *dir_id, char *file_id, char *child_num, char **child_token)
{
 	/* delete Query */
	char query[255];
	printf("file_id : %s \n", file_id);	

	int i=0;
	int num = atoi(child_num);

	for(i=2;  i<3+num; i++)
	{
		memset(query, 0x00, 255);
		printf("del i : %d \n", i);
		if(i==2)
		{
			printf("dellll\n");
			sprintf(query,"delete From File where file_id = '%s'", file_id);
		
		}
		else
		{
			sprintf(query,"delete From File where file_id = '%s'", child_token[i]);

		}

		if (mysql_query(con, query)) 
		{
			fprintf(stderr,"%s\n",mysql_error(con));
			return 0;
		}
		else
		{

		}

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

	//printf("folder_Path, : %s \n", folder_Path);
	str = folder_Path;
	str = strtok(str, "\\/");
	//printf("str is : %s \n", str);

	while(str != NULL)
	{

		*name_Buf = str;
		str = strtok(NULL, "\\/");

	}
	//printf("Seg?\n");
	return 1;
}

