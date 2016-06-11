#ifndef PQUEUE_H
#define PQUEUE_H

#ifndef PQUEUE_APPEND_SIZE
#define PQUEUE_APPEND_SIZE 100
#endif

	typedef struct _PQUEUE
	{
		int *data;
		int size;
		int max_size;
	} _PQUEUE;
	typedef _PQUEUE * PQUEUE;

	PQUEUE Init(PQUEUE Q);
	
	int IsEmpty(PQUEUE Q);

	int ExtractMax(PQUEUE Q, int *x);

	int Insert(PQUEUE Q, int x);

	void Destroy(PQUEUE Q);

#endif