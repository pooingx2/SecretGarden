#include <stdio.h>
#include <openssl/bn.h>
#include <string.h>
#include <stdlib.h>

typedef unsigned char uchar;

typedef struct
{
		uchar *key;
		int key_len;
} SSU_SEC_key;

char* BinaryToBN( uchar *msg, int msg_len )
{
		BIGNUM *temp;

		temp = BN_new();
		BN_init(temp);

		BN_bin2bn(msg, msg_len, temp); // binary to BN

		return BN_bn2hex(temp); // BN to hex
}


int main(void)
{
		SSU_SEC_key Secret;

		char message[128] = "dir_id\tpublic id\tpublic pwd\tprivate id\tprivate pwd";

		char *enc_data;
		char *dec_data;

		int enc_data_len;
		int dec_data_len;

		int iRet = 1;
		int sk_size;

		FILE *fp = NULL;
		fp = fopen( "result.txt", "w+" );
		if( fp == NULL )
				return 0;

		Secret.key = (uchar *)malloc( 16 * sizeof(uchar) );
		Secret.key_len = sizeof(Secret.key);

		fprintf( fp, "<main>\n" );
		fprintf( fp, "original  message = %s\n\n", message);

		// 0. key Gen
		if( SSU_SEC_sk_gen(&sk_size, &Secret.key, &Secret.key_len) )
		{
				fprintf( fp , "\t* Generation Secret Key \n");
				fprintf( fp , "\tSecret key len : %d bytes\n", Secret.key_len) ;
				fprintf( fp , "\tSecret key : %s\n\n", BinaryToBN( Secret.key, Secret.key_len) );
		}

		// 1. AES Encrption
		enc_data = NULL;
		enc_data_len = 0;
		if( SSU_SEC_aes_encrypt( message, strlen( message ) + 1, Secret.key, Secret.key_len, &enc_data, &enc_data_len ) )
		{
				fprintf( fp, "* AES encryption OK.\n" );
				fprintf( fp, "Encyrpted message = %s\n", BinaryToBN( enc_data, enc_data_len ) );
				fprintf( fp, "Encrypted message len = %d\n", enc_data_len );
		}
		else
		{
				fprintf( fp, "* AES encryption fail!\n" );
				iRet = 0;
				goto END;
		}

		// 2. AES Decryption
		dec_data = NULL;
		dec_data_len = 0;
		if( SSU_SEC_aes_decrypt( enc_data, enc_data_len, Secret.key, Secret.key_len, &dec_data, &dec_data_len ) )
		{
				fprintf( fp, "\n* AES decryption OK.\n" );
				fprintf( fp, "Decrypted message = %s\n", dec_data );
				fprintf( fp, "Decrypted message len = %d\n", dec_data_len );
		}
		else
		{
				fprintf( fp, "* AES decryption fail!\n" );
				iRet = 0;
				goto END;
		}

END:
		fprintf( fp, "++++++++++++ END of PROGRAM ++++++++++++\n" );
		fclose( fp );

		//메모리 해제  이부분 에러 난다!! 흠..
		//    SAFE_FREE( Secret.key );

		//    SAFE_FREE( enc_data );
		//    SAFE_FREE( dec_data );
		//    free( Secret.key );
		//    free( enc_data );
		//    free( dec_data );

		return 1;
}
