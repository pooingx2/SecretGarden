.EXPORT_ALL_VARIABLES:

TARGET	:= dirServ
TOPDIR	:= $(shell /bin/pwd)
SUBDIRS	:= main skeleton xmlHandler
EXLIB	:= -I/usr/local/xml/include/libxml2
OPTX    := -lxml2

##################################################

include $(TOPDIR)/Config.mk

all : compile $(OBJS)
	$(CC) $(OBJS) $(addsuffix /built-in.o,$(SUBDIRS)) -o $(TARGET) $(EXLIB) $(OPTX)

include $(TOPDIR)/Rules.mk


