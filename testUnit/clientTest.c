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
   joinToPassingServ("127.0.0.1", "12500");

}

