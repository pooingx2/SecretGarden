all : dirServ

dirServ : main.o protocol.o dirs.o
	gcc -W -Wall -o dirServ  main.o protocol.o dirs.o -I/usr/include/mysql -L/usr/lib -lmysqlclient

main.o : main.c 
	gcc -W -Wall -c -o main.o main.c -I/usr/include/mysql -L/usr/lib -lmysqlclient

protocol.o : protocol.c 
	gcc -W -Wall -c -o protocol.o protocol.c

dirs.o : dirs.c
	gcc -W -Wall -c -o dirs.o dirs.c -I/usr/include/mysql -L/usr/lib -lmysqlclient
