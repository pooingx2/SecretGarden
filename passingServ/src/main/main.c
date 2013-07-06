#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 
#include <sys/epoll.h>

#include "protocol.h"
#include "epoll.h"

#define MAXLINE 1024

Peer peer[MAX_CLIENT];

int main(int argc, char **argv)
{	
 
  printf("[ETEST][START] epoll test server v1.2 (simple epoll test server)\n");
  /* entry , argument check and process */
  if(argc < 3) g_svr_port = DEFAULT_PORT;
  else
  {
     if(strcmp("-port",argv[1]) ==  0 )
     {
        g_svr_port = atoi(argv[2]);
        if(g_svr_port < 1024)
        {
           printf("[ETEST][STOP] port number invalid : %d\n",g_svr_port);
           exit(0);
        }
     }
  }

  printf("init peer array \n");
  init_data0(peer);  

  /* init server */
  printf("init server port\n");
  init_server0(g_svr_port);
  epoll_init();    /* epoll initialize  */

  printf("init server_process\n");
  /* main loop */
  while(1)
  {
     server_process(peer);  /* accept process. */
  } /* infinite loop while end. */

}

