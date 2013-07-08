#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/rsa.h>
#include <openssl/evp.h>
int main(int argc, char *argv[])
{
		RSA *rsa;
		int len, ret;
		unsigned char *cipherText;
		unsigned char *plainText = "Hello RSA!";
		unsigned char *originText;

		rsa = RSA_generate_key(1024, 3, NULL, NULL);

		if (RSA_check_key(rsa) != 1)
		{
				perror("not gnerate RSA key");
				exit(0);
		}

		cipherText = (unsigned char *) calloc (1, (size_t) RSA_size(rsa));

		len = RSA_public_encrypt(strlen(plainText), plainText, cipherText, rsa, RSA_PKCS1_PADDING);

		if (len == -1)
		{
				perror("not encrypt data");
				exit(0);
		}

		printf("plain text: %s\n", plainText);
		printf("ciper text: %s\n", cipherText);

		originText = (unsigned char *) calloc(1, strlen(plainText)+1);

		ret = RSA_private_decrypt(len, cipherText, originText, rsa, RSA_PKCS1_PADDING);

		if (ret == -1)
		{
				perror("not decrypt data");
				exit(0);
		}

		printf("origin text: %s\n", originText);

		free(cipherText);
		free(originText);
		RSA_free(rsa);

		return 0;
}
