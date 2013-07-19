ifndef TOPDIR
TOPDIR := ..
endif

#########################################################

include $(TOPDIR)/Config.mk

TARGET	:= built-in.o
SUBDIRS := 
OBJS	:= $(patsubst %.S, %.o, $(wildcard *.S))
OBJS	+= $(patsubst %.c, %.o, $(wildcard *.c))
LIBXML  := /usr/local/xml/include/libxml2
OPTX    := xml2

##all : compile $(OBJS)
##	$(CC) $(OBJS) -o $(TARGET) -I$(LIBXML) -l$(OPTX)

all : compile $(OBJS)
	$(LD) -r $(addsuffix /$(TARGET),$(SUBDIRS)) $(OBJS) -o $(TARGET) -I$(LIBXML) 
 
include $(TOPDIR)/Rules.mk
