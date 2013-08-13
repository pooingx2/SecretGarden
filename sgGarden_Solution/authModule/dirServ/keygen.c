#include "header.h"
#include <openssl/rand.h>
#include <stdarg.h>

void keyGen(int *size, char **key)
{
		BIGNUM *rnd;			// big number
		int result;

		rnd = BN_new();			// create BN

		*size = SIZE;    	// SIZE = 128 bits (AES)

		// openSSL : void RAND_seed(const void *buf, int num);
		RAND_seed(*key, sizeof(*key));
		// openSSL : int BN_rand(BIGNUM *rnd, int bits, int top, int bottom);
		result = BN_rand(rnd, *size, 1, 0);

		BN_bn2bin(rnd, *key);	//BIGNUM to Binary
		BN_free(rnd);			// free BN
}

char* BinaryToBnHex(byte *msg)
{
	BIGNUM *temp;
	
	temp = BN_new();
	BN_init(temp);
	
	BN_bin2bn(msg, strlen(msg), temp);

	return BN_bn2hex(temp);
}

char* BnHexToBinary(char *org_Hex, char *dst_Binary)
{


}

int build_key_String(char keyString[], int count, ...)
{
	printf("variable init \n");
	va_list key_Element;
	int i, sum;

	printf("va access \n");
	va_start(key_Element,  count);

	printf("strcat undefined length variable \n");
	for(i = 0; i< count; i++)
	{
		printf("va_arg init() \n");
		va_arg(key_Element, char);
		//strcat(keyString, va_arg(key_Element, char *));
		//strcat(keyString, "\t");
	}
	printf("va_arg end \n");
	va_end(key_Element);
	
	return i;
}
