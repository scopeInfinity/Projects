#include <stdio.h>
#include "pqueue.c"

int main(int argc, char const *argv[])
{
	PQUEUE queue;

	while(1) {
	
		printf("\nOptions\n============\n0 - Init\n1 - Insert\n2 - Is Empty\n3 - Extact Max\n4 - Destroy\n5 - Exit");
		char ch;
		printf("\nEnter Choice : ");
		scanf(" %c", &ch);

		int x, y, status;
			
		switch(ch) {
			case '0' : queue = Init(queue);
			break;
			
			case '1' :
			printf("Enter a number : ");
			scanf("%d",&x);
			printf("Status %d\n",Insert(queue,x));
			break;

			case '2' :
			printf("IsEmpty : %s\n",IsEmpty(queue)?"True":"False");
			break;

			case '3' :
			printf("Status %d\n",status = ExtractMax(queue,&y));
			if(status == 0)
				printf("Max : %d\n",y);
			break;

			case '4' :
			Destroy(queue);
			break;

			case '5' :
			return 0;

		}

	}
	return 0;
}