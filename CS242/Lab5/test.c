#include<stdio.h>
int main(int argc, char const *argv[])
{
	if(argc==1)
		return 1;//Error
	printf("Message : %s", argv[1]);
	return 0;
}