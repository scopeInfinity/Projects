#include<stdio.h>
#include<string.h>
#include<time.h>
#include<stdlib.h>

char line[100];
char* getValueFromKey(FILE *fin,char *str,int fieldToSkip)
{
	while(1)
	{
		fscanf(fin,"%s",line);
		if(feof(fin)) break;
		if(strcmp(line,str)==0)
		{
			int i;
			//Skipping
			for(i=0;i<fieldToSkip;i++)
			fscanf(fin,"%s",line);
			fscanf(fin,"%s",line);

			return line;
		}
	}
	return NULL;
}
void printCPU()
{
	FILE *fin = fopen("/proc/cpuinfo","r");
	char *str;
	printf("-------------------\nCPU Info\n-------------------\n\n");
	while((str=getValueFromKey(fin,"processor",1))!=NULL)
	{
		printf("Processor %s\n",str);
		str = getValueFromKey(fin,"cpu",0);//cpu family
		str = getValueFromKey(fin,"cpu",2);//cpu MHz
		printf("\tClock Speed\t: %sMhz\n", str);
		str = getValueFromKey(fin,"cores",1);//cores
		printf("\tCores\t\t : %sMhz\n\n", str);


	}

	fclose(fin);
}
void printKernal()
{
	FILE *fin = fopen("/proc/version","r");
	char str[1000];
	printf("-------------------\nKernal Version\n-------------------\n\n");
	fscanf(fin,"%[^(]",str);
	printf("%s ",str);
	printf("\n\n");
	fclose(fin);
}
void printAvgLoad()
{
	FILE *fin = fopen("/proc/loadavg","r");
	char *str;
	float f;
	fscanf(fin,"%f",&f);//1min
	fscanf(fin,"%f",&f);//5min
	fscanf(fin,"%f",&f);//15min

	printf("-------------------\nAverage Load (last 15 min) %f: \n-------------------\n\n",f);
	fclose(fin);
}
void printMemFree()
{
	FILE *fin = fopen("/proc/meminfo","r");
	char *str;
	float f;
	printf("-------------------\nMemory Info\n-------------------\n");
	str = getValueFromKey(fin,"MemTotal:",0);
	printf("Total Memory \t: %s kB\n",str);
	str = getValueFromKey(fin,"MemFree:",0);
	printf("Free Memory \t: %s kB\n",str);
	

	fclose(fin);
}
void printSwapMem()
{
	FILE *fin = fopen("/proc/meminfo","r");
	char *str;
	printf("-------------------\nSwap Info\n-------------------\n\n");

	str = getValueFromKey(fin,"SwapTotal:",0);//swap total
	printf("Total Swap : %s kb\n",str);
	str = getValueFromKey(fin,"SwapFree:",0);//swap total
	printf("Free Swap : %s kb\n",str);
	
	fclose(fin);

	fin = fopen("/proc/swaps","r");
	fscanf(fin,"%*s %*s %*s %*s %*s %*s %*s %*s %s",str);//Space Used
	printf("Swap Used : %s kb\n",str);
	
	printf("\n\n");
	fclose(fin);
}

void printSwapDetails()
{
	FILE *fin = fopen("/proc/swaps","r");
	char str[1024];
	printf("-------------------\nSwap Details\n-------------------\n\n");
	fscanf(fin,"%*[^\n]");//Ignore FIrst Line
	int c=0;
	while(1)
	{
		fscanf(fin,"%s",str);//Read
		if(feof(fin))break;
		printf("%s\t",str );
		c++;
		if(c%5==0)
			printf("\n");
		
	}
	fclose(fin);

}
void printBootTime()
{
	FILE *fin = fopen("/proc/stat","r");
	char *str;
	str = getValueFromKey(fin,"ctxt",0);//Before Boot Time
	long tim;
	fscanf(fin,"btime\t%ld",&tim);//Read
	time_t now = tim;
	printf("-------------------\nBoot Time : %s\n-------------------\n\n",ctime(&now));

	fclose(fin);

}
void printCpuTime()
{
	FILE *fin = fopen("/proc/stat","r");
	char *str;
	str = getValueFromKey(fin,"cpu",0);//For all core together
	int user,kernal;
	fscanf(fin,"%d %*d %d",&user,&kernal);
	printf("-------------------\nTime Spend By CPU\n-------------------\n User Mode : %d sec  \n Kernal Mode : %d sec \n\n",user,kernal);

	fclose(fin);

}

void printNoOfContextSwitch()
{
	FILE *fin = fopen("/proc/stat","r");
	char *str;
	str = getValueFromKey(fin,"ctxt",0);//Context Switchs
	printf("\n------------------------\nContext Switchs : %s\n-----------------------------\n\n",str);

	fclose(fin);

}
void printNoOfInterupts()
{
	FILE *fin = fopen("/proc/stat","r");
	char *str;
	str = getValueFromKey(fin,"intr",0);//No. Of Intrupts
	printf("\n------------------------\nNo. Of Intrupts : %s\n-----------------------------\n\n",str);

	fclose(fin);

}

int main(int argc, char const *argv[])
{
	printf("System Info\n---------------\n\n");
	printf("0  - All\n");
	printf("1  - CPU/Processor/Core Info\n");
	printf("2  - Linux Kernal Info\n");
	printf("3  - Average load in last 15 min\n");
	printf("4  - Usable and free Memory\n");
	printf("5  - Total/Used Swap Memory\n");
	printf("6  - Swap Partitions and Size\n");
	printf("7  - CPU Time Spend in User and Kernal mode\n");
	printf("8  - Number of Context Switches\n");
	printf("9  - Total number of interrupts encountered\n");
	printf("10 - Last Boot Time\n");
	
	printf("Enter Choice : ");
	int id;
	scanf(" %d",&id);


	int fallthrough = 0;

	switch(id)
	{
		case 0:fallthrough=1;

		case 1:printCPU();
				if(!fallthrough) break;
		case 2:printKernal();
				if(!fallthrough) break;
		case 3:printAvgLoad();
				if(!fallthrough) break;
		case 4:printMemFree();
				if(!fallthrough) break;
		case 5:printSwapMem();
				if(!fallthrough) break;
		case 6:printSwapDetails();
				if(!fallthrough) break;
		case 7:printCpuTime();
				if(!fallthrough) break;
		case 8:printNoOfContextSwitch();
				if(!fallthrough) break;
		case 9:printNoOfInterupts();
				if(!fallthrough) break;
		case 10:printBootTime();
				 break;
		default :printf("Invalid Choice!\n");

	}
	return 0;
}
