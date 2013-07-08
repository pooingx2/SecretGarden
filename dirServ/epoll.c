#include "epoll.h"

/*----------------------------------------------------------------
  function : init_data0
  io: none
  desc: initialize global client structure values
----------------------------------------------------------------*/

void init_data0(Peer *peer)
{
  register int i;

  for(i = 0 ; i < MAX_CLIENT ; i++)
  {	
     // =1 은 클라이언트가 아직 할당되지 않은 상태를 의미한다.
     peer[i].socket = -1;
  }
}

/*-------------------------------------------------------------
  function: init_server0
  io: input : integer - server port (must be positive)
  output: none
  desc : tcp/ip listening socket setting with input variable
----------------------------------------------------------------*/

void init_server0(int svr_port)
{
  struct sockaddr_in serv_addr;
 
  /* Open TCP Socket */
  if( (g_svr_sockfd = socket(AF_INET,SOCK_STREAM,0)) < 0 )
  {
      printf("[ETEST] Server Start Fails. : Can't open stream socket \n");
      exit(0);
  }

  /* Address Setting */
  memset( &serv_addr , 0 , sizeof(serv_addr)) ;

  serv_addr.sin_family = AF_INET;
  serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  serv_addr.sin_port = htons(svr_port);

  /* Set Socket Option  */
  int nSocketOpt = 1;
  if( setsockopt(g_svr_sockfd,
		 SOL_SOCKET,
		 SO_REUSEADDR,
		 &nSocketOpt,
		 sizeof(nSocketOpt)) < 0 )
  {
      printf("[ETEST] Server Start Fails. : Can't set reuse address\n");
      close(g_svr_sockfd);
      exit(0);
  }
 
  /* Bind Socket */
  if(bind(g_svr_sockfd,(struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
  {
     printf("[ETEST] Server Start Fails. : Can't bind local address\n");
     close(g_svr_sockfd);
     exit(0);
  }

  /* Listening */
  listen(g_svr_sockfd,15); /* connection queue is 15. */
  printf("[ETEST][START] Now Server listening on port %d\n",svr_port);
}
/*------------------------------- end of function init_server0 */

void epoll_init(void)
{
  struct epoll_event events;

  g_epoll_fd = epoll_create(MAX_EVENTS);
  
  if(g_epoll_fd < 0)
  {
     printf("[ETEST] Epoll create Fails.\n");
     close(g_svr_sockfd);
     exit(0);
  }
  printf("[ETEST][START] epoll creation success\n");

  /* event control set */
  events.events  = EPOLLIN;
  events.data.fd = g_svr_sockfd;

  /* server events set(read for accept) */
  if( epoll_ctl(g_epoll_fd, EPOLL_CTL_ADD, g_svr_sockfd, &events) < 0 )
  {
     printf("[ETEST] Epoll control fails.\n");
     close(g_svr_sockfd);
     close(g_epoll_fd);
     exit(0);
  }

  printf("[ETEST][START] epoll events set success for server\n");
}
/*------------------------------- end of function epoll_init */

void epoll_cli_add(int cli_fd)
{
 
  struct epoll_event events;

  /* event control set for read event */
  events.events = EPOLLIN;
  events.data.fd = cli_fd;

  if( epoll_ctl(g_epoll_fd, EPOLL_CTL_ADD, cli_fd, &events) < 0 )
  {
     printf("[ETEST] Epoll control fails.in epoll_cli_add\n");
  }

  printf("client epoll create \n");
}

void userpool_add(int cli_fd,char *cli_ip, Peer *peer)
{
  /* get empty element */
  register int i;

  for( i = 0 ; i < MAX_CLIENT ; i++ )
  {
     if(peer[i].socket == -1) break;
  }
  if( i >= MAX_CLIENT ) close(cli_fd);

  peer[i].socket = cli_fd;
  memset(&peer[i].ip[0],0,20);
  strcpy(&peer[i].ip[0],cli_ip);

}

void userpool_delete(int cli_fd, Peer *peer)
{
  register int i;

  for( i = 0 ; i < MAX_CLIENT ; i++)
  {
       if(peer[i].socket == cli_fd)
       {
          peer[i].socket = -1;
          break;
       }
  }
}

void userpool_send(char *buffer, Peer *peer)
{
  register int i;
  int len;

  len = strlen(buffer);

  for( i = 0 ; i < MAX_CLIENT ; i ++)
  {
      if(peer[i].socket != -1 )
      {

          len = send(peer[i].socket, buffer, len,0);
          /* more precise code needed here */
      }
  }
}

void client_recv(int event_fd, Peer *peer)
{

  unsigned char headerBuf[HEADERSIZE]; 
  unsigned char dataBuf[DATASIZE];
  int len;

  memset(headerBuf, 0x00, HEADERSIZE);
  memset(dataBuf,   0x00, DATASIZE);

  Peer t_peer;
  t_peer.socket = event_fd;

  printf("recv start \n");
  len =  recvFrom(&t_peer, headerBuf, dataBuf);  
  

  int type = byteToInt(headerBuf, 0);

  printf("read end type : %d\n", type);

  if( len < 0 || len == 0 )
  {
      userpool_delete(event_fd, peer);
      close(event_fd);
      return;
  }
  
  printf("start type classify\n");
  switch(type)
  {
	case 0 : 
	{

		//userpool_send(headerBuf, peer);
		//userpool_send(dataBuf, peer);

		break;
	}

        case 1 :
	{
		Peer auth;
		auth.socket = g_epoll_auth;
		
		if(g_epoll_auth == 0)
		{
			//Exception
			printf("auth server not running \n");
			break;
		}	

		printf("login request to : %d, %d, %s \n", auth.socket, strlen(dataBuf), dataBuf);
		sendTo(&auth, 1, event_fd, strlen(dataBuf), dataBuf);

		break;
	}

	case 101 :
	{
		printf("Auth Serv Binding \n");
		
		g_epoll_auth = event_fd;

		printf("Auth Serv fd is : %d \n", event_fd);

		sendTo(g_epoll_auth, 102, event_fd , strlen(dataBuf), dataBuf);

		printf("Auth Serv Binding End \n");
 		break;
	}
	case 102 : 
	{
		printf("Dir Serv Binding \n");
	}
 
	default :
	{
		printf("default\n");
	}
  }

}

void server_process(Peer *peer)
{
  struct sockaddr_in cli_addr;
  int i,nfds;
  int socket;
  int cli_len = sizeof(cli_addr);

  nfds = epoll_wait(g_epoll_fd,g_events,MAX_EVENTS,100); /* timeout 100ms */

  if(nfds == 0) return; /* no event , no work */
  if(nfds < 0)
  {
      printf("[ETEST] epoll wait error\n");
      return; /* return but this is epoll wait error */
  }

  for( i = 0 ; i < nfds ; i++ )
  {
      if(g_events[i].data.fd == g_svr_sockfd)
      {
          socket = accept(g_svr_sockfd,
		 (struct sockaddr *)&cli_addr,
		 (socklen_t *)&cli_len);

          if(socket < 0) /* accept error */
          {

          }
          else
          {
             printf("[ETEST][Accpet] New client connected. fd:%d,ip:%s\n",
		socket,
		inet_ntoa(cli_addr.sin_addr));

             userpool_add(socket,
			  inet_ntoa(cli_addr.sin_addr),
			  peer);
             epoll_cli_add(socket);
          }
          continue; /* next fd */
      }
      
          /* if not server socket ,
	     this socket is for client socket,
	     so we read it */
     // 이벤트가 발생한 소켓에 대해서 데이터를 읽어들인다.
     // Thread Creadte
     printf("Event Create \n");
     client_recv(g_events[i].data.fd, peer);   
     
  } /* end of for 0-nfds */
}
/*------------------------------- end of function server_process */

void *fThread(void *data)
{
	int n;
	int cSocket;
	unsigned char headerBuf[HEADERSIZE];
		 char dataBuf[DATASIZE];
		 char *ip;
		 int  type;
		 int  length;

        Peer *peer;

	peer = (Peer *)data;
	cSocket = peer->socket;
        ip   = peer->ip;
 
	memset(headerBuf, 0x00, HEADERSIZE);
	memset(headerBuf, 0x00, DATASIZE);
	
	readHeader(cSocket, headerBuf, HEADERSIZE);		
}






