#include <stdio.h>
#include <string.h>
int main () {
		unsigned char a[1024];
		strcpy(a,"");
		//a="asdfasdf";
		printf("%d",sizeof(a));
		printf("%d",strlen(a));
}
