#include "protocol.h"

int 
readHeader
(Client *client, byte *headerBuf, int headerSize){

		int readSize;
		int needSize;

		readSize=0;
		needSize=headerSize;
			

		while(needSize > 0){
				
				readSize+=read(client->socket, headerBuf, needSize);
				needSize=needSize-readSize;
				
		}
		
		return readSize;
}

int 
readData
(Client *client, byte *dataBuf, int dataSize){
		
		int readSize;
		int needSize;

		readSize=0;
		needSize=dataSize;
		
		
		while(needSize > 0){
				
				readSize+=read(client->socket, dataBuf, needSize);
				needSize=needSize-readSize;
				
		}
		
		return readSize;
}

int 
recvFrom
(Client *client, byte *headerBuf, byte *dataBuf)
{	
	 	int headerSize = readHeader(client, headerBuf, HEADERSIZE);

		int length = byteToInt(headerBuf, 4);
		
		int dataSize   = readData(client  , dataBuf,   length );

		printf("[ from %s ] type : %d, length : %d data : %s \n", client->ip, byteToInt(headerBuf,0)
									, byteToInt(headerBuf, 4), dataBuf);

		return (headerSize + dataSize);
}

int 
byteToInt
(byte *buf, int offset) {

		int unit_digit, ten_digit, hund_digit, thos_digit;

		unit_digit = buf[offset] << 24;
		ten_digit = buf[offset+1] << 16;
		hund_digit = buf[offset+2] << 8;
		thos_digit = buf[offset+3] << 0;

		return (unit_digit+ten_digit+hund_digit+thos_digit);
}

int 
sendTo
(Client *client, int type, int length, byte *buf) {

		
		byte headerBuf[HEADERSIZE];
		byte dataBuf[DATASIZE];
		
		int headerSize = writeHeader(client,  type  ,length);
		int dataSize   = writeData  (client,  length,buf);

		printf("[  to  %s ] type : %d, length : %d, data : %s \n", client->ip, type, length, buf);

		return (headerSize + dataSize);
}

int 
writeHeader(Client *client, int type, int length){
		
		byte headerBuf[HEADERSIZE];

		intToByte(type,headerBuf,0);
		intToByte(length,headerBuf,4);

		int headerSize =  write(client->socket,headerBuf,HEADERSIZE);
		
		return headerSize;
}

int 
writeData(Client *client, int length, byte *dataBuf){

		byte headerBuf[DATASIZE];
		int dataSize = write(client->socket,dataBuf,length);

		return dataSize;
}

void 
intToByte(int n, byte *buf, int offset){

		buf[offset+0] = n>>24;
		buf[offset+1] = n>>16;
		buf[offset+2] = n>>8;
		buf[offset+3] = n>>0;
}
