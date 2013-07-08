#include <stdio.h>
#include <stdlib.h>
typedef unsigned char byte;

int byteToInt(byte *buf, int offset) {
		int unit_digit, ten_digit, hund_digit, thos_digit;

		unit_digit = buf[offset] << 24;
		ten_digit = buf[offset+1] << 16;
		hund_digit = buf[offset+2] << 8;
		thos_digit = buf[offset+3] << 0;

		return (unit_digit+ten_digit+hund_digit+thos_digit);
}

// int를 버퍼의 offset부터 4바이트의 공간에 저장
void intToByte(int n, byte *buf, int offset){
		buf[offset+0] = (n>>24);
		buf[offset+1] = (n>>16);
		buf[offset+2] = (n>>8);
		buf[offset+3] = (n>>0);
}

int main() {
		byte b[100];
		intToByte(12123, b, 0);
		printf("%d ",b[0]);
		printf("%d ",b[1]);
		printf("%d ",b[2]);
		printf("%d ",b[3]);
		
		printf("%d ",(byteToInt(b,0)));
}
