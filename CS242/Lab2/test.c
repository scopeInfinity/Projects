#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>

#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char const *argv[])
{
	fork();
	fork();
	printf("Hello\n");
	return 0;
}