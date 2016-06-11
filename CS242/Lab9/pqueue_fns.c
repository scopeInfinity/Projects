#include "pqueue.h"
#include <stdlib.h>

	PQUEUE Init(PQUEUE Q) {
		if(Q == NULL)
			Q = malloc(sizeof(_PQUEUE));
		if( Q->data == NULL) 
			Q->data = malloc(sizeof(int)*PQUEUE_APPEND_SIZE);
		Q->size = 0;
		Q->max_size = PQUEUE_APPEND_SIZE;
		return Q;
	}
	
	int IsEmpty(PQUEUE Q) {
		return (Q==NULL || Q->size == 0);
	}

	int ExtractMax(PQUEUE Q, int *x) {
		if(Q == NULL)
			return -1;
		
		if( Q->data == NULL)
				return -2; 

		if(Q->size == 0)
			return -1;
		else {
			int *data = Q->data;
			
			*x = *data;
			*data = *(data + Q->size - 1);
			Q->size--;

			//Move Down

			int current = 0, maxChild;
			while (current < Q->size/2) {
				
				maxChild = current;
				if (Q->data[current*2 + 1] > Q->data[maxChild])
					maxChild = current*2 + 1;
				if (Q->data[current*2 + 2] > Q->data[maxChild])
					maxChild = current*2 + 2;
				
				if(maxChild == current)
					break;

				int swapTemp = Q->data[maxChild];
				Q->data[maxChild] = Q->data[current];
				Q->data[current] = swapTemp;
				
				current = maxChild; 
			}

			return 0;
		}
	}

	int Insert(PQUEUE Q, int x) {
		if(Q == NULL)
			return -1;
		if(Q->data == NULL)
			return -1;

		if(Q->size == Q->max_size) {
			Q->max_size += PQUEUE_APPEND_SIZE;
			Q->data = realloc(Q->data, sizeof(int)*Q->max_size);
		}
	


		*(Q->data + Q->size) = x;
		Q->size++;
		

		//Move Up
		int current = Q->size - 1, parent;
		while (current>0) {
			parent = (current - 1)/2;
			if (Q->data[parent] > Q->data[current])
				break;

			int swapTemp = Q->data[parent];
			Q->data[parent] = Q->data[current];
			Q->data[current] = swapTemp;
			current = parent; 
		}
		return 0;

	}

	void Destroy(PQUEUE Q) {
		if(Q == NULL)
			return;
		if (Q->data != NULL)
			free(Q->data);
		Q->size = 0;
			free(Q);
	}
