/****************************
A Simple Card Thrower Game

Email	: gagan.cs14@iitp.ac.in
*****************************/
#include<stdio.h>
#include<stdlib.h>
#include<time.h>

typedef struct 
{
	int num;
	int type;
} CARD;		//structure to store CARDS

//Swap two cards
void swap(CARD *a,CARD *b)
{
	CARD temp=*a;
	*a=*b;
	*b=temp;
}

const int scoreWin=1;	//Points on W

int point(CARD s1,CARD s2)		//Rule Book
{
	if(s1.num==1) s1.num=14;
	if(s2.num==1) s2.num=14;	//Make Ace of maximum point
	if(s1.num<s2.num) return -1;
	else if(s1.num>s2.num) return +1;
	
	
	return 0;
}

void print(CARD a)		//Print a Single Card
{
printf("  ");
	switch(a.type)
	{
	case 0: printf("  \u2660 ");break;
	case 1: printf("  \u2661 ");break;
	case 2: printf("  \u2662 ");break;
	case 3: printf("  \u2663 ");break;
	}
	if(a.num>1 && a.num<11)
		printf("%3d",a.num);
	else
	switch(a.num)
	{
	case  1:printf("  A");break;
	case 11:printf("  J");break;
	case 12:printf("  Q");break;
	case 13:printf("  K");break;

	}
	
}
void printHeader()
{
	printf("  Simple Card Thrower Game |\n----------------------------\n\t\tBy gagan1kumar (gagan.cs14)\n\n");
}
int main()
{
	int lScore=0,rScore=0;
	srand(time(NULL));
	CARD list[52];	//We will use card from last index
	int i,j;

	//Initilize all cards
	for(i=0;i<13;i++)
	{
	for(j=0;j<4;j++)
	{
	list[j*13+i].num=i+1;
	list[j*13+i].type=j;
	}
	}


	//Instructions
	system("clear");
	printHeader();
	printf("\nChoose yourself as a Player 1 or as a Player 2\n\n");
	printf("Now, Press Return key to Shuffle Cards and start the game ...\n\n");
	getchar();
	

	//Shuffle
	for(i=0;i<52;i++)
		swap(&list[i],&list[rand()%52]);
	int cardsleft=26;
	int flag;
	

	while(cardsleft)		//Game Play
	{
	system("clear");
	printHeader();
	printf("Current Cards\n\n");

	switch(point(list[cardsleft-1],list[cardsleft+26-1]))	//Check Who Wins this chance
	{
	case  1 : lScore+=scoreWin;flag=1;break;
	case -1 : rScore+=scoreWin;flag=-1;break;
	default : flag=0;
	}
	

	//Print Current status
	printf("Player 1 : ");
	for(i=0;i<cardsleft-1;i++)
	printf("#");
	print(list[cardsleft-1]);
	if(flag==1) printf("   +");
	printf("\n");
	
	printf("Player 2 : ");
	for(i=0;i<cardsleft-1;i++)
	printf("#");
	print(list[26+cardsleft-1]);
	if(flag==-1) printf("   +");
	printf("\n\n");


	cardsleft--;
	if(cardsleft!=0)
	printf("\n\nCards left : %d\nPress Return key to throw next card\n",cardsleft);
	else
	printf("\n\n\aPlayer 1 Score : %d \nPlayer 2 Score : %d \n\n",lScore,rScore);
	getchar();
	}	


	//Final Score
	if(lScore<rScore)
		printf("Player 2 Wins By %d Score.\n",rScore-lScore);
	else
	if(lScore>rScore)
		printf("Player 1 Wins By %d Score.\n",lScore-rScore);
	else
		printf("Game Draw\n");
	
	
	return 0;
}
