.EXPORT_ALL_VARIABLES:

TARGET	:= passingServ
TOPDIR	:= $(shell /bin/pwd)
SUBDIRS	:= main epoll protocol

##################################################

include $(TOPDIR)/Config.mk

all : compile $(OBJS)
	$(CC) $(OBJS) $(addsuffix /built-in.o,$(SUBDIRS)) -o $(TARGET) 

include $(TOPDIR)/Rules.mk


