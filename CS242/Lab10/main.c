#include "motion.h"
#include <stdio.h>
#include <time.h>

int main(int argc, char const *argv[])
{
	srand(time(NULL));
	
	int i;
	for (i = 0; i < NODES; ++i)
		Nodes[i].row = Nodes[i].column = -1;
	
	for (i = 0; i < NODES; ++i)
	{
		int nid,r,c;
		printf("Enter Node ID, Floor and Room No -\n");
		scanf("%d %d %d", &nid, &r, &c);
		if(nid<1 || nid>NODES)
		{
			printf("Invalid Node ID[1-%d Only]\n",NODES);
			i--;
			continue;
		}
		if(Nodes[nid-1].row!=-1) {
			printf("Node Already Provided!\n");
			i--;
			continue;
		}
		if(r<1 && r>ROW)
		{
			printf("Invalid ROW ID[1-%d Only]\n",ROW);
			i--;
			continue;
		}
		if(c<1 && c>COLUMN)
		{
			printf("Invalid Node ID[1-%d Only]\n",COLUMN);
			i--;
			continue;
		}
		Nodes[nid-1].row = r;
		Nodes[nid-1].column = c;
		
	}
	setRandomDirection();
	for (i = 0; i < 10; ++i)
	{
		printf("\n_____________________________________________\nITERATION %d\n\n",i+1 );
		printNodes();
		nextInteration();
	}


	return 0;
}