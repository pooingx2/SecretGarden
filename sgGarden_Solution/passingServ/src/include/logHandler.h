#include <stdio.h>
#include <stdlib.h>

#include "epoll.h"
#include "protocol.h"

int
log_line_init();

int
log_user_name(int sock, Peer *peer);

int
log_user_ip(int sock, Peer *peer);

int
log_user_packet(int sock, Peer *peer, char *packet);

int
log_user_direction(int sock);

int 
log_user_pType(int type);

int
log_line_feed();
