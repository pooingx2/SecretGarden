all : dirServ

dirServ : main.o protocol.o dirs.o keygen.o encrypt.o decrypt.o 
	gcc -W -Wall -o dirServ  main.o protocol.o dirs.o keygen.o encrypt.o decrypt.o -I/usr/include/mysql -L/usr/lib -lmysqlclient -lcrypto

main.o : main.c 
	gcc -W -Wall -c -o main.o main.c -I/usr/include/mysql -L/usr/lib -lmysqlclient

protocol.o : protocol.c 
	gcc -W -Wall -c -o protocol.o protocol.c

dirs.o : dirs.c
	gcc -W -Wall -c -o dirs.o dirs.c -I/usr/include/mysql -L/usr/lib -lmysqlclient

keygen.o : keygen.c
	gcc -W -Wall -c -o keygen.o keygen.c

encrypt.o : encrypt.c
	gcc -W -Wall -c -o encrypt.o encrypt.c

decrypt.o : decrypt.c
	gcc -W -Wall -c -o decrypt.o decrypt.c
