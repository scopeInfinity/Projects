#ifndef MOTION_H
#define MOTION_H

#define ROW 10
#define COLUMN 10
#define NODES 4

struct NODE
{
	int row, column, direction;
};

struct NODE  Nodes[NODES];

void nextInteration();
void printNodes();
void setRandomDirection();

#endif