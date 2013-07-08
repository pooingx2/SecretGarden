#include "header.h"
#include <openssl/rand.h>

void keyGen(int *size, byte **key)
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
