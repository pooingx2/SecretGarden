#include "macAddressCapture.h"

/* Mac 주소 표시 */
/*
void print_hwadd(u_char *hwadd)
{
	 int i;

	 for(i=0; i<5; i++)
	 {
		 printf("%2x : ", hwadd[i]);
	 }
	 printf("%2x", hwadd[i]);
}
*/
/* 패킷 데이터 표시 */
/*
void 
packet_print(u_char *user, const struct pcap_pkthdr *h, const u_char *p)
{
	struct ether_header *eth;
	int i;

	eth = (struct ether_header *) p;

	printf("발신측 : ");
	print_hwadd(eth->ether_shost); 
	printf("-> 수신측 : ");
	printf("\n");
}
*/

void callback(u_char *useless, const struct pcap_pkthdr *pkthdr, const u_char *packet)
{


	printf("Call Callback function \n");
	struct ip *iph;
	struct tcphdr *tcph;

	static int count = 1;
	struct ether_header *ep;
	unsigned short ether_type;
	int chcnt  = 0;
	int length = pkthdr->len;



	/*
	ep = (struct ether_header *)packet;

	packet += sizeof(ep);

	ether_type = ntohs(ep->ether_type);
	*/
	/*
	if(ether_type == ETHERTYPE_IP)
	{
		iph = (struct ip *)packet;
		printf("IP Packet\n");
	}	
	*/
}



