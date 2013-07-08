SOURCES = decrypt.c encrypt.c keygen.c test.c
OBJECTS = ${SOURCES:.c=.o}
OUT = test

$(OUT): $(OBJECTS)
	gcc -o $(OUT) $(OBJECTS) -lcrypto

clean:
	rm -f test *.o
