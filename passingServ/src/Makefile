.EXPORT_ALL_VARIABLES:

TARGET	:= passingServ
TOPDIR	:= $(shell /bin/pwd)
SUBDIRS	:= main xmlHandler epoll protocol
EXLIB	:= -I/usr/local/xml/include/libxml2
OPTX    := -lxml2

##################################################

include $(TOPDIR)/Config.mk

all : compile $(OBJS)
	$(CC) $(OBJS) $(addsuffix /built-in.o,$(SUBDIRS)) -o $(TARGET) $(EXLIB) $(OPTX)

include $(TOPDIR)/Rules.mk


