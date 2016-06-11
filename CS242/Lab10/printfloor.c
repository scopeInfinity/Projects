#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include "motion.h"

const char *DIRECTIONS[] = {"NorthWest","West","SouthWest","South","SouthEast","East","NorthEast","North"};


void printNode(int n) {
	char str[100];
	int l=0,i;
	str[l++]='|';
	str[l++]='0'+n;	//Node Index
	str[l++]='|';
	int row = Nodes[n-1].row;
	int column = Nodes[n-1].column;
	int direction = Nodes[n-1].direction;

	//Floor
	if(row==10)
		str[l++]='1',str[l++]='0';
	else 
		str[l++]='0'+row;
	str[l++]='|';
	
	//ROOM
	if(column==10)
		str[l++]='1',str[l++]='0';
	else 
		str[l++]='0'+column;
	str[l++]='|';
	str[l]='\0';
	strcat(str,DIRECTIONS[direction]);
	l = strlen(str);
	str[l++]='|';
	str[l++]='\0';
	for (i = 0; i <l ; ++i)
		printf("-");
	printf("\n");
	printf("%s\n", str);
	for (i = 0; i <l ; ++i)
		printf("-");
	printf("\n");
		
}

void printAll() {
	printf("\nBuilding\n");
	int r,c,i;
	for (r = 0; r < ROW; ++r)
	{
		for (c = 0; c < COLUMN; ++c)
		{
			int node = 0;//No Node
			for (i = 0; i < NODES; ++i)
			{
				if(Nodes[i].row-1 == r && Nodes[i].column-1 == c)
					node = i+1;
			}
			if(node==0)
				printf(".");
			else
				printf("%d",node );
		}
		printf("\n");
	}
	printf("\n");
	sleep(1);
}

void printNodes() {
	int i;
	for (i = 1; i <= NODES; ++i)
	{
		printNode(i);
	}
}
