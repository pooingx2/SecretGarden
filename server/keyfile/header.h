#include <openssl/bn.h>
#include <openssl/rand.h>
#include <openssl/aes.h>
#include <openssl/evp.h>

#define AES_KEY_SIZE 128           // AES key size
#define SK_SIZE 128              // Session key size

#define TRUE    1
#define FALSE    0

typedef unsigned char uchar;
static unsigned char IVseedConstant[AES_BLOCK_SIZE] = { 
		0xcb, 0xce, 0xcb, 0xcd, 0xcb, 0xce, 0xcb, 0xcd,
		0xcb, 0xce, 0xcb, 0xcd, 0xcb, 0xce, 0xcb, 0xcd 
}; // Initialize AES IV
