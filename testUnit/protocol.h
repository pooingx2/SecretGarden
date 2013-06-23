#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <arpa/inet.h>


// Java to C 간 Byte통신을 위한 사용자 정의 자료형
typedef unsigned char byte;
typedef char 	      data_bytes;

// 통신중 에러 및 송수신자의 정보를 확인하기 위한 소켓 구조체
typedef struct Client {
		int socket;
		char *ip;
}Client;

// 프로토콜에 정의된 헤더와 데이터 사이즈
#define HEADERSIZE 8
#define DATASIZE  1024


// TCP 버퍼에 저장된 데이터를 프로토콜에 지정된 형식으로 읽어오기 위한 함수
// client : 상대 Host Program
// headerBuf : 프로토콜 타입, 가변길이 데이터를 읽어오기 위한 길이 저장
// dataBuf   : 구분자로 ','를 가진 String데이터가 전송된다.
int 
recvFrom(Client *client, byte *headerBuf, byte *dataBuf);

int 
readHeader(Client *client, byte *headerBuf, int headrSize);

int 
readData(Client *client, byte *dataBuf, int dataSize);


// 상대 TCP버퍼에 데이터를 전송하기 위한 함수
// client : 상대 호스트 Program
// type   : 프로토콜 타입
// length : 데이터 길이
// buf    : 데이터가 저장된 버퍼
int 
sendTo(Client *client, int type, int length, byte *buf); 

int 
writeHeader(Client *client,int type, int length);

int 
writeData(Client *client, int length, byte *dataBuf);


// JAVA TO C 간에 정수를 바이트 형태로 전달하기위한 함수
void 
intToByte(int n, byte *buf, int offset);

// JAVA TO C 간에 수신한 바이트 정보를 정수로 변환하기 위한 함
int 
byteToInt(byte *buf, int offset);수
