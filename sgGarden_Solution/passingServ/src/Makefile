.EXPORT_ALL_VARIABLES:

TARGET	:= passingServ
TOPDIR	:= $(shell /bin/pwd)
SUBDIRS	:= main protocol logHandler epoll packetAnalyzer 
MYSQL   := /usr/include/mysql
PCAP    := pcap

##################################################

include $(TOPDIR)/Config.mk

all : compile $(OBJS)
	$(CC) $(OBJS) $(addsuffix /built-in.o,$(SUBDIRS)) -o $(TARGET) -l$(PCAP) -I$(MYSQL)

include $(TOPDIR)/Rules.mk


