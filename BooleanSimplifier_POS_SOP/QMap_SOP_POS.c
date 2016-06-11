/******************* Q-Map Solver ******************
		
		For 15 variables Max.
		Gagan Kumar
		1401CS15

****************************************************/
#include<stdio.h>
#include<stdlib.h>
#include<string.h>


int N;			//Number of Variables
int isSOP=0;
	
/***********************
Convention to Store Implicant in a Integer Variables
	00  - 0
	11  - 1
	01  - x
	10  - invalid
**********************/



int convertToMyConversion(int x)
{

	int a=0;
	int i=0;
	while(x)
	{
		a|=((x&1)<<(2*i))|((x&1)<<(2*i+1));
		i++;
		x>>=1;
	}
	return a;
}
int convertBackFromMyConversion(int x)
{

	int a=0;
	int i=0;
	while(x)
	{
		a|=((x&1))<<(i);
		i++;
		x>>=2;
	}
	return a;
}
int isOneBitDifference(int x,int y)
{
	int c=0;
	while(x || y)
	{
		if((x&3)!=(y&3))
			c++;
		x>>=2;
		y>>=2;
	}
	return c;
}
int combineBit(int x,int y)	
{
	if(x==y)
		return x;
	return 01;
}
int combineTwoNumber(int x,int y)
{
	int i=0;
	int a=0;
	while(x || y)
	{
		a|=(combineBit(x&3,y&3))<<(i*2);
		x>>=2;
		y>>=2;
		i++;
	}
	return a;
}

int isSubset(int outer,int inner)
{
	while(outer || inner)
	{
		int a=outer&3;
		int b=inner&3;
		if(a==0 && b==3)
			return 0;
		if(a==3 && b==0)
			return 0;
		if(a!=01 && b==01)
			return 0;	
		outer>>=2;
		inner>>=2;
	}
	return 1;
}
void addAlternateChar(char *s,char x)
{
	char s2[40];
	strcpy(s2,s);
	int i,l=strlen(s2);
	for(i=0;i<l;i++)
		{
			s[2*i]=s2[i];
			if(s2[i]==' ') s[2*i+1]=' ';
			else s[2*i+1]=x;
		}
	s[2*l-1]='\0';
}
void makeMinTerm(char *str,char *strUpper,int x,char sc_inside)
{	
	int i=0,j=0;
	int z=1<<(N*2);
	while(z!=1)
	{
		int a=x&3;
		int aa = (isSOP<<1)|isSOP;//For comparing
		if(aa==3) aa=0; else aa=3;
		x>>=2;
		strUpper[N-i-1]=' ';
		if(a==aa)
			strUpper[N-i-1]='_';
		if(a!=1)
			{
				str[N-i-1]='A'+N-1-j;
				i++;
			}
		if(a==2){	//Invalid Input
			str[0]=strUpper[0]='\0';
			return;
		}
		j++;
		z>>=2;
		
	}
	for(;i<N;i++)str[N-i-1]=strUpper[N-i-1]=' ';
	str[N]='\0';
	strUpper[N]='\0';
	addAlternateChar(str,sc_inside);
	addAlternateChar(strUpper,' ');


}
void removeEimplicantsFromFEssential(int *FinalImplicants,int fsize,int *Eimplicants,int esize)
{
	int i,j;
	for(i=0;i<fsize;i++)
	{
		for(j=0;j<esize;j++)
			if(FinalImplicants[i]==Eimplicants[j])
			if(Eimplicants[j]>=0)
				{
					Eimplicants[j]=-1-Eimplicants[j];
				}
	}
}

