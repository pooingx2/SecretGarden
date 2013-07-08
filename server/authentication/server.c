#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <arpa/inet.h>

#define PORT 5556
#define BUFSIZE 100

void *fThread(void *);

int main(int argc, char **argv) {
	int sSocket;	// server Socket
	int cSocket;	// client Socket
	int len,sts;
	pthread_t thrID;
	struct sockaddr_in sAddr, cAddr;

	// client 접속을 기다리는 소켓 생성
	sSocket = socket(PF_INET, SOCK_STREAM,0);
	if(sSocket ==-1){ 
		printf("socket error\n");
		return -1;
	}
	printf("server socket create\n");

	// 서버 주소를 저장하는 구조체를 0으로 초기화
	memset(&sAddr,0,sizeof(sAddr));
	sAddr.sin_addr.s_addr = htonl(INADDR_ANY);	// IP를 리턴
	sAddr.sin_family = AF_INET;		// 주소 타입
	sAddr.sin_port = htons(PORT);	// 포트번호

	if(bind(sSocket, (struct sockaddr*) &sAddr, sizeof(sAddr))==1){
		printf("Can't Bind\n");
		return -1;
	}

	// client 접속을 허락하고 데이터를 주고받는 client 소켓 생성
	if(listen(sSocket,5)==1){
		printf("listen Fail\n");
		return -1;
	}
	printf("Client 접속을 기다립니다.\n");

	while(1) {
		len = sizeof(cAddr);
		if((cSocket = accept(sSocket, (struct sockaddr*) &cAddr, &len))<0){
			printf("Server failed in accepting\n");
			return -1;
		}
		printf("%s 접속했습니다.\n",inet_ntoa(cAddr.sin_addr));

		// Client thread 생성
		if((sts=pthread_create(&thrID,NULL,fThread,(void *)cSocket))<0){
			printf("Can't create thread\n");
			return -1;
		}
	}
	close(sSocket);

	return 0;
}

void *fThread(void *data) {
	int n;
	int cSocket;
	char rBuffer[BUFSIZE];
	cSocket = (int)data;

	while((n=read(cSocket,rBuffer,sizeof(rBuffer)))!=0){
		rBuffer[n] = '\n';
		printf("%s",rBuffer);
		write(cSocket,rBuffer,n);
	}
	close(cSocket);
}
