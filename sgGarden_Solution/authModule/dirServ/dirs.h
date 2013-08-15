#include "protocol.h"
#include <stdio.h>
#include <mysql.h>
#include <string.h>


int 
get_dir_size(MYSQL *con, char *dir_id, char dataBuf[]);

int 
createRootDir(MYSQL *con, char *name, char *private, char *public,char *accessKey, char *user_id, char *claudRate);

int 
getdirectoryList(MYSQL *con, char *userId, char *private_cloud, char *public_cloud ,char dataBuf[]);

int 
getNodeIndex(MYSQL *con, char *dirName, char *private, char *public, char *user_id, char directory_id[]);

int
get_access_Key(MYSQL *con, char *accessKey, char keyString[]);

int 
check_access_Key(MYSQL *con, char *dirid,  char *enc_data,  char *accessKey );

int 
getFileList(MYSQL *con, char *dirId, char fileList[]);

int
get_file_id(MYSQL *con, char *name, char *parent, char *depth, char *root, char file_id[]);

int
get_meta_Path(MYSQL *con, char *file_id, char *metaPath);

int
createFolder(MYSQL *con, char *name, char *parent, char *depth, char *root);

int
createFile(MYSQL *con, char *name, char *parent, char *depth, char *root, char *size);

int 
search_parentNode(MYSQL *con, char *name, char *parent, char *depth, char *root, char fileList[]);

int
modify_meta_Path(MYSQL *con , char *file_id, char *metaPath);

int 
share_request(MYSQL *con, char *user_id, char *target_id, char *dir_id);

int
get_shared_list(MYSQL *con, char *target_id);

int 
del_dir(MYSQL *con, char *dir_id);

int
del_file(MYSQL *con, char *file_id);


int
getElements(byte *dataBuf, char *token, char *tokenBuf[]);

int
get_Name(char folder_Path[], char **name_Buf);
