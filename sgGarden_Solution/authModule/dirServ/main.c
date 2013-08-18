#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 
#include <sys/epoll.h>
#include <mysql.h>
#include <openssl/bn.h>

#include "dirs.h"
#include "protocol.h"
#include "header.h"

#define MAXLINE 1024


int main(int argc, char **argv)
{
   byte headerBuf[HEADERSIZE];
   byte dataBuf[DATASIZE];
   char directoryList[DATASIZE];
   char file_id[DATASIZE];
   char metaPath[DATASIZE];
   char fileList[DATASIZE*2];
   char *tokenBuf[DATASIZE];
   char directory_id[10];
   
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

 
  
   messagingServ.socket =  joinToPassingServ(argv[1], argv[2], 102);	
   //printf("Join Dir to Poll\n");  
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
		memset(directoryList, 0x00, DATASIZE);
		memset(fileList,  0x00, DATASIZE*2);
		memset(file_id, 0x00, DATASIZE);
		memset(metaPath, 0x00,DATASIZE);
		memset(directory_id , 0x00, 10);

		int recv_len =  recvFrom(&messagingServ, headerBuf, dataBuf);
	
	/* Send */
	if(recv_len > 0)
	{
			
		type = byteToInt(headerBuf, 0);
		desc = byteToInt(headerBuf, 4);
		length = byteToInt(headerBuf, 8);
		
		switch(type)
		{
			/* 디렉토리 조회 */
			case 7 :
			{
			
			     /* 디렉토리 조회를 위한 인자 획득 */
			     getElements(&dataBuf, '\t', tokenBuf);	
			 
			     /* User가 접속한 클라우드의 디렉토리 리스트 조회 */ 
	     		 // 인자  리스트 :  User id, Public, Private
				 // 반환  리스티 :  dir_id, name, master(이미 존재), cloud_rate, sum(size)
			     state = getdirectoryList   (con, tokenBuf[0], tokenBuf[1], tokenBuf[2], directoryList);			
				 state = get_shared_dir_list(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], directoryList);      
				 // state = get_shared_dir_list(con, tokenBuf[0]);
			     

			     if(state == 1)
			     {
        		    	 sendTo(&messagingServ, 8,
                             		    desc, strlen(directoryList), directoryList);
			     }
			     else
			     {
				 		sendTo(&messagingServ, 0,
                             		    desc, strlen(NoneMs), NoneMs);
			     }
                             break;

			}
			
			// 디렉토리 생성 
			case 9 :
			{
			     printf("before keygen\n");
			     char *key;
			     char keyString[1024];
				 char *decode;
			     char dir_id_String[10];
				
			     char *enc_data;
			     int enc_data_len;
			     int iRet;
			     int size;
			     int dir_id;

			     memset(keyString, 0x00, 1024);
				 //memset(decode,	   0x00, 1024);
			
			     /* 디렉토리 생성을 위한 인자 획득 */
			     getElements(dataBuf, '\t', tokenBuf);
			
			     // 암호화 키 생성 	
                 key = (byte*)malloc(16 * sizeof(char));
			     keyGen(&size, &key);
			     printf("Generation Secret Key String\n");
			     printf("Secret Key String : %s \n", key);

			     /* 디렉토리 생성 */
			     // 인자 리스트 : 디렉토리 이름, 프라이빗 정보, 퍼블릭 정보, 유저ID
			     state = createRootDir(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], key, tokenBuf[3], "5:5");	

			     if(state == 1)
			     {
				/* 디렉토리가 제대로 생성되었으므로 암호화된 Keyfile을 보내준다 (예외 처리 필요) */
				// 생성한 노드 인덱스 가져오기
				printf("getCreateNode \n");
				dir_id = getNodeIndex(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf[3], directory_id);				      
				// 메세지 키 생성  
				printf("build Key String \n");
				strcat(keyString, directory_id);	
				printf("before encrytion, Org message is %s \n", keyString);

				// 암호화
				enc_data = NULL;
				enc_data_len;
				
				if(encryptAES(keyString, strlen(keyString)+1, key, &enc_data, &enc_data_len))
				{
						printf("AES encrytion \n");
						printf("Encrypted message = %s \n", BinaryToBnHex(enc_data));
						printf("Encrypted message len = %d \n", enc_data_len);
				}

				int de_data_len;	

				
				if(decryptAES(enc_data, enc_data_len, key, &decode, &de_data_len))
				{
						printf("AES decrytion \n");
						printf("decrypted message = %s \n", decode);
						printf("decrypted message len = %d \n", de_data_len);
				
			    	sendTo(&messagingServ, 10, 
				    	desc, strlen(BinaryToBnHex(enc_data)), BinaryToBnHex(enc_data));
				}
				

			    else
			    {
					sendTo(&messagingServ, 0, 
					    desc, strlen(NoneMs), NoneMs);
			    }

				
			     break;

			}

			// 디렉토리 엑세스
			case 11 :
			{
				char de_key[100];
				memset(de_key, 0x00, 100);

				// 디렉토리 액세스를 위한 인자(dir_id, enc_data) 회득 
				getElements(dataBuf, "\t", tokenBuf);				
			
				
				// Key 일치 판정 - (id) 
				// DB 조회 - 복호  키 획득 - 비교 
				state = get_access_Key(con, tokenBuf[0], de_key);
				state = check_access_Key(con, tokenBuf[0], tokenBuf[1], de_key);
				
				state = 1;
				
				if(state == 1)
				{
					state = getFileList(con, tokenBuf[0], fileList);
					
				}				
			

				// 하부 리스트 전송  			
				if(state == 1)
				{
				 	sendTo(&messagingServ, 12,
                             		    desc, strlen(fileList), fileList);
				}

				else
				{
					sendTo(&messagingServ, 12,
                             		    desc, strlen(NoneMs), NoneMs);
				}
			
				break;
			}
			
			// 폴더 생성
			case 13 :
			{

				/* 폴더 생성을 위한 인자(name, parent, depth, root) 획득 */	
				state = getElements(dataBuf, "\t", tokenBuf);

				/* 폴더 생성(Insert Query) */
				state = createFolder(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf[3]);

				/* 디렉토리 하부 폴더 조회 */
				state = getFileList(con, tokenBuf[3], fileList);

				/* 부모 노드와 자식노드 데이터 생성 */
				/*
				strcat(fileList, tokenBuf[1]);
				strcat(fileList, "\t");

				strcat(fileList, tokenBuf[0]);
				strcat(fileList, ",");
				strcat(fileList, tokenBuf[2]);
				*/
				/* 생성 결과 제공 */
				if(state == 1)
				{
					sendTo(&messagingServ, 14, desc, strlen(fileList), fileList);
				}
				else
				{
					sendTo(&messagingServ, 0,  desc, strlen(NoneMs), NoneMs);
				}

				break;
			}

			// 파일(메타 데이터) 업로드
			case 15 : 	
			{	
				char recv_client[20];
				char *name_Buf;
				char *request_meta = "upload meta data";

				memset(recv_client, 0x00, 20);
				//memset(name_Buf, 0x00, 15);

				/* 파일 업로드를 위한 인자(name, parent, depth, root, [fileSize(추가)), metaData(token5)]획득 */
				state = getElements(dataBuf, "\t", tokenBuf);			
				get_Name(tokenBuf[0], &name_Buf);

				/* 파일 생성(Insert Query) */		
				// tokenBuf[3] == root (directory)
				createFile(con, name_Buf, tokenBuf[1], tokenBuf[2], tokenBuf[3], tokenBuf[4]);

				/* meta data, file_id, path, recv_client */
				state =  get_file_id(con, name_Buf, tokenBuf[1], tokenBuf[2], tokenBuf[3], file_id);
				
				/* meta data generate */ 
				sprintf(recv_client, "%d", desc);

				strcat(tokenBuf[5], ",");
				strcat(tokenBuf[5], file_id);
				strcat(tokenBuf[5], ",");
				strcat(tokenBuf[5], tokenBuf[0]);
				strcat(tokenBuf[5], ",");
				strcat(tokenBuf[5], recv_client);
					
				//printf("tokenBuf[5][mata data] is : %s \n", tokenBuf[5]);
				
				/* tokenBuf[3] is directory id */
				state = getFileList(con, tokenBuf[3], fileList);

				/* 메타 데이터 서버로 전송 */
				if(state == 1)
				{
					//sendTo(&messagingServ, 30, desc, strlen(tokenBuf[4]), tokenBuf[4]);
					sendTo(&messagingServ, 16, desc, strlen(fileList), fileList);
				}
				else
				{
					sendTo(&messagingServ, 0, desc, strlen(NoneMs), NoneMs);
				}

				break;
			}
			
			// 파일(메타 데이터) 다운로드 
			case 17 :
			{
				char recv_client[20];
				state = getElements(dataBuf, "\t", tokenBuf);

				memset(recv_client, 0x00, 20);

				/* 파일(메타 데이터) 다운로드를 위한 인자(name,parent,depth,root기반의 file id 조회)  */
				get_file_id(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf[3], file_id);
				get_meta_Path(con, file_id, metaPath);
				sprintf(recv_client, "%d", desc);
				
				strcat(metaPath, ",");
				strcat(metaPath, recv_client);

				/* metaPath, recv_client */


				if(state == 0)
					return 0;

				sendTo(&messagingServ, 32, desc, strlen(metaPath), metaPath);
					

				break;
			}


			// 메타 데이터의 metaPath 설정
			case 45:
			{

				state = getElements(dataBuf, "\t", tokenBuf);
				
				/* file_id, recv_client, meta_path  기반으로 데이터 설정 */
				printf("id : %s, recv_client : %s, meta_path : %s \n", tokenBuf[0], tokenBuf[1], tokenBuf[2]);
				modify_meta_Path(con, tokenBuf[0], tokenBuf[2]);

				int apposite_client = atoi(tokenBuf[1]);

				/* 데이터 전송 : 메타데이터의 성공적인 업로드 */
				sendTo(&messagingServ, 16, apposite_client, strlen(tokenBuf[2]), tokenBuf[2]);

				break;
			}
			
			// 메타 데이터를  클라이언트 에게  전송
			case 47:
			{
				state = getElements(dataBuf, "\t", tokenBuf);
				
				/* recv client, meta data */
				printf("recv_client : %s, meta_data : %s \n", tokenBuf[0], tokenBuf[1]);			
				
				int apposite_client = atoi(tokenBuf[0]);

				/* 데이터 전송 : 메타데이터의 성공적인 업로드 */
				sendTo(&messagingServ, 18, apposite_client, strlen(tokenBuf[1]), tokenBuf[1]);


				/* 데이터 전송 */
				break;
			}

			/* id check */
			case 19:
			{
				char id_buf[30];
				char none_String[30] = "Error : Can't Certificate ID";
				memset(id_buf, 0x00, 30);
				state = getElements(dataBuf, "\t", tokenBuf);
	

				/* Share In - user_id, target_id, dir_id */			
				/* Share Re - user_id, target_id, dir_id , share id, requester, targer, dirName  */
				state = id_check(con, tokenBuf[0], id_buf );

				if(state == 1)
				{

					/* Ok */
					sendTo(&messagingServ, 20, desc, strlen(id_buf), id_buf);

				}
				else if(state == 2)
				{
					/* ID Not Vaild */

				}
				else
				{
					/* Server Not Running */
					sendTo(&messagingServ, 0, desc, strlen(none_String), none_String);

				}
			
				
				break;

			}


			/* Share request */
			case 21:
			{
				state = getElements(dataBuf, "\t", tokenBuf);
	

				/* Share In - target_id, dir_id, user_num, ~user id */			
				/* Share Re - user_id, target_id, dir_id , share id, requester, targer, dirName  */
				state = share_request(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf);

				if(state == 1)
				{

					/* Ok */

					
					char share_List[1024];
					memset(share_List, 0x00, 1024);
					
					strcat(share_List, "Sharing Reqeust is Succesed");
					/*
					memset(share_List, 0x00, 1024);	
					state = get_share_list(con, tokenBuf[1], share_List);
					*/
					sendTo(&messagingServ, 22, desc, strlen(share_List), share_List);

				}
				else if(state == 2)
				{
					/* ID Not Vaild */

				}
				else
				{
					/* Server Not Running */
				}
			
				
				break;
			}

			/* Share List */
			case 23:
			{		
				state = getElements(dataBuf, "\t", tokenBuf);

				/* Share App - share id , target_id */
				//state = share_approve(con, tokenBuf[0]);
				state = 1;

				if(state == 1)
				{
					/* OK */
					char share_List[1024];
					
					memset(share_List, 0x00, 1024);	
					state = get_share_list(con, tokenBuf[0], share_List);
			
					sendTo(&messagingServ, 24, desc, strlen(share_List), share_List);

				}
				else
				{
					/* False */
				}
				
				break;
			}
		
			/* Share Approve */
			case 25:
			{		
				state = getElements(dataBuf, "\t", tokenBuf);

				/* Share App - share id , target_id */
				state = share_approve(con, tokenBuf[0]);

				if(state == 1)
				{
					/* OK */
					char share_List[1024];
					
					memset(share_List, 0x00, 1024);	
					state = get_share_list(con, tokenBuf[1], share_List);
			
					sendTo(&messagingServ, 24, desc, strlen(share_List), share_List);

				}
				else
				{
					/* False */
				}
				
				break;
			}



			/* Share Deny */
			case 26:
			{	
				state = getElements(dataBuf, "\t", tokenBuf);
			
				/* Deny - share id */
				/* Share App - share id , target_id */
				state = share_deny(con, tokenBuf[0]);

				if(state == 1)
				{
					/* OK */
					char share_List[1024];
					
					memset(share_List, 0x00, 1024);	
					state = get_share_list(con, tokenBuf[1], share_List);
			
					sendTo(&messagingServ, 24, desc, strlen(share_List), share_List);
					printf("Refuse Ok \n");
				}
				else
				{
					/* False */
				}

				break;
			}


			/* Share Cancle */
			case 27:
			{	
				state = getElements(dataBuf, "\t", tokenBuf);
			
				/* Deny - share id */
				/* Share App - share id , target_id */
				state = share_cancle(con, tokenBuf[0]);

				if(state == 1)
				{
					/* OK */
					char share_List[1024];
					
					memset(share_List, 0x00, 1024);	
					state = get_share_list(con, tokenBuf[1], share_List);
			
					sendTo(&messagingServ, 24, desc, strlen(share_List), share_List);

				}
				else
				{
					/* False */
				}

				break;
			}



			/* 디렉토리 삭제 */
			/* dir_id, access_key, user_id, private, public */
			case 28:
			{
				
				char file_id_list[1024];

				memset(file_id_list, 0x00, 1024);
				/* 디렉토리 id를 받아서 특정 디렉토리를 삭제한다, 하부 폴더 id리스트를 바든다 */
				/* in - dir_id */
				state = getElements(dataBuf, "\t", tokenBuf);


				printf("del dir\n");
				state = del_dir(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf[3], tokenBuf[4]);


				/* 하부 폴더도 루트가 해당인것을 찾아 삭제한다 */
				if(state == 1)
				{


					/* User가 접속한 클라우드의 디렉토리 리스트 조회 */ 
					// 인자  리스트 :  User id, Public, Private
					// 반환  리스티 :  dir_id, name, master(이미 존재), cloud_rate, sum(size)
					state = getdirectoryList(con, tokenBuf[2], tokenBuf[3], tokenBuf[4], file_id_list);			   

					// state = get_shared_dir_list(con, tokenBuf[0]);


					if(state == 1)
					{
						sendTo(&messagingServ, 8,
								desc, strlen(file_id_list), file_id_list);
					}
					else
					{
						sendTo(&messagingServ, 0,
								desc, strlen(NoneMs), NoneMs);
					}

				}

				break;
			}

			/* 폴더 삭제 */
			/* dir_id, folder_id, num, 자식 */
			case 29:
			{
				char *sign = "delete file OK\n";
				char fileList[1024];

				memset(fileList, 0x00, 1024);
				/* 폴더 id를 받아서 특정 디렉토리를 삭제한다 */
				/* in - file_id */
				state = getElements(dataBuf, "\t", tokenBuf);
				state = del_file(con, tokenBuf[0], tokenBuf[1], tokenBuf[2], tokenBuf);
				state = getFileList(con, tokenBuf[0], fileList);
			

				if(state == 1)
				{

					sendTo(&messagingServ, 12, desc, strlen(fileList), fileList);
				
				}


				break;
			}

			// 공유 기능
			default  :
			{


			     break;
			}

		}
	}
	}
}
}

	
	