void removeSubSets(int *Eimplicants,int esize)
{
	int i,j;
	for(i=0;i<esize;i++)
	if(Eimplicants[i]>=0)
	{
		for(j=0;j<esize;j++)
		if(i!=j)
		if(Eimplicants[j]>=0)
		if(isSubset(Eimplicants[i],Eimplicants[j]))
		{
			Eimplicants[j]=-1-Eimplicants[j];
		}
	}
}
void print(int x,char inside)
{
		char minTerm[40],minTermU[40];
		makeMinTerm(minTerm,minTermU,x,inside);
		printf("  %s\n> %s %d\n",minTermU,minTerm,x);
}
int main()
{
	char ch;
	printf("Want to Try for POS(Y/N) :");
	scanf(" %c",&ch);
	if(ch=='Y' || ch=='y')
		isSOP=0;
	else if(ch=='N' || ch=='n')
		isSOP=1;
	else {
		printf("Invalid Choice\n");
		return;
	}
	/****** String Difference For POS and SOP ******/
	//String collection
	//First POS then SOP
	char SC_termStart[2][2]={"("," "};
	char SC_termEnd[2][2]={")"," "};
	char SC_inBwTerm[2][2]={".","+"};
	char SC_insideTerm[2]={'+','.'};
	char SC_NameOfTerm[2][8]={"MaxTerm","MinTerm"};


	/************ INPUT *********/
	printf("Enter Number of Variables : ");
	scanf("%d",&N);
	
	int NOCi = 1+(1<<N);
	int *implicants=(int *)malloc(sizeof(int)*(N+1)*NOCi);			//implicant[no_of_reductant_variable][0 - size, 1,2,3... Numbers]
	int i,j,k,num;

	printf("Enter Number of %s : " ,SC_NameOfTerm[isSOP]);
	scanf("%d",&num);
	int *minTerms = (int *)malloc(sizeof(int)*num);
	printf("Enter %d %s : ",num ,SC_NameOfTerm[isSOP]);
	*implicants=0;
	for(i=0;i<num;i++)
	{
		int x;
		scanf("%d",&x);
		minTerms[i]=convertToMyConversion(x);	//Addition of new element in new Row.
		implicants[1+(*implicants)++]=minTerms[i];
	}
	int dnum;
	printf("Enter Number of don't Care Terms : " );
	scanf("%d",&dnum);
	printf("Enter %d don't Care Terms : ",dnum );
	for(i=0;i<dnum;i++)
	{
		int x;
		scanf("%d",&x);
		implicants[1+(*implicants)++]=convertToMyConversion(x);	//Addition of new element in new Row.
	}
	

	/*********** Formation of Tables *******************/
	for(i=0;i<N;i++)
	{
		implicants[(i+1)*NOCi]=0;
		//comparing each pair of last column
		for(j=0;j<implicants[i*NOCi];j++)
			for(k=j+1;k<implicants[i*NOCi];k++)
			//if(implicants[(i)*NOCi+j+1]!=-1 && implicants[(i)*NOCi+k+1]!=-1)
			{

				/******************* MAPPING to Check If Implicant is Done************
				+ve = Not yet paired
				-ve = Already paired

				p and q = inverse Mapping
				
				**********************************************************************/

				int p=implicants[(i)*NOCi+j+1];
				int q=implicants[(i)*NOCi+k+1];
				if(p<0) p=-p-1;
				if(q<0) q=-q-1;
				if(isOneBitDifference(p,q)==1)
				{
					int x=combineTwoNumber(p,q);
					implicants[(i)*NOCi+j+1]=-p-1;
					implicants[(i)*NOCi+k+1]=-q-1;
					implicants[(i+1)*NOCi+1+implicants[(i+1)*NOCi]++]=x;
				}	
			}

	}
	



	int *Eimplicants=(int *)malloc((sizeof(int))*(1<<N));
	int esize=0;
	//Printing Implicant's
	for (i = 0; i <= N; ++i)
	{
		for(j=0;j<implicants[(i)*NOCi+0];j++)
			if(implicants[(i)*NOCi+j+1]>=0)
				Eimplicants[esize++] = implicants[(i)*NOCi+j+1];
	}
	removeSubSets(Eimplicants,esize);
	
	/******************************
	printf("Min Terms\n");
	for (i = 0; i < esize; ++i)
	if(Eimplicants[i]>=0)
		print(Eimplicants[i]);
	********************************/

	int *TableTwo = (int *)malloc(sizeof(int)*num*esize);
	int *ColumnNeedToDo = (int *)malloc(sizeof(int)*num);
	int *EssentialRows = (int *)malloc(sizeof(int)*esize);

	for(j=0;j<num;j++)
		ColumnNeedToDo[j]=1;

	for(i=0;i<esize;i++)
	if(Eimplicants[i]>=0)
	{
		
		for(j=0;j<num;j++)
			if(isSubset(Eimplicants[i],minTerms[j]))
				TableTwo[i*num+j] = 1;	//Star
			else
				TableTwo[i*num+j] = 0;	//No Star
			
		EssentialRows[i]=1;
	}
	else EssentialRows[i]=0;

	//For column with single star
	for(i=0;i<num;i++)
	if(ColumnNeedToDo[i]==1)
	{
		int c=0;
		int in;
		for(j=0;j<esize;j++)
		if(EssentialRows[j]==1)
			if(TableTwo[j*num+i])
				{
					c++;
					in = j;
				}
		if(c==1)
			{
				EssentialRows[in]=2;
				for(j=0;j<num;j++)//For each column
					if(ColumnNeedToDo[j]==1)
					{
						if(TableTwo[in*num+j])
							ColumnNeedToDo[j]=0;
						
					}
			}
	}

	/**  Left Out Elements **
	printf("\n\nLeft Out\n");
	for(i=0;i<esize;i++)
	if(EssentialRows[i]==1)
		print(Eimplicants[i]);
	for(i=0;i<num;i++)
	if(ColumnNeedToDo[i]==1)
		printf("%d >..\n", convertBackFromMyConversion( minTerms[i]));
	****************/


	//Finding for new subsets
	for(i=0;i<esize;i++)
	if(EssentialRows[i]==1)
	for(j=0;j<esize;j++)
	if(EssentialRows[j]==1)
	if(i!=j)
	{
		int isSubset = 1;
		//Check Subset
		for(k=0;k<num;k++)
		if(ColumnNeedToDo[k]==1)
		{
			if(TableTwo[i*num+k]==0 && TableTwo[j*num+k]==1)
				{
					isSubset = 0;
					break;
				} 
		}
		if(isSubset)
		{
			EssentialRows[j]=3;
		//	printf("Removed Subset\n");
		//	print(Eimplicants[j]);
		}
	}

	//May have multiple answers
	/******Searching for maxStar Rows ******/
	while(1)
	{
		//printf("column left\n");
		
		//for(j=0;j<num;j++)//For each column
		//		if(ColumnNeedToDo[j]==1)
		//			printf("%d,", convertBackFromMyConversion(minTerms[j]));

		int mStar=0,mStarI=-1;
		for(i=0;i<esize;i++)
		if(EssentialRows[i]==1)
		{
			int c=0;
			for(j=0;j<num;j++)
				if(ColumnNeedToDo[j]==1)
				if(TableTwo[i*num+j]==1)
					c++;
			if(mStar<c) {mStar = c;mStarI=i;}
			
		}
		if(mStarI==-1) break;
				EssentialRows[mStarI]=2;
				for(j=0;j<num;j++)//For each column
					if(ColumnNeedToDo[j]==1)
					{
						if(TableTwo[mStarI*num+j])
							ColumnNeedToDo[j]=0;
						
					}
		
		//printf("Prime Implicant Added,Stars : %d\n",mStar);
		//print(Eimplicants[mStarI]);
		
	}


	int *FinalImplicants = (int *)malloc((sizeof(int)*(1<<N)));
	int fsize=0;
	
	//Finally Addition of Remaning Term in Final Answer

	for(i=0;i<esize;i++)
		if(EssentialRows[i]==2)
			FinalImplicants[fsize++] = Eimplicants[i];

	/*******************************
	//Printing Minimum SOP
	printf("Min Terms\n");
	for (i = 0; i < fsize; ++i)
		if(FinalImplicants[i]>=0)
		print(FinalImplicants[i]);
	*********************************/


	//Printing Final Answer

	char minTerm[40],minTermU[40];
	
	//Printing Bars
	printf("\n       ");
	int numberOfTerms=0;
	for (i = 0; i < fsize; ++i)
		if(FinalImplicants[i]>=0)
		{
			makeMinTerm(minTerm,minTermU,FinalImplicants[i],SC_insideTerm[isSOP]);
			printf("%s   ", minTermU);
			numberOfTerms++;
		}


	//Printing Variables
	printf("\nSOP = ");
	int whichTerm=0;
	for (i = 0; i < fsize; ++i)
		if(FinalImplicants[i]>=0)
		{
			makeMinTerm(minTerm,minTermU,FinalImplicants[i],SC_insideTerm[isSOP]);
			printf("%s%s%s", SC_termStart[isSOP], minTerm,SC_termEnd[isSOP]);
			whichTerm++;
			if(whichTerm!=numberOfTerms)
				printf("%s",SC_inBwTerm[isSOP]);
		}
	printf("\n");
	
	free(implicants);
	free(Eimplicants);
	free(FinalImplicants);
	free(minTerms);
	return 0;
}