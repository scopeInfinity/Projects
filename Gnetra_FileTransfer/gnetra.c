#include<stdio.h>
#include<string.h>
#include<stdlib.h>

void quit(int id)
{
	if(id==2)
		printf("Port Number Invalid\n");
	else printf("Some Error Occured!");
	exit(id);
}
char *filename(char *s)
{
	int i;
	char *fn=(char *)malloc(1000*sizeof(char));
	int l=strlen(s),p=l-1;
	if(p<0 ) quit(-1);
	for(i=p-1;i>=0;i--)
		if(s[i]=='/')
			{p=i+1;break;}
	if(i<0) p=0;
	if(p>l-1) quit(-1);
	int j=0;
	for(i=p;i<l;i++,j++)
		fn[j]=s[i];
	fn[j]='\0';
	return fn;
}
int main(int argc,char *argv[])
{
	int showman=1;
	int n,i;
	// file or folder name
	if(argc==3)
	{
		if(strcmp(argv[1],"--r")==0)
		{
			showman=0;
			printf("Waiting To Receive ...\n");			
			for(i=0;argv[2][i]!='\0';i++)
			{
				if(argv[2][i]>='0' && argv[2][i]<='9')
					n=n*10+argv[2][i]-'0';
				else quit(2);
			}
			system("ifconfig");
			char str[1000]="";
			strcat(str,"vartemp=\"`nc -l ");
			strcat(str,argv[2]);
			strcat(str,"`\";nc -l ");
			strcat(str,argv[2]);
			strcat(str," | gzip -d -c > \"$vartemp\";");
			printf("\n%s\nDo you want to execute?(Y/N)",str );
			char ch;
			scanf(" %c",&ch);
			if(ch=='Y' || ch=='y')
				system(str);
		}
	}
	else if(argc==5)
	{
		if(strcmp(argv[1],"--t")==0)
		{
			showman=0;
			printf("File to send : %s to %s: %s",argv[4],argv[2],filename(argv[4]));
			for(i=0;argv[3][i]!='\0';i++)
			{
				if(argv[3][i]>='0' && argv[3][i]<='9')
					n=n*10+argv[3][i]-'0';
				else quit(2);
			}
			char str[1000]=" ";
			strcat(str,"echo \"");
			strcat(str,filename(argv[4]));
			strcat(str,"\" | nc ");
			strcat(str,argv[2]);
			strcat(str," ");
			strcat(str,argv[3]);
			strcat(str,";");
	

			strcat(str,"gzip -c < \"");
			strcat(str,argv[4]);
			strcat(str,"\" | nc ");
			strcat(str,argv[2]);
			strcat(str," ");
			strcat(str,argv[3]);
			strcat(str,";");
			
			printf("\n%s\nDo you want to execute?(Y/N)",str );
			char ch;
			scanf(" %c",&ch);
			if(ch=='Y' || ch=='y')
				system(str);
		}
	}
	if(showman)
	{
		system("clear");
		printf("\t\tGNETRA\t(For linux)\n\t\t-------------------------\n\t\t\tBy gagan1kumar\n\n");
		printf("Unsecure Simple File Transfer\n\n");
		printf("Syntax :\n\n");
		printf("\tSend File \t : %s --t <IP> <PORTNO> <FILENAME>\n",argv[0]);
		printf("\tReceive File \t : %s --r <PORTNO>\n",argv[0]);
		printf("\n\nWarning : Current Unsecure Version Can be Exploited EASILY Use Only With TRUSTED People Now\n\n");

	}
	return 0;
}
