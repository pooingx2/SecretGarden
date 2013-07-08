#include <sys/types.h>  
#include <sys/stat.h>  
#include <sys/socket.h>  
#include <signal.h>  
#include <unistd.h>  
#include <netinet/in.h>  
#include <arpa/inet.h>  
#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>

#include "skel.h"
#include "skeleton.h"
#include "xmlHandler.h"

#define MAXLINE 1024

int main(int argc, char **argv)
{
	//컴파일 여부 체크
	test();

	char *port = "12400";
	
	int server_sockfd, client_sockfd;
	int state, client_len;
	int pid;

	struct sockaddr_in clientaddr, serveraddr;

	char buf[MAXLINE];

	if( (server_sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		perror("socket error : ");
		exit(0);
	}

	bzero(&serveraddr, sizeof(serveraddr));
	serveraddr.sin_family = AF_INET;
	serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);
	serveraddr.sin_port   = htons(atoi(argv[1]));

	state = bind(server_sockfd, (struct sockaddr *)&serveraddr, sizeof(serveraddr));
	

	//debuging
	//xmlDocPtr pt;
	//parseDoc("user_3208.xml", pt);

	if(state == -1)
	{
		perror("bind error : ");
	}

	state = listen(server_sockfd, 5);
	
	if(state == -1)
	{
		perror("listen error : ");
	}

	signal(SIGCHLD, SIG_IGN);
	
	while(1)
	{
		client_sockfd = accept(server_sockfd, (struct sockaddr *)&clientaddr, &client_len);

		if(client_sockfd == -1)
		{
			perror("Accept Error : ");
			exit(0);
		}

		printf("Accept Success !! \n");
		//pid = fork();
		


		pid = 0;
		char *docname    = "user_3208.xml";
	 	char nodeString[MAXLINE];



		if(pid == 0)
		{
			
			xmlDocPtr  u_doc;
			xmlNodePtr u_cur;
			
			while(1){
			// 1. read data  
			// ** 어떤 종류의 XML데이터를 요청하는지 확인한다.

			memset(buf,        0x00, MAXLINE);
			memset(nodeString, 0x00, MAXLINE);

			read(client_sockfd, buf, MAXLINE-1);
	

			//read Header

			//read Data




			//Classify stream			
			if(buf[0] == 'a')
			{
				//루트 디렉토리 요청
				//ID,PW 기반으로 XML load 및 파싱
				printf("파싱 요청\n");

				u_doc = xmlParseFile("user_3208.xml");

				if(u_doc == NULL)
				{
					printf("Can't excute PARSING \n");

					//For DEBUGING
					buf[0] = 'x';
					buf[1] = 'x';
					write(client_sockfd, buf, strlen(buf));
				}
				else
				{
					memset(buf, 0x00, MAXLINE);
					if(u_doc == NULL)
					{
						printf("u_doc is Null \n");
					}
					
					printf("get Root Element \n");				
					u_cur = xmlDocGetRootElement(u_doc);
					
					printf("Root Name : %s \n", u_cur->name);
					sprintf(buf, "Root Name is : %s", u_cur->name);
					write(client_sockfd, buf, strlen(buf));
				}			
			}
			else if(buf[0] == 'b')
			{	
				//특정 디렉토리의 하위 디렉토리 조회
				xmlChar nodename;
				
				printf("노드 탐색\n");

				//u_cur =  searchNode(nodename);
        
				printf("노드 조회\n");
				
				showChildNodes(u_doc, u_cur, nodeString);

				if(nodeString == NULL)
				{
					printf("Can't search NODE \n");
				}

				printf("전송 데이터 %s \n", nodeString);

				write(client_sockfd, nodeString, sizeof(nodeString));

			}
			else if(buf[0] == 'c')
			{
				//디렉토리 생성
				
				createBurket(u_cur, 0,"created", "root", 1);
				xmlSaveFormatFile("user_3208.xml", u_doc, 1);					
			}
			else if(buf[0] == 'd')
			{
				//특정 메타데이터 업로드 


			}
			else if(buf[0] == 'e')
			{
				//특정 메타데이터 다운로드
				
			}
			else
			{
				//Rewrite Request Send
			}

			/*
			while(1)
			{
				memset(buf, 0x00, MAXLINE);
				if(read(client_sockfd, buf, MAXLINE-1) <= 0)
				{
					close(client_sockfd);
					exit(0);
				}
				if(buf[0] == 'a'){
					printf("루트 디렉토리 정보 수신\n");
				}
				
				write(client_sockfd, buf, strlen(buf));
			}
		        */

			}
		}
	
		if(pid == -1)
		{
			perror("fork error : ");
			return 1;
		}
	}

}

