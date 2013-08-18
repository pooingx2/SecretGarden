#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <signal.h> 
#include <sys/epoll.h>
#include <signal.h>

#include "protocol.h"
#include "epoll.h"
#include "macAddressCapture.h"
#include "logHandler.h"

#define MAXLINE 1024

//Peer peer[MAX_CLIENT];
;

/* extern variable - pcap lib */


int main(int argc, char **argv)
{	
 
  printf("Serv program start \n");
  Peer peer[4000];
  
  strcat(dev, "eth0");
  strcat(filter_exp, "port 12600");

  
  printf("dev set : %s \n", dev);
  if(pcap_lookupnet(dev, &net, &mask, errBuf) == -1)
  {
	  	fprintf(stderr, "Can't get netmask for device %s \n", dev);
	    net = 0;
		mask =0;	
  }

  handle = pcap_open_live(dev, BUFSIZ, 1, 1000, errBuf);
  if(handle == NULL )
  {
	  printf("Couldn't parse filter %s : %s \n", dev, errBuf);
	  return(2);
  }

  if(pcap_compile(handle, &fp, filter_exp, 0, net) == -1)
  {
	  printf("Err pcap Compile \n");
  }

  if(pcap_setfilter(handle, &fp) == -1)
  {
	  printf("Err set filter \n");
  }
  

  pid_t pid;
  pid = fork();

  /* Packet Analysis */
  if(pid == 0)
  {
	/*
	printf("process init() \n");
	if(pcap_loop(handle, -1, callback, NULL) < 0 )
	{
		printf("err\n");
		exit(1);
	}
	*/
  }
  else
  {

  printf("Event Polling [Egde Trriger] server v1.0 \n");
  /* entry , argument check and process */

  	if(argc < 3) g_svr_port = atoi(argv[1]);
  	else
  	{
   	  if(strcmp("-port",argv[1]) ==  0 )
   	  {
   	     g_svr_port = atoi(argv[2]);
   	     if(g_svr_port < 1024)
   	     {
   	        printf("port number invalid : %d\n",g_svr_port);
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
 	 } 

  }
}

