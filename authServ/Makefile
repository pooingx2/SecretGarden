all : test

test : main.o protocol.o auth.o
	gcc -m32 -W -Wall -o test main.o protocol.o auth.o -I/usr/include/mysql -L/usr/lib -lmysqlclient

main.o : main.c 
	gcc -m32 -W -Wall -c -o main.o main.c -I/usr/include/mysql -L/usr/lib -lmysqlclient

protocol.o : protocol.c 
	gcc -m32 -W -Wall -c -o protocol.o protocol.c

auth.o : auth.c
	gcc -m32 -W -Wall -c -o auth.o auth.c -I/usr/include/mysql -L/usr/lib -lmysqlclient
