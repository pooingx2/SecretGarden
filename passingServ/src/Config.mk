CC		:= gcc
LD		:= ld
INCLUDES	:= -I. -I$(TOPDIR)/include 
DEFINES		:= -DDEBUG
LIBSSL 		:= -lcrypto
LIBXML  := /usr/local/xml/include/libxml2
OPTX  := xml2
CFLAGS		:= -O2 -W -Wall $(INCLUDES) $(DEFINES) $(LIBSSL) -I$(LIBXML) -L$(OPTX)


######################################################

.SUFFIXDES	: .o .c .S

%.o:%.c
	$(CC) $(CFLAGS) -c $< -o $@
%.o:%.S
	$(CC) $(CFLAGS) -c $< -o $@



