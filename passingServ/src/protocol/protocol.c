#include "protocol.h"

int 
readHeader
(Peer *peer, byte *headerBuf, int headerSize)
{

		int readSize;
		int needSize;
		

		readSize=0;
		needSize=headerSize;
			
		while(needSize > 0){
				
				readSize+=read(peer->socket, headerBuf, needSize);
			//	printf("readheader : %d \n", readSize);
				if(readSize < 0 || readSize == 0)
				{
			//		printf("return \n");
					return 0;
				}
				needSize=needSize-readSize;
				
		}

	//	printf("readSize : %d \n", readSize);
		return readSize;
}

int 
readData
(Peer *peer, byte *dataBuf, int dataSize)
{
		
		int readSize;
		int needSize;

		readSize=0;
		needSize=dataSize;
		
		
		while(needSize > 0){
				readSize+=read(peer->socket, dataBuf, needSize);
				needSize=needSize-readSize;
				
		}
		return readSize;
}

// 프로토콜을 해석하기 위해  TCP버퍼에서 데이터를 읽어들여  header와 data로 분리하여 저장한다.
int 
recvFrom
(Peer *peer, byte *headerBuf, byte *dataBuf)
{	
	 	int headerSize = readHeader(peer, headerBuf, HEADERSIZE);
		
		if(headerSize < 1 )
		{
	//		printf("Non Header \n");
			return 0;
		}
 
		int length = byteToInt(headerBuf, 8);
		int dataSize   = readData(peer  , dataBuf,   length );

		printf("[ from %s ] type : %d, redesc : %d length : %d data : %s \n",
		 peer->ip, byteToInt(headerBuf,0),
                 byteToInt(headerBuf, 4),
                 byteToInt(headerBuf, 8),
                 dataBuf);

		return (headerSize + dataSize);
}

int 
byteToInt
(byte *buf, int offset)
{

		int unit_digit, ten_digit, hund_digit, thos_digit;

		unit_digit = buf[offset] << 24;
		ten_digit = buf[offset+1] << 16;
		hund_digit = buf[offset+2] << 8;
		thos_digit = buf[offset+3] << 0;

		return (unit_digit+ten_digit+hund_digit+thos_digit);
}

void 
intToByte(int n, byte *buf, int offset)
{

		buf[offset+0] = n>>24;
		buf[offset+1] = n>>16;
		buf[offset+2] = n>>8;
		buf[offset+3] = n>>0;
}


int 
sendTo
(Peer *peer, int type, int redescriptor, int length , byte *dataBuf)
{
		
		byte sendBuf[HEADERSIZE+length];		

		int headerSize = writeHeader(peer,  
					     type,
					     redescriptor, 
	    				     length,
				             sendBuf);
	       	
		int dataSize   = writeData  (peer,  length, sendBuf ,dataBuf);

		printf("[  to  %s ] type : %d, redesc : %d, length : %d, data : %s \n", 
		      peer->ip,
                      type,
                      redescriptor, 
                      length,
                      dataBuf);

		return (12 + dataSize);
}

int 
writeHeader(Peer *peer, int type, int redescriptor, int length, byte *buf )
{
		

		
		//byte headerBuf[HEADERSIZE];

		intToByte(type,buf,0);
		intToByte(length,buf,8);
		intToByte(redescriptor,buf,4); 		

		//int headerSize =  write(peer->socket,headerBuf,HEADERSIZE);
		
		return 12;
}

int 
writeData(Peer *peer, int length, byte *buf,byte *dataBuf)
{
		int offset;
 				
		
		for(offset = 12; offset < 12+length; offset++)
		{
			buf[offset] = dataBuf[offset-12];
		}
		//byte headerBuf[DATASIZE];
		int dataSize = write(peer->socket,buf,length+12);
		return dataSize;
}


int
joinToPassingServ(char *servIP, char *servPort, int servType)
{

		int    serv_sockfd;
		int    client_sockfd;
		int    client_len;
		int    data_len;
		struct sockaddr_in client_addr;

		unsigned char headerBuf[HEADERSIZE];
		unsigned char dataBuf[DATASIZE];

		memset(headerBuf, 0x00, HEADERSIZE);
		memset(dataBuf, 0x00,   DATASIZE);
  		
		client_sockfd = socket(AF_INET, SOCK_STREAM, 0);
		client_addr.sin_family = AF_INET;
		client_addr.sin_addr.s_addr = inet_addr(servIP);
		client_addr.sin_port = htons(atoi(servPort));
  		//client_addr.sin_addr.s_addr = htons(atoi(servIP));
		//client_addr.sin_port = htons(atoi(servPort));

		client_len = sizeof(client_addr);
		
		//Connect 
		serv_sockfd = connect(client_sockfd, (struct sockaddr *)&client_addr, client_len);
		if(serv_sockfd < 0)
		{
			printf("connect error\n");
		}
		
		strcpy(dataBuf, "Binding");		
		data_len = strlen(dataBuf);		

		//Write
		Peer passingServ;
		passingServ.socket = client_sockfd;
		printf("sock_fd : %d, %d", passingServ.socket, serv_sockfd);

		sendTo(&passingServ, servType, 0, data_len, dataBuf);
	
		//Read
		recvFrom(&passingServ, headerBuf, dataBuf);		
		
		return passingServ.socket;
}


int
isRunning(int socket)
{
	
}
