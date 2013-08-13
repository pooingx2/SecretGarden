#include <openssl/bn.h>
#include <openssl/rand.h>
#include <openssl/aes.h>
#include <openssl/evp.h>

#define KEYSIZE 128		// AES key size
#define SIZE 128				// Session key size

#define TRUE    1
#define FALSE    0

typedef unsigned char byte;
static unsigned char IVseedConstant[AES_BLOCK_SIZE] = { 
		0xcb, 0xce, 0xcb, 0xcd, 0xcb, 0xce, 0xcb, 0xcd,
		0xcb, 0xce, 0xcb, 0xcd, 0xcb, 0xce, 0xcb, 0xcd 
}; // Initialize AES IV

void  
keyGen(int *size, char **key);

char* 
BinaryToBnHex(byte *msg);

char*
BnHexToBinary(char *org_Hex, char *dst_Binary);

int 
build_key_String(char keyString[], int count, ...);


