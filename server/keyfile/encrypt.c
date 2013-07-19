#include "header.h"

int SSU_SEC_aes_encrypt(uchar *msg, int msg_len, uchar *sk, int sk_len, uchar **enc_msg, int *enc_msg_len )
{
		// 입력 파라미터: uchar *msg; int msg_len; uchar *sk; int sk_len;
		// 출력 파라미터: uchar **enc_msg; int *enc_msg_len;
		// 내부변수
		int ret = TRUE;
		int templen;

		// EVP 객체를 이용한 AES 암호화
		// ctx(암호화에 사용되는 데이터들이 저장되는 임시 저장소) 초기화
		EVP_CIPHER_CTX ctx;
		EVP_CIPHER_CTX_init(&ctx);

		// 초기화: 암호 알고리즘 할당, 키 할당, IV 할당
		EVP_EncryptInit_ex(&ctx, EVP_des_ofb(), NULL, sk, IVseedConstant);

		// 초기화 끝난 후에 암호문 저장될 메모리 할당
		*enc_msg = malloc( msg_len + EVP_CIPHER_CTX_block_size(&ctx) );
		if( *enc_msg == NULL )
				return FALSE;

		// 업데이트: 메시지 암호화(마지막 블록 제외)
		if(!EVP_EncryptUpdate(&ctx, *enc_msg, enc_msg_len, msg, msg_len))
				ret = FALSE;

		// 종료: 마지막 블록 암호화
		if(!EVP_EncryptFinal_ex(&ctx, *enc_msg+(*enc_msg_len), &templen))
				ret = FALSE;

		// 암호문 길이
		*enc_msg_len += templen;

		EVP_CIPHER_CTX_cleanup(&ctx);

		return ret;
}
