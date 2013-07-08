SOURCES = decrypt.c encrypt.c keygen.c main.c
OBJECTS = ${SOURCES:.c=.o}
OUT = keygen

$(OUT): $(OBJECTS)
	gcc -o $(OUT) $(OBJECTS) -lcrypto

clean:
	rm -f test *.o
