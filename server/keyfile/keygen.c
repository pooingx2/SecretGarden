#include "header.h"
#include <openssl/rand.h>

int SSU_SEC_sk_gen(int *sk_size, uchar **sk, int *sk_len)
{
		BIGNUM *rnd;
		int ret;

		rnd = BN_new();

		*sk_size = SK_SIZE;    // SK_SIZE = 128 bits (for AES)

		RAND_seed(*sk, sizeof(*sk));            // random seed
		ret = BN_rand(rnd, *sk_size, 1, 0);

		*sk_len = BN_num_bytes(rnd);
		BN_bn2bin(rnd, *sk);

		BN_free(rnd);

		return ret;
}
