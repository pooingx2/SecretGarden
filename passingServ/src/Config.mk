CC		:= gcc
LD		:= ld
INCLUDES	:= -I. -I$(TOPDIR)/include 
INCXML		:= -I/usr/local/xml/include/libxml2
OPTXML		:= -lxml2
DEFINES		:= -DDEBUG
CFLAGS		:= -O2 -W -Wall $(INCLUDES) $(INCXML) $(OPTXML) $(DEFINES)

######################################################

.SUFFIXDES	: .o .c .S

%.o:%.c
	$(CC) $(CFLAGS) -c $< -o $@
%.o:%.S
	$(CC) $(CFLAGS) -c $< -o $@

######################################################
