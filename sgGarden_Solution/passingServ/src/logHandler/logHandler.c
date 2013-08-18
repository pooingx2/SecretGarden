#include <stdio.h>
#include "protocol.h"
#include "epoll.h"
#include "logHandler.h"



int
log_user_name(int sock, Peer *peer)
{

	// sock 번호를 기반으로 ID를 조회하기 위한 INDEX  획득
	int   pf;
	int    i;
	int  temp;
	char buf[30];

	memset(buf, 0x00, 20);
	

	for(i =0; i<100; i++)
	{
		if(sock == peer[i].socket)
		{
			break;
		}
	}


	/* 파일 [열고] [쓰고] [닫기] */
	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	if(peer[i].userName[0] == 0 )
	{
		strcat(buf, "ID : Not AUTH");
		fputs(buf, pf);
		
	}
	else
	{
		printf("Id recongnize \n");
		strcat(buf, "ID :  ");
		strcat(buf, &peer[i].userName);
		fputs(buf, pf);
	}
	  
	
	fclose(pf);
	return 1;
}


int
log_user_ip(int sock, Peer *peer)
{

	// sock 번호를 기반으로 ID를 조회하기 위한 INDEX  획득
	int   pf;
	int    i;
	int  temp;
	char buf[20];

	memset(buf, 0x00, 20);
	

	for(i =0; i<100; i++)
	{
		if(sock == peer[i].socket)
		{
			break;
		}
	}


	/* 파일 [열고] [쓰고] [닫기] */
	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	strcat(buf, " IP : ");
	strcat(buf, &peer[i].ip);
	fputs(buf, pf);
	fclose(pf);

	return 1;
}


int
log_user_packet(int sock, Peer *peer, char *packet)
{

	// sock 번호를 기반으로 ID를 조회하기 위한 INDEX  획득
	int   pf;
	int    i;

	for(i =0; i<100; i++)
	{
		if(sock == peer[i].socket)
		{
			break;
		}
	}


	/* 파일 [열고] [쓰고] [닫기] */
	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	fputs(packet, pf);
	fclose(pf);

	return 1;
}

int
log_user_ptype(int ptype)
{
	int  pf;
	char ptype_str[4];

	sprintf(ptype_str, "%d", ptype);

	/* 파일 [열고] [쓰고] [닫기] */
	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	fputs(" TYPE : ", pf);
	fputs(ptype_str, pf);
	fclose(pf);
}

int
log_line_feed()
{
	int  pf;
	char *buf = "]\n";

	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	fputs(buf, pf);
	fclose(pf);

	return 1;

}

int
log_line_init()
{
	int  pf;
	char *buf = "[";

	if( (pf = fopen("log.txt", "a+")) == NULL)
	{
		fputs("Cannot Open File \n", stderr);
	}

	fputs(buf, pf);
	fclose(pf);

	return 1;

}


