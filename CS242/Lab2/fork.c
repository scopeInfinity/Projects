#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>

#include <sys/types.h>
#include <sys/wait.h>

void childWork(int id);
char *child_name[4]={"Wise Child","Foolish Child","Wicked Child","Hardworking Child"};

int main(int argc, char const *argv[])
{
	int i;
	int ChildMap[4];
	int begin_hw_child;
	
	for(i=0;i<4;i++)
	{
		pid_t pid = fork();
		if(pid == 0)
		{
			int myid = i;
			childWork(myid);
		}
		else 
		{
			ChildMap[i] = pid;
			if(i==3)//hardworking
				begin_hw_child = time(NULL);
		}	
	}
	
	//Waiting for all child process to exit
	for (i = 0; i < 4; ++i)
	{
		int sig;
		pid_t pid = wait(&sig);
		int id=0,j;
		while(ChildMap[id]!=pid)
			id++;
		printf("\n> %s Signal : %s\n",child_name[id],strsignal( WTERMSIG(sig) ) );
		if(id==3)//hardworking
		{
			int end_hw_child = time (NULL);
			printf(" Time Taken for Hardworking : %d sec\n",(end_hw_child-begin_hw_child));

		}
	}

	return 0;
}
void child_wise()
{
	sleep(1);
	exit(16);
}
void child_foolish()
{
	int *x=NULL;
	*x=1;
}
void child_wicked()
{
	exit(SIGABRT);
}

void child_hardworking()
{
	execve("SystemInfo",NULL,NULL);
}


void childWork(int id)
{	
	//must exit
	switch(id)
	{
		case 0 : child_wise();
				break;
		case 1 : child_foolish();
				break;
		case 2 : child_wicked();
				break;
		case 3 : child_hardworking();
				break;

	}
}