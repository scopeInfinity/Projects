#include <stdio.h>
int N, A[100];

void merge(int *F,int *M,int *L)
{
	int B[100];
	int *i=F,*j=M+1,*k=&B[0];
	while(i<=M && j<=L)
	{
		if(*i<*j)
			*(k++) = *(i++);
		else *(k++) = *(j++);
	}
	while(j<=L)
		*(k++) = *(j++);
	while(i<=M)
		*(k++) = *(i++);
	i=F;
	j=B;
	while(i<=L)
		*(i++) = *(j++);
	
}
void msort(int *F,int *L)
{
	if(L>F)
	{
		int n = L-F+1;
		int *M = F+n/2-1;
		msort(F,M);
		msort(M+1,L);
		merge(F,M,L);
	}
}
int main()
{
	int i;
	printf("Enter Number of Elements : ");
	scanf("%d",&N);
	printf("Enter numbers : ");
	for (i = 0; i < N; ++i)
		scanf("%d",&A[i]);
	msort(&A[0],&A[0]+N-1);
	printf("\nSorted Array : ");
	for (i = 0; i < N; ++i)
		printf("%d ",A[i]);


	return 0;
}