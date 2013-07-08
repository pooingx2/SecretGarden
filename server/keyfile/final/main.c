#include <stdio.h>
#include <openssl/bn.h>
#include <string.h>
#include <stdlib.h>
#include "header.h"

char* BinaryToBnHex(byte *msg)
{
		BIGNUM *temp;		// Big number

		temp = BN_new();
		BN_init(temp);

		BN_bin2bn(msg, strlen(msg), temp); // binary to BN

		return BN_bn2hex(temp); // BN to hex
}


int main(void)
{
		byte *key;

		char message[128] = "dir id\tpublic id\tpublic pwd\tprivate id\tprivate pwd";

		char *enc_data;
		char *dec_data;

		int enc_data_len;
		int dec_data_len;

		int iRet = 1;
		int size;		// 128 bits (AES)

		FILE *fp = NULL;
		fp = fopen( "result.txt", "w+" );
		if( fp == NULL ){
				printf("error\n");
				return 0;
		}

		key = (byte*)malloc(16 * sizeof(byte));

		printf("original  message = %s\n", message);

		// key Generate(키 생성)
		keyGen(&size, &key);
		printf("Generation Secret Key\n");
		printf("Secret key : %s\n", BinaryToBnHex(key) );

		// AES Encrption(암호화)
		enc_data = NULL;
		enc_data_len = 0;
		if(encryptAES(message,strlen(message)+1, key, &enc_data,&enc_data_len)){
				printf("AES encryption\n" );
				printf("Encyrpted message = %s\n", BinaryToBnHex(enc_data));
				printf("Encrypted message len = %d\n", enc_data_len);
				fprintf( fp, "%s\n", enc_data );
		}
		else{
				printf("AES encryption Error!\n" );
		}

		// AES Decryption(복호화)
		dec_data = NULL;
		dec_data_len = 0;
		if(decryptAES(enc_data, enc_data_len, key, &dec_data, &dec_data_len)) {
				printf("AES decryption\n" );
				printf("Decrypted message = %s\n", dec_data);
				printf("Decrypted message len = %d\n", dec_data_len);
		}
		else{
				printf("AES decryption Error!\n" );
		}

		fclose( fp );

		//메모리 해제  이부분 에러 난다!! 흠..
		//    SAFE_FREE( Secret.key );

		//    SAFE_FREE( enc_data );
		//    SAFE_FREE( dec_data );
		//    free( Secret.key );
		//    free( enc_data );
		//    free( dec_data );

		return 0;
}
