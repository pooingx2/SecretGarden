#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 

#include "protocol.h"

#define MAXLINE 1024

int main(int argc, char **argv)
{	
	char *port = "12400";
	
	int server_sockfd, client_sockfd;
	int state, client_len;
	int pid;

	struct sockaddr_in clientaddr, serveraddr;
	
	Client client;
		

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
		client_sockfd = accept(server_sockfd, 
			(struct sockaddr *)&clientaddr,
			 &client_len);

		if(client_sockfd == -1)
		{
			perror("Accept Error : ");
			exit(0);
		}

		printf("Accept Success !! \n");
		pid = fork();

		unsigned char *headerBuf[8];
		unsigned char *dataBuf[MAXLINE];
		int type;
		int length;		

		if(pid == 0)
		{
			while(1){

			memset(headerBuf, 0x00, 8);
			memset(dataBuf  , 0x00, 1024);
			
			client.socket = client_sockfd;
			client.ip     = inet_ntoa(clientaddr.sin_addr);			

			//readHeader(client_sockfd, headerBuf, 8);
			recvFrom(&client, headerBuf, dataBuf);			

			type = byteToInt(headerBuf, 0);
			length = byteToInt(headerBuf, 4);
	
			printf("type : %d , length : %d \n", type, length);
				
			//readData(client_sockfd, dataBuf, length);

			//printf("debug start \n");
			//printf("data : %s \n", dataBuf);
			//printf("end...?\n");
			
			length = strlen(dataBuf);
			printf("type : 2 length %d data : %s \n",length, dataBuf);
			sendTo(&client, 2, length, dataBuf);

			}
		}
	
		if(pid == -1)
		{
			perror("fork error : ");		
		}
	}

}

