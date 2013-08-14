#include <stdio.h>

#include <sys/types.h>
#include <sys/time.h>

#include <sys/socket.h>

#include <netinet/in.h>
#include <netinet/in_systm.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>

#include <netinet/ip.h>
#include <netinet/tcp.h>
#include <netinet/udp.h>
#include <netinet/if_ether.h>
#include <netinet/ip_icmp.h>

#include <pcap.h>

/* Extern Variable */

pcap_t  			*handle;
char				dev[10];
char 				errBuf[PCAP_ERRBUF_SIZE];
struct 	bpf_program fp;
char				filter_exp[15];
bpf_u_int32 			mask;
bpf_u_int32			net;
struct pcap_pkthdr		header;
const  u_char			*packet;



/* Mac 주소 표시 */
void print_hwadd(u_char *hwadd);

/* 패킷 데이터 표시 */
void packet_print(u_char *user, const struct pcap_pkthdr *h, const u_char *p);

/* 패킷을 받아들일 경후 함수를 호출한다  */
void callback(u_char *useless, const struct pcap_pkthdr *pkthdr, const u_char *packet);

