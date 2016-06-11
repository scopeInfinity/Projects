
main: main.o motion.o printfloor.o
	gcc -o main main.o motion.o printfloor.o
motion.o: motion.c
	gcc -c motion.c
printfloor.o: printfloor.c
	gcc -c printfloor.c
main.o: motion.c
	gcc -c main.c
