#include<stdio.h>
#include<fstream>
/******Marking Scheme******/
const int posScore=4;
const int negScore=0;
const int maxq=40;	//Maxmimum no. of questions
const int ans[maxq]={1,4,2,3,2};//put correct answers here


using namespace std;
int main(int v,char *file[])
{	int i;
	if(v!=2) {printf("Error!\n");return -1;}
	char ch;
	int ques,res;
	ifstream fin(file[1]);
	int myans[maxq]={0};
	int score=0;

	while(fin)
	{
		fin>>ch;
		fin>>ques;
		fin>>ch;
		fin>>res;
		fin>>ch;
		fin>>ch;
		fin>>ch;
		fin>>ch;
		if(ques<1 || ques>40) {printf("Error!\n");return -1;}
	myans[ques-1]=res;
	}
	fin.close();	

	/*********Print the input*****
	for(i=0;i<maxq;i++)
		printf("%d => %d\n",i+1,myans[i]);
	*/
	for(i=0;i<maxq;i++)
		if(myans[i]!=0)
			if(myans[i]==ans[i])
				score+=posScore;
			else
				score+=negScore;

	printf("%40s =>  %d\n",file[1],score);
return 0;
}
