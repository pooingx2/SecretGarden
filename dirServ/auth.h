#include "protocol.h"
#include <mysql.h>


// 사용자 인증
int
auth(MYSQL *con, char *id, char *pwd);



// 사용자 등록
int 
signUp(MYSQL *con, char *id, char *password, char *name, char *email);



// 인증 및 등록에 사용될 데이터 획득 함수 
int
getElements(char *str, char token, char *tokenBuf[]);


