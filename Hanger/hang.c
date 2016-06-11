#include <stdio.h>
#include <stdlib.h>

int main() {
	int *p;
	int c=0;
	do
	{
	p=malloc(10000000);
	printf("%d\n",++c );
	} while (p!=NULL);
	return 0;


}

