#include "protocol.h"
#include <stdio.h>
#include <mysql.h>
#include <string.h>

int 
getdirectoryList(MYSQL *con, char *userId, char dataBuf[]);

int 
getNode(MYSQL *con, char *userId, char *dirName, char *listBuf);

int 
creatDirectory(MYSQL *con, char *file_id, char *type,  char *name
			 , char *depth,   char *parent,  char *root);


int 
fileUpload();

int 
fileDownload();



int
getElements(byte *dataBuf, char *token, char *tokenBuf[]);
