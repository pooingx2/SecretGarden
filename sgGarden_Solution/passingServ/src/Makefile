.EXPORT_ALL_VARIABLES:

TARGET	:= passingServ
TOPDIR	:= $(shell /bin/pwd)
SUBDIRS	:= main epoll protocol xmlHandler packetAnalyzer
LIBXML  := /usr/local/xml/include/libxml2
MYSQL   := /usr/include/mysql
OPTX    := xml2
PCAP    := pcap

##################################################

include $(TOPDIR)/Config.mk

all : compile $(OBJS)
	$(CC) $(OBJS) $(addsuffix /built-in.o,$(SUBDIRS)) -o $(TARGET) -I$(LIBXML) -l$(OPTX) -l$(PCAP) -I$(MYSQL)

include $(TOPDIR)/Rules.mk


