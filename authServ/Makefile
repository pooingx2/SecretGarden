all : test

test : main.o protocol.o epoll.o auth.o
	gcc -W -Wall -o test main.o protocol.o epoll.o auth.o -I/usr/include/mysql -L/usr/lib -lmysqlclient

main.o : main.c 
	gcc -W -Wall -c -o main.o main.c -I/usr/include/mysql -L/usr/lib -lmysqlclient

protocol.o : protocol.c 
	gcc -W -Wall -c -o protocol.o protocol.c

epoll.o : epoll.c 
	gcc -W -Wall -c -o epoll.o epoll.c

auth.o : auth.c
	gcc -W -Wall -c -o auth.o auth.c -I/usr/include/mysql -L/usr/lib -lmysqlclient
