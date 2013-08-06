#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>
#include <netinet/in.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <errno.h>

#include <openssl/rand.h>
#include <openssl/ssl.h>
#include <openssl/err.h>

//#include <mysql.h>

#ifndef protocol_H
#define protocol_H

// 프로토콜 구성도
/*  
 *   ||  타입  ||  데이터 길이 ||  파일 디스크립터 ||  데이터 ||
 *   
 */ 

// 기본 프로토콜 타입 지정(0~50)
#define ERROR_REPORT      	 0
#define LOGIN_REQUEST     	 1
#define LOGIN_RESPONSE    	 2
#define LOGOUT_REQUEST   	 3
#define LOGOUT_RESPONSE  	 4
#define SIGNUP_REQUEST   	 5
#define SIGNUP_RESPONSE  	 6
#define DIR_LIST_REQUEST 	 7
#define DIR_LIST_RESPONSE  	 8
#define DIR_CREAT_REQUEST	 9
#define DIR_KEY_RESPONSE	 10
#define ACCESS_DIR_REQUEST 	 11
#define ACCESS_DIR_RESPONSE	 12
#define FOLDER_CREAT_REQUEST	 13
#define FOLDER_CREAT_RESPONSE	 14
#define META_UPLOAD_REQUEST	 	 15
#define META_UPLOAD_RESPONSE	 16
#define META_DOWNLOAD_REQUEST	 17
#define META_DOWNLOAD_RESPONSE	 18
#define SHARE_REQUEST		 	 19
#define SHARE_RESPONSE		 	 20

//클라이언트간 전송 프로토콜
#define DIR_TO_HDFS_FOR_UPLOAD_METADATA		30
#define HDFS_TO_DIR_FOR_MODIFY_METAPATH		31
#define DIR_TO_HDFS_FOR_DOWNLOAD_METADATA	32
#define HDFS_TO_DIR_FOR_SEND_METADATA		33

//추가 Passing Serv 프로토콜(51~100)
#define PROGRAM_EXIT_REQUEST     50
#define PROGRAM_EXIT_RESPONSE    51

//Serv Binding 프로토콜(101~110) VPN 구성
#define AUTH_BINDING 		 	 101
#define DIR_BINDING		 	 	 102
#define HDFS_BINDING		 	 103
#define AUTH_BINDING_RESPONSE	 104
#define DIR_BINDING_RESPONSE	 105
#define HDFS_BINDING_RESPONSE	 106

//Handshake 프로토콜
#define NONECE_REQUEST			 113
#define NONECE_RESPONSE			 114
#define KEY_EXCHANGE_REUQEST	 115
#define KEY_EXCHANGE_RESPONSE	 116
#define SESSION_KEY_REQUEST	     117
#define SESSION_KEY_RESPONSE	 118

// 프로토콜에 정의된 헤더와 데이터 사이즈
#define HEADERSIZE   		 12
#define DATASIZE  		 	 1024


// Java to C 간 Byte통신을 위한 사용자 정의 자료형
typedef unsigned char byte;
typedef char 	      data_bytes;

// 통신중 에러 및 송수신자의 정보를 확인하기 위한 소켓 구조체
typedef struct Peer 
{
		int socket;
		SSL		*sslHandle;
		SSL_CTX *sslContext;
		char ip[20];
		//char mac_address[50];
		//char accessable_directory[200];
		int isAuth;
}Peer;


/* MYSQL 관련 변수 */
//MYSQL *con;
//MYSQL *row;

// TCP 버퍼에 저장된 데이터를 프로토콜에 지정된 형식으로 읽어오기 위한 함수
// peer : 상대 Host Program
// headerBuf : 프로토콜 타입, 가변길이 데이터를 읽어오기 위한 길이 저장
// dataBuf   : 구분자로 ','를 가진 String데이터가 전송된다.
int 
recvFrom(Peer *peer, byte *headerBuf, byte *dataBuf);

int 
readHeader(Peer *peer, byte *headerBuf, int headrSize);

int 
readData(Peer *peer, byte *dataBuf, int dataSize);


// 상대 TCP버퍼에 데이터를 전송하기 위한 함수
// peer : 상대 호스트 Program
// type   : 프로토콜 타입
// length : 데이터 길이
// buf    : 데이터가 저장된 버퍼
int 
sendTo(Peer *peer, int type, int redescriptor, int length,  byte *dataBuf); 

int 
writeHeader(Peer *peer,int type, int redescriptor, int length, byte *buf);

int 
writeData(Peer *peer, int length,byte *buf,  byte *dataBuf);


// JAVA TO C 간에 정수를 바이트 형태로 전달하기위한 함수
void 
intToByte(int n, byte *buf, int offset);

// JAVA TO C 간에 수신한 바이트 정보를 정수로 변환하기 위한 함
int 
byteToInt(byte *buf, int offset);


//Client join to Serv 
//File Descriptor join
int
joinToPassingServ(char *servIP, char *servPort, int servType);

// Peer Condition Check
int 
isRunning(int socket);

int 
getElements(byte *dataBuf, char *token, char *tokenBuf[]);

//암호화 알고리즘
#endif


