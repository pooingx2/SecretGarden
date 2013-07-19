CC		:= gcc
LD		:= ld
INCLUDES	:= -I. -I$(TOPDIR)/include 
DEFINES		:= -DDEBUG
LIBSSL 		:= -lcrypto
CFLAGS		:= -O2 -W -Wall $(INCLUDES) $(DEFINES) $(LIBSSL)

######################################################

.SUFFIXDES	: .o .c .S

%.o:%.c
	$(CC) $(CFLAGS) -c $< -o $@
%.o:%.S
	$(CC) $(CFLAGS) -c $< -o $@

######################################################
