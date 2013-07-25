#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <mysql.h>

#define PORT 5551
#define HEADERSIZE 8
#define DATASIZE 1024

#define Error 0
#define LoginRequest 1
#define LoginResponse 2
#define LogoutRequest 3
#define LogoutResponse 4
#define DirectoryRequest 5
#define DirectoryResponse 6

typedef struct Client {
		int socket;
		char *ip;
}Client;

typedef unsigned char byte;

void *fThread(void *);
void databaseErr(MYSQL *con);
void readHeader(int cSocket, unsigned char *headerBuf, int headrSize);
void readData(int cSocket, char *dataBuf, int dataSize);
int byteToInt(unsigned char *buf, int offset);
void packetMgr(int type, int length, char *dataBuf, Client *client, MYSQL *con);
void login(Client *client, MYSQL *con, char *id, char *pwd); 
void sendTo(Client *client, char *buf); 

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
			exit(1);
		}
		printf("server socket create\n");
	
		// 서버 주소를 저장하는 구조체를 0으로 초기화
		memset(&sAddr,0,sizeof(sAddr));
		sAddr.sin_addr.s_addr = htonl(INADDR_ANY);	// IP를 리턴
		sAddr.sin_family = AF_INET;		// 주소 타입
		sAddr.sin_port = htons(PORT);	// 포트번호
	
		if(bind(sSocket, (struct sockaddr*) &sAddr, sizeof(sAddr))==1){
				printf("Can't Bind\n");
			exit(1);
		}
	
		// client 접속을 허락하고 데이터를 주고받는 client 소켓 생성
		if(listen(sSocket,5)==1){
			printf("listen Fail\n");
			exit(1);
		}
		printf("Wait Clients\n");
	
		while(1) {
			len = sizeof(cAddr);
			if((cSocket = accept(sSocket, (struct sockaddr*) &cAddr, &len))<0){
				printf("Server failed in accepting\n");
				exit(1);
			}
			Client client;
			client.socket = cSocket;
			client.ip = inet_ntoa(cAddr.sin_addr);
	
			printf("User Access : %s\n",client.ip);
	
			// Client thread 생성
			if((sts=pthread_create(&thrID,NULL,fThread,(void *)&client))<0){
				printf("Can't create thread\n");
					exit(1);
			}
		}	
		close(sSocket);

		return 0;
	}

void *fThread(void *data) {
		int n;
		int cSocket;
		unsigned char headerBuf[HEADERSIZE];
		char dataBuf[DATASIZE];
		char *ip;
		int type;
		int length;
	
		Client *client;
		client = (Client*)data;
		cSocket = client->socket;
		ip = client->ip;
	
		// DB connection 초기화
		MYSQL *con = mysql_init(NULL);
		if(con == NULL) {
			databaseErr(con);
		}
		
		// DB 연결
		if(mysql_real_connect(con,"localhost",
								"pooingx2","SSM2013","SecretGarden",0,NULL,0) == NULL) {
			databaseErr(con);
		}
	
		// DB 인코딩
		mysql_query(con, "set session character_set_connection=utf8");
		mysql_query(con, "set session character_set_results=utf8");
		mysql_query(con, "set session character_set_client=utf8");

		memset(headerBuf,0,HEADERSIZE);
		memset(dataBuf,0,DATASIZE);
		readHeader(cSocket,headerBuf,HEADERSIZE);

		type = byteToInt(headerBuf,0);
		length = byteToInt(headerBuf,4);

		readData(cSocket,dataBuf,length);

		packetMgr(type,length,dataBuf,client,con);

		mysql_close(con);
		close(cSocket);
}

void databaseErr(MYSQL *con) {
		fprintf(stderr,"%s\n",mysql_error(con));
		mysql_close(con);
		exit(1);
}

void readHeader(int cSocket, byte *headerBuf, int headerSize){
		int	readSize;
		int needSize;

		readSize=0;
		needSize=headerSize;
		while(needSize > 0){
				readSize+=read(cSocket, headerBuf, needSize);
				needSize=needSize-readSize;
		}
		printf("header read end...\n");
}

void readData(int cSocket, char *dataBuf, int dataSize){
		int	readSize;
		int needSize;

		readSize=0;
		needSize=dataSize;
		while(needSize > 0){
				readSize+=read(cSocket, dataBuf, needSize);
				needSize=needSize-readSize;
		}
		printf("data read end...\n");
}

int byteToInt(byte *buf, int offset) {
		int unit_digit, ten_digit, hund_digit, thos_digit;

		unit_digit = buf[offset] << 24;
		ten_digit = buf[offset+1] << 16;
		hund_digit = buf[offset+2] << 8;
		thos_digit = buf[offset+3] << 0;
		printf("test : %d %d %d %d\n",unit_digit,ten_digit,hund_digit,thos_digit);

		return (unit_digit+ten_digit+hund_digit+thos_digit);
}

void packetMgr(int type, int length, char *dataBuf, Client *client, MYSQL *con) {
		char *str;
		char *token[DATASIZE];
		int i=0;
		int j=0;
		str = dataBuf;
		printf("[ from %s ] type : %d, length : %d, data : %s \n", client->ip, type, length, str);

		str = strtok(str,"\t");		// \t 단위로 토큰을 나움
		while(str !=NULL) {
				token[i++] = str;
				str = strtok(NULL,"\t");
		}
		if(type == LoginRequest) {
				login(client,con,token[0],token[1]);
		}
		if(type == LogoutRequest) {
				// delete
				sendTo(client,"4");
		}
}

void login(Client *client, MYSQL *con, char *id, char *pwd) {
		char query[255];
		sprintf(query,"SELECT * FROM User WHERE user_id='%s' && pwd='%s'",id,pwd);
		if (mysql_query(con, query)) {
				databaseErr(con);
		}

		MYSQL_RES *result = mysql_store_result(con);

		if (result == NULL) {
				databaseErr(con);
				printf("null\n");
		}

		int num_fields = mysql_num_fields(result);

		char buf[DATASIZE]="";
		MYSQL_ROW row;
		row = mysql_fetch_row(result);

		if(row == NULL){
				sprintf(buf,"%d\tIncoorect ID or Password",Error);
				sendTo(client, buf);
		}
		else {
				//sprintf(buf,"%d\t%s",LoginResponse,row[2]);
				sprintf(buf,"%d\t%s",345,row[2]);
				sendTo(client, buf);
		}
		/*
		while ((row = mysql_fetch_row(result))) { 
				for(int i = 0; i < num_fields; i++) { 
						//printf("%s ", row[i] ? row[i] : "NULL"); 
				} 
				printf("\n"); 
		}*/

}

void sendTo(Client *client, char *buf) {
		printf("[  to  %s ] : %s\n", client->ip, buf);
		write(client->socket,buf,strlen(buf));
}

