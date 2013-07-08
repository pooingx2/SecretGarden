#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 
#include <sys/epoll.h>
#include <mysql.h>

#include "auth.h"
#include "protocol.h"
#include "epoll.h"

#define MAXLINE 1024


int main(int argc, char **argv)
{
   byte headerBuf[HEADERSIZE];
   byte dataBuf[DATASIZE];
   char *tokenBuf[DATASIZE];
   
   char *id[100];
   char *pwd[100];
   int   state = 1;
   MYSQL *con;   

   char *String = "OK";
   char *NoneMs = "Error";
   int type = 0;
   int desc = 0;
   int length = 0;
   Peer messagingServ;  

 
  
   messagingServ.socket =  joinToPassingServ("127.0.0.1", "12500", 101);	
        
	// DB connection 초기화
	con = mysql_init(NULL);
	if(con == NULL) 
	{	
		fprintf(stderr,"%s\n",mysql_error(con));
	}
		
        // DB 연결
	if(mysql_real_connect
             (con,"localhost","root","32sm2u","SecretGarden", 0, NULL, 0) == NULL)
 	 {
		printf("DB Call \n");
		fprintf(stderr,"%s\n",mysql_error(con));
         }
	
	// DB 인코딩
	mysql_query(con, "set session character_set_connection=utf8");
	mysql_query(con, "set session character_set_results=utf8");
	mysql_query(con, "set session character_set_client=utf8");
   
   while(1) 
   {
	memset(headerBuf, 0x00, HEADERSIZE);
	memset(dataBuf,   0x00, DATASIZE);
        memset(tokenBuf,  0x00, DATASIZE);

	int recv_len =  recvFrom(&messagingServ, headerBuf, dataBuf);
	
	/* Send */
	if(recv_len > 0)
	{
			
		type = byteToInt(headerBuf, 0);
		desc = byteToInt(headerBuf, 4);
		length = byteToInt(headerBuf, 8);
		
		switch(type)
		{
			case 1 :
			{
			
			     // 인증을 위하여 ID, PWD 획득
			     getElements(&dataBuf, '\t', tokenBuf);		     
			     // 인증
			     printf("%s , %s \n", tokenBuf[0], tokenBuf[1]);
			     state = auth(con, tokenBuf[0], tokenBuf[1]);
				
			     if(state != 0)
			     {
        		    	 sendTo(&messagingServ, 2,
                             		    desc, strlen(String), String);
			     }
			     else
			     {
				 sendTo(&messagingServ, 0,
                             		    desc, strlen(NoneMs), NoneMs);
			     }
                             break;
			}
			case 5 :
			{

			     // 등록을 위하여 차례대로 버퍼에 id, pwd, name, email저장
			     getElements(dataBuf, '\t', tokenBuf);
			     // 데이터 베이스에 등록
			     state = signUp(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf[3]);

			     if(state == 1)
			     {
			    	sendTo(&messagingServ, 6, 
				    desc, strlen(String), String);
			     }
			     else
			     {
				sendTo(&messagingServ, 6, 
				    desc, strlen(NoneMs), NoneMs);
			     }

			}
			default  :
			{
			     break;
			}
		}
	
	}
	/* Send End */

   }
}

