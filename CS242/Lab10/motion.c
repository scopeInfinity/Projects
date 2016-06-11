#include "motion.h"
#include <stdlib.h>

void nextInterationNode(int n) {
	int deltax = 0, deltay=0;
	//printf("%d Node Direction %d\n",n, Nodes[n-1].direction);
	if(Nodes[n-1].direction<3)
		deltax=-1;
	if(3<Nodes[n-1].direction && Nodes[n-1].direction<7)
		deltax=+1;
	if(Nodes[n-1].direction==0)
		deltay=-1;
	if(1<Nodes[n-1].direction && Nodes[n-1].direction<5)
		deltay=+1;
	if(Nodes[n-1].direction>5)
		deltay=-1;

	int newDeltaX = deltax;
	int newDeltaY = deltay;


	//Check Other
	int other;
	for (other = 0; other < NODES; ++other)
	if(other!=n-1)
	{
		if(Nodes[other].row == Nodes[n-1].row+deltay && Nodes[other].column == Nodes[n-1].column+deltax)	
		{
			//newDeltaX = -deltax;
			//newDeltaY = -deltay;
			return;
			//Don't Move
		}
	}


	Nodes[n-1].row+=deltay;
	Nodes[n-1].column+=deltax;
	
	if(Nodes[n-1].row<1 || Nodes[n-1].row>ROW)
	{
		newDeltaY = -deltay;
	}
	if( Nodes[n-1].column<1 || Nodes[n-1].column>COLUMN)
	{
		newDeltaX = -deltax;
	}
	int newDirection = 0;
	if(newDeltaX==0)
		if(newDeltaY<0)
			newDirection = 7;
		else
			newDirection = 3;
	else if(newDeltaX>0)
		{
			if(newDeltaY<0)
				newDirection = 6;
			else if(deltay==0)
				newDirection = 5;
			else newDirection = 4;
		}
	else {
			if(newDeltaY<0)
				newDirection = 0;
			else if(newDeltaY==0)
				newDirection = 1;
			else newDirection = 2;
			
		}
	//	printf("%d %d => %d\n", newDeltaX, newDeltaY , newDirection);
	
	if(newDeltaY!=deltay || newDeltaX!=deltax)
		Nodes[n-1].direction=newDirection;
	if(newDeltaY>0 && deltay<0) {
		Nodes[n-1].row+=2;
	}
	if(newDeltaY<0 && deltay>0) {
		Nodes[n-1].row-=2;
	}
	if(newDeltaX>0 &&  deltax<0) {
		Nodes[n-1].column+=2;
	}
	if(newDeltaX<0 && deltax>0) {
		Nodes[n-1].column-=2;
	}


}

void nextInteration() {
	printAll();
	int i;
	for (i = 1; i <= NODES; ++i)
	{
		nextInterationNode(i);
	}
}

void setRandomDirection() {
	int i;
	for (i = 1; i <= NODES; ++i)
	{
		int d = rand()%8;
		Nodes[i-1].direction = d;
	}
}