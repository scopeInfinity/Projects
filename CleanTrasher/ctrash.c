#include<stdio.h>
#include<string.h>
#include<stdlib.h>
int main(int argc,char *argv[])
{
	if(argc==2)
		if(strcmp(argv[1],"-i")==0)
		{
			char cmd[1000]="sudo cp ";
			strcat(cmd,argv[0]);
			strcat(cmd," ");
			strcat(cmd,"/usr/bin/");
			printf("%s\nDo you want to install?(Y/N)",cmd);
			char c;
			c=getchar();
			if(c=='Y' || c=='y')
				system(cmd);
			return 2;

		}
	if(!(argc==2 || (argc==3 && strcmp(argv[2],"-nc")==0)))
	{
		printf("\nClean Trasher(For linux)\n\t\tBy GHpain\n\n\t%s <filename>\n\t%s <filename> -nc\t\t(No Confirmation)\n\t%s -i\t\t\t(Install)\n\nWarning : Do not use Wildcards\n\n", argv[0], argv[0], argv[0]);
		return -2;
	}
	long i,j,size,sp;
	FILE *fn=fopen(argv[1],"rb");
	if(fn==NULL)
	{
	if(argc==2)
		printf("File Not Opened!!!\n");
		return -1;
	}
	fseek(fn,0,2);
	size=ftell(fn);
	sp=size/100+1;
	fclose(fn);
	if(argc==2)
	{
	char c;
	printf("File Size %ld Bytes\n", size);
	printf("Do you really want to delete following file\n%s\n(Y/N)?", argv[1]);
	c=getchar();
	if(c!='Y' && c!='y') return 1;
	}
	fn=fopen(argv[1],"rb+");
	fseek(fn,0,0);
	if(fn==NULL)
	{
	if(argc==2)
		printf("File Not Trashed!!!\n");
		return -3;
	}
	char p[]="echo -n 00% ";
	int d;
	if(argc!=2)
	for(i=0;i<size;i++)
		fputc(0,fn);
	else
	for(i=0,j=0;i<size;i++,j++)
	{
		fputc(0,fn);
		if(j==sp)
			{
				d=i*100/size;
				p[8]='0'+d/10;
				p[9]='0'+d%10;
				system(p);
				j=0;
			}
		
	}
	fclose(fn);
	char cmd[1000]="rm \"";
	strcat(cmd,argv[1]);
	strcat(cmd,"\"");
	if(argc==2)
		printf("\nData Destructed - now normally removing file\n");
	system(cmd);
	
	return 0;
}