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

/* definition */
#define MAX_CLIENT   10000
#define DEFAULT_PORT 12500
#define MAX_EVENTS   10000


/* global definition */
int g_svr_sockfd;              /* global server socket fd */
int g_svr_port;                /* global server port number */

int g_epoll_fd;                /* epoll fd */

struct epoll_event g_events[MAX_EVENTS]; 

// Epoll 메세지 패싱 서버에서 Auth, Dir, HDFS를 나타내는 소켓 디스크립터 변수
int g_epoll_auth;
int g_epoll_dir;
int g_epoll_HDFS;

/* function prototype */
void init_data0(Peer *peer);            /* initialize data. */
void init_server0(int svr_port);  /* server socket bind/listen */
void epoll_init(void);            /* epoll fd create */
void epoll_cli_add(int cli_fd);   /* client fd add to epoll set */

void userpool_add(int cli_fd,char *cli_ip, Peer *peer);
void userpool_delete(int cli_fd, Peer *peer);
void userpool_send(char *buffer, Peer *peer);
void userpool_recv(int event_fd);

void client_recv(int event_fd, Peer *peer);

void server_process(Peer *peer11);


// message table,  

#endif
