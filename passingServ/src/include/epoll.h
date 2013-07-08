#ifndef epoll_H
#define epoll_H

#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include "protocol.h"

// 최대 사용자수, 기본 포트번호, 최대 수용가능한 이벤트 갯수
#define MAX_CLIENT   10000
#define DEFAULT_PORT 12500
#define MAX_EVENTS   10000

// 프로그램에서 사용할 전역 변수 (소켓, 포트, 디스크립터)
int g_svr_sockfd;              
int g_svr_port;                
int g_epoll_fd;                
struct epoll_event g_events[MAX_EVENTS]; 

// Epoll 메세지 패싱 서버에서 Auth, Dir, HDFS를 나타내는 소켓 디스크립터 변수
int g_epoll_auth;
int g_epoll_dir;
int g_epoll_HDFS;

// 메세지 패싱 함수
void init_data0(Peer *peer);      
void init_server0(int svr_port);  
void epoll_init(void);            
void epoll_cli_add(int cli_fd);   

// 사용자 접속시 이벤트 풀에 사용자 디스크립터 저장
void userpool_add(int cli_fd,char *cli_ip, Peer *peer);
void userpool_delete(int cli_fd, Peer *peer);
void userpool_send(char *buffer, Peer *peer);
void userpool_recv(int event_fd);

// 데이터 수신
void client_recv(int event_fd, Peer *peer);

// 이벤트 대기
void server_process(Peer *peer11);

/* 중간평가 이후 구현할 모듈 */

// 접속한 사용자 테이블( ID, IP, MAC, Message Seqeence) 관리 프로세스 - IPC로 구현한다.  
/*
struct Joining_User_Info{

	char userId[20];
	char userIp[20];
	char userMac[30];

	int  connection;
	int  socketFd;

	int  allowOrDeny;

	int  messageNum;

		

}
*/
// IP변조 탐지 모듈

// 로그 기록 모듈(Protocol)


#endif
