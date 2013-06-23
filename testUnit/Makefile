all : test

test : main.o protocol.o
	gcc -W -Wall -o test main.o protocol.o

main.o : main.c
	gcc -W -Wall -c -o main.o main.c

protocol.o : protocol.c
	gcc -W -Wall -c -o protocol.o protocol.c
