#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <openssl/evp.h>
#include <openssl/rsa.h>
#include <openssl/bio.h>
#include <openssl/pem.h>
#include <openssl/objects.h>
#include <openssl/err.h>
#include <openssl/x509.h>

int main()
{

		//
		// Local variables definition
		//
		EVP_PKEY_CTX    *evp_ctx    = NULL;
		EVP_PKEY        *ppkey      = NULL;
		FILE            *fpPri      = NULL;
		FILE            *fpPub      = NULL;
		int             retValue    = 1;

		for (;;)
		{
				//
				// Function allocates public key algorithm context using the algorithm
				// specified by id
				//
				evp_ctx = EVP_PKEY_CTX_new_id(EVP_PKEY_RSA, NULL);
				if (NULL == evp_ctx)
				{
						printf("RSA Public key algorithm context is not allocated\n");
						break;
				} // if
				printf("RSA Public key algoritm context allocated\n");

				//
				// Function initializes a public key algorithm context using key pkey
				// for a key genration operation
				//
				retValue = EVP_PKEY_keygen_init(evp_ctx);
				if (1 != retValue)
				{
						printf("Initialization of public key alogorithm context failed\n");
						break;
				} // if
				printf("Public key alogorithm context initialized\n");

				//
				// Setting RSA key bit to 2048
				//
				retValue = EVP_PKEY_CTX_set_rsa_keygen_bits(evp_ctx, 2048);
				if (1 != retValue)
				{
						printf("RSA key bits not set to 2048\n");
						break;
				} // if
				printf("RSA key bits set to 2048\n");

				//
				// Function performs a key generation operation
				//
				retValue = EVP_PKEY_keygen(evp_ctx, &ppkey);
				if (1 != retValue)
				{
						printf("Key generation operation failed\n");
						break;
				} // if
				printf("Key generated successfully\n");

				//
				// Creating a file to store RSA private key
				//
				fpPri = fopen("./private.pem", "w+");
				if (NULL == fpPri)
				{
						printf("File pointer of private.pem file is not opened\n");
						break;
				} // if
				printf("File pointer or private.pem file opened\n");

				retValue = PEM_write_PrivateKey(fpPri, ppkey, NULL, NULL, 0, 0, NULL);
				if (1 != retValue)
				{
						printf("Private key is not written to file private.pem\n");
						break;
				} // if
				printf("Private key written to file private.pem\n");

				//
				// Final break statement
				//
				break;
		} // for

		//
		// Releasing all the memory allocations and the handles
		//

		getchar();

		return 0;
}
