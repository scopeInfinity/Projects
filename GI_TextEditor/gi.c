#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <sys/ioctl.h>
#define LINE_SIZE 1000

const char B_R=' ';
const char B_L=' ';
const char B_B=' ';
const char B_U=' ';


const char B_UL=' ';
const char B_UR=' ';
const char B_DL=' ';
const char B_DR=' ';

char *file_name=NULL;
enum {vk_l=68,vk_r=67,vk_u=65,vk_d=66};
	
int width=100;
int height=50;
int x=0,y=2;//x realtive uy absolute
char buffer[LINE_SIZE]="";
int l=0;
	
void 	man();
void 	new();
void 	alert(char *str);
int 	popup(char *str,int);
int 	file_exists(char *str);
void 	cmd_mode();
int 	do_cmd(char cmd);
void 	gotoxy(int,int);
void 	insert_mode();
void 	quit();
void 	clear();
void 	refresh_dim();
int 	min(int,int);
void 	update_xy();
int 	arrowControl(char );
int 	save();
int 	read_cmd_line(char *);
void 	open();
void 	destroyLL();

typedef struct data_struct 
{
	char line[LINE_SIZE];
	struct data_struct *next,*prev;

} DATA;
DATA *head=NULL,*ln=NULL;
/*********VARIABLES FOR DATA HANDLING**********/

DATA *D_add(DATA *ln)//ADD after ln 	
{
	DATA *tmp=malloc(sizeof(DATA));
	//strcpy(tmp->line,str);
	tmp->next=NULL;
	tmp->prev=NULL;
	if(ln!=NULL)
	{
		tmp->next=ln->next;
		tmp->prev=ln;
		if(tmp->next!=NULL)
		tmp->next->prev=tmp;
		if(tmp->prev!=NULL)
		tmp->prev->next=tmp;

		

	}
	return tmp;
}
DATA *D_remove(DATA *ln)//Remove ln return previous 	
{
	DATA *tmp=head;
	if(ln!=NULL)
	{
		if(ln->prev!=NULL)
			{
				ln->prev->next=ln->next;
				tmp=ln->prev;
			}
		else
			{
				head=ln->next;

				tmp=head;
			}
		if(ln->next!=NULL)
			ln->next->prev=ln->prev;
		free(ln);
	}

	return tmp;

}





int D_print(DATA *ln)
{
	int h=0,y=2;
	DATA *ls=ln;
	while(ls!=NULL)
	{
		if(h>=height-3)
			break;
		h+=1+strlen(ls->line)/(width-1);
		ls=ls->prev;
	}
	if(ls==NULL) ls=head;
	int i,j,j2;
	for(i=1;i<height-1;i++)
	{
		gotoxy(1,i+1);
		printf("~");
		if(ls!=NULL)
		{
			if(ls==ln) y=i+1;
			for(j=0,j2=0;ls->line[j]!='\0';j++,j2++)
				{
					if(j2==width)
					{
						i++;
						j2=0;gotoxy(2,i+1);
					}
					if(ls->line[j]=='\t')
					printf("%c",175 );					//Escpecail Tap key
				else printf("%c",ls->line[j]);
			}
			for(;j2<width-2;j2++)
				printf(" ");
			ls=ls->next;

		}
		else
			for(j=0;j<width-2;j++)
				printf(" ");

	}
	for(;i<height-1;i++)
		{
		gotoxy(2,i+1);
		for(j=0;j<width-2;j++)
				printf(" ");
	
		}
	return y;
}
/**********************************************/

int main(int argc,char *argv[])
{
	int show_man_pak=1;
	system ("/bin/stty raw");		//KeyStroked Without Enter
	system ("/bin/stty -echo");		//KeyStroked No Printed
	
	if(argc==2)
	if(strcmp(argv[1],"--help")==0)
	{
			man(argv[0],0);
			quit();
	}
	else if(strcmp(argv[1],"--install")==0)
	{
		system ("/bin/stty cooked");		//KeyStroked Without Enter
		system ("/bin/stty echo");		//KeyStroked No Printed
	
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
	else 
	{
		file_name=malloc(sizeof(char)*100);
		strcpy(file_name,argv[1]);
		if(file_exists(file_name))
					{	
						open();
						cmd_mode();
						quit();
					}
					else show_man_pak=0;
				
	}
	head=D_add(NULL);
	if(show_man_pak)
	man(argv[0],1);
	cmd_mode();
	quit();
	return 0;
}

void man(char *f,int type)	//type==0 wait for key
{
		clear();
		gotoxy(width/2-12,2);
		printf("GI Text Editor(For Linux)");
		gotoxy(width/2-13,3);
		printf("---------------------------");
		gotoxy(3,6);
		gotoxy(width/2,4);
		printf("- gagan1kumar (gagan.cs14)");
		gotoxy(3,6);
		printf("Syntax");
		gotoxy(4,7);
		printf("1> %s --help \tHelp",f);
		gotoxy(4,8);
		printf("2> %s\t\tFor normal open",f);
		gotoxy(4,9);
		printf("3> %s <file> \tFor open specific file",f)	;
		gotoxy(4,10);
		printf("4> %s --install \tInstall",f)	;
		
		gotoxy(3,12);
		printf("Command");
		gotoxy(4,13);
		printf(" i - Insert Mode (Esc Key Twice to Come back to Command Mode)");
		gotoxy(4,14);
		printf(".s - Save");
		gotoxy(4,15);
		printf(".o - Open");
		gotoxy(4,16);
		printf(".a - Save As");
		gotoxy(4,17);
		printf(".n - New");
		gotoxy(4,18);
		printf(".e - Exit");
		gotoxy(4,19);
		printf(".d - Delete Line");

		if(type)
			{
			gotoxy(width/2-15,21);
			printf("Press any key to continue...");

			}
		getchar();		

}
void clear()
{
	//system("clear");
	refresh_dim();
	int i,j;
	char c;
	for(i=0;i<height;i++)
	{
		gotoxy(1,i+1);
	
		for (j = 0; j < width; j++)
		{
			

			if(i==0 || i==height-1 || j==0 || j== width-1)
			{
			
			if(i==0)
				c=B_U;
			if(j==0)
				c=B_L;
			if(i==height-1)
				c=B_B;
			if(j==width-1)
				c=B_R;

			if(i==0 && j==0)
				c=B_UL;
			if(i==0 && j==width-1)
				c=B_UR;
			if(i==height-1 && j==0)
				c=B_DL;
			if(i==height-1 && j==width-1)
				c=B_DR;
				putchar(c);
			}
			else printf(" ");
		}
	}
			
	gotoxy(1,1);
	
}
void update_xy()
{
	if(y>1)
	{
	gotoxy(width-9,height);
	printf("%03dC",x+1 );
	gotoxy(x+2,y);
	}
}
void cmd_mode()
{
	char cmd;
	int ret;
	while(1)
	{
	clear();
	gotoxy(2,1);
	printf(" ---- COMMAND MODE ---- ");
	y=D_print(ln);
	gotoxy(2+x,y);
	cmd=getchar();
	ret=do_cmd(cmd);
	
	/*if(x==0)
		printf("Invalid Command");
	else if(x==-1) printf("Invalid Syntax");
	else if(x==1) ;
	else printf("Unexpected Error!\n");
*/
	}
}
int read_cmd_line(char *strmsg)
{
	system ("/bin/stty cooked");
	system ("/bin/stty echo");
	if(file_name==NULL)
	file_name=malloc(sizeof(char)*100);
	char *str=malloc(sizeof(char)*100);
	
	gotoxy(2,height);
	int n;
	printf("%s%n",strmsg,&n );
			int i;
			
			for(i=0;i<width*8/10-n;i++)
				printf(" ");
			gotoxy(2+n,height);
			if(scanf(" %100s",str)!=1)
			{
				if(str!=NULL)
					free(str);
				str=NULL;
			}
			
			for(i=0;i<width*7/10;i++)
				putchar(B_B);
			update_xy();
	system ("/bin/stty raw");
	system ("/bin/stty -echo");
	
	if(str!=NULL)
		strcpy(file_name,str);
	else
		return 0;
	free(str);
	return 1;
}
int do_cmd(char cmd)
{
	
	if(cmd=='i' || cmd=='I')
		insert_mode();
	else if(cmd=='.')
	{
		gotoxy(2,height);
		printf(".");
		cmd=getchar();
		printf("%c",cmd);
	switch(cmd)
	{
		case 'd':
		case 'D':ln=D_remove(ln); 
				if(ln==NULL) {head=ln=malloc(sizeof(DATA));ln->line[0]='\0';}
				y=D_print(ln);

				strcpy(buffer,ln->line);
				l=strlen(buffer);
				x=0;
					break;
		

		case 'e':
		case 'E': quit();
					break;
		case 'n':
		case 'N': new();
					break;
		case 'i': 
		case 'I':insert_mode();
				break;
		
		case 'S':
		case 's':
	
			if(file_name==NULL)
			{
				
				if(read_cmd_line("Filename: "))
				if(!file_exists(file_name))
					if(save()) alert("Saved!"); else alert("Not Saved!");
				else if(popup("File Already Exists. Overwrite",0))
						if(save())  alert("Saved!"); else alert("Not Saved!");
			}
			else
				save();
				break;
		case 'A':
		case 'a'://save as
			if(read_cmd_line("Filename: "))
				if(!file_exists(file_name))
					if(save())  alert("Saved!"); else alert("Not Saved!");
				else if(popup("File Already Exists. Overwrite",0))
						if(save())  alert("Saved!"); else alert("Not Saved!");
				break;

		case 'O':
		case 'o':
			if(read_cmd_line("Filename: "))
			{
				if(file_exists(file_name))
					open();
				else
					{
						alert("File Doesn't Exists");
						free(file_name);
						file_name=NULL;
					}
			}
				break;
	
	}
} else if(cmd==27)
arrowControl(27); 	
	return 0;
}
void gotoxy(int x,int y)
{
	printf("\033[%d;%df",y,x);
}

int arrowControl(char ch)
{
	int key=-1;
		if(ch==27)	//Escape sequence
		{
			ch=getchar();
			if(ch==91)	//Observation arrow keys
			{
				ch=getchar();
				if(ln!=NULL)
				{
				if(ch==vk_u || ch==vk_d)
				{
				buffer[l]='\0';
				strcpy(ln->line,buffer);
				if(vk_d==ch)
					if(ln->next!=NULL)
					{
					ln=ln->next;
					l=strlen(ln->line);
					y=D_print(ln);
					strcpy(buffer,ln->line);
					update_xy();
					gotoxy(2+min(x,l),y);
					return 1;
					}

				if(vk_u==ch)
					if(ln->prev!=NULL)
					{
					ln=ln->prev;
					l=strlen(ln->line);
					y=D_print(ln);
					strcpy(buffer,ln->line);
					update_xy();
					gotoxy(2+min(x,l),y);
					return 1;
					}
				}
				else if(ch==vk_r)	{if(x<l) {x++;update_xy();return 1;}}
				else if(ch==vk_l)	{if(x>0) {x--;update_xy();return 1;}}
			}
		}
			else 
				{//
					//ungetc(ch,stdin);
					if(ch==27)
					return -1;
				}
		}
		return 0;
}
void insert_mode()
{
	clear();
	gotoxy(2,1);
	printf(" ---- INSERT MODE ----");
	char ch;
	int tmp_no;
	y=D_print(ln);
				
	update_xy();
	
	if(ln==NULL)  ln=head;
	do
	{
		ch=getchar();
		tmp_no=arrowControl(ch);	//Work Done of arrow Control
		if(tmp_no==1)	ch=0;		//Making non-esc
		else if(tmp_no==-1) break;	//Esc
		else if(isgraph(ch) || isspace(ch))
		{
			//Enter included
			if(ch==13)				//For Enter Reading 13
			{
				int j;
				for(j=0;j<x;j++)
					ln->line[j]=buffer[j];
				ln->line[j]='\0';
				DATA *l2=D_add(ln);
				for(;j<l;j++)
					l2->line[j-x]=buffer[j];
				l2->line[l-x]='\0';
				l=l-x;
				x=0;
				ln=l2;
				strcpy(buffer,ln->line);
				y=D_print(ln);

				update_xy();
				

			}
			else
			{
			if(x>l) x=l;

			if(x==l)
			{
				if(ch=='\t')		//For TAB
					printf("%c", 175);
				else printf("%c",ch);
				buffer[x++]=ch;
				l++;
				update_xy();

				
			
			}
			else
			{
				int j;
				for(j=l;j>x;j--)
					buffer[j]=buffer[j-1];
				buffer[x]=ch;
				for(j=x;j<=l;j++)
				{
					if(buffer[j]=='\t')	//For tab
						printf("%c", 175);
					else
					printf("%c",buffer[j]);
				}
				x++;
				l++;
				update_xy();

			
			}
			}
		}
		else if(ch==127)	//Backspace
		{
			if(x!=0)
			{
				if(x==l)
				{
				l--;
				x--;
				printf("\b ");
				update_xy();
				}
				else
				{
					int j;
					gotoxy(2+x-1,y);
					for(j=x;j<l;j++)
					{
						printf("%c",buffer[j] );
						buffer[j-1]=buffer[j];
					}
					printf(" ");
					l--;
					x--;
					update_xy();
				}
			}
			else
			{
				if(ln!=NULL)
				if(ln->prev!=NULL)
				{
					buffer[l]='\0';
					x=strlen(ln->prev->line);
					strcat(ln->prev->line,buffer);
					strcpy(buffer,ln->prev->line);
					ln=D_remove(ln);
					y=D_print(ln);
					
					l=strlen(buffer);

					update_xy();
				
				}
			}

		}

	}while(ch!=27); //Esc
	buffer[l]='\0';
	strcpy(ln->line,buffer);
					
}
void quit()
{
	//clear();
	system ("/bin/stty cooked");
	system ("/bin/stty echo");
	system("clear");
	exit(0);

}
int min(int a,int b)
{
	if(a<b) return a;
	return b;
}
void refresh_dim()
{
	struct winsize w;
    ioctl(0, TIOCGWINSZ, &w);

    height=w.ws_row;
    width=w.ws_col;
  

}
void new()
{
	destroyLL();
	clear();
	buffer[0]='\0';
	x=0;
	
	l=0;
	DATA *tmp;
	tmp=malloc(sizeof(DATA));
	tmp->next=NULL;
	tmp->prev=NULL;
	tmp->line[0]='\0';
	head=tmp;
	ln=head;
	y=D_print(head);
	file_name[0]='\0';
}
int save()
{
	FILE *f=fopen(file_name,"w");
	if(f==NULL) return 0;
	DATA *tmp=head;
	while(tmp!=NULL)
	{
		fprintf(f, "%s",tmp->line);
		if(tmp->next!=NULL)fprintf(f, "\n" );	
		tmp=tmp->next;
	}
	fclose(f);
	return 1;
}

void destroyLL()
{
	DATA *tmp;
	while(head!=NULL)
	{
		tmp=head;
		head=head->next;
		free(tmp);
	}

}
void open()
{
	int err=0;
	FILE *f=fopen(file_name,"r");
	destroyLL();
	char str[LINE_SIZE];
	DATA *tmp,*prev;
	tmp=malloc(sizeof(DATA));
	tmp->next=NULL;
	tmp->prev=NULL;
	if((fgets(tmp->line,LINE_SIZE,f))==NULL) 
		tmp->line[0]='\0';
	else
		tmp->line[strlen(tmp->line)-1]='\0';//Removing new line from last
	head=tmp;
	prev=head;
	while((fgets(str,LINE_SIZE-1,f))!=NULL)
	{
		tmp->line[strlen(tmp->line)-1]='\0';//Removing new line from last
		tmp=malloc(sizeof(DATA));
		tmp->next=NULL;
		tmp->prev=prev;
		strcpy(tmp->line,str);
		prev->next=tmp;
		
		prev=tmp;

	}
	x=0;
	ln=head;
	y=D_print(head);
	strcpy(buffer,head->line);
	l=strlen(buffer);
	update_xy();
	fclose(f);
}
int file_exists(char *str)
{
	FILE *f=fopen(str,"r");
	if(f!=NULL)
	{
		fclose(f);
		return 1;
	}
	return 0;
}
int popup(char *str,int def)//default
{

	

	gotoxy(2,height);
	int i;
	
	for(i=0;i<width*7/10;i++)
		printf(" ");
	gotoxy(2,height);
	printf("%s (Y/N) : ", str);
	
	char ch;
	getchar();
	ch=getchar();
	if(ch=='Y' || ch=='y')
		def=1;
	else if(ch=='n' || ch=='N')
		def=0;
	for(i=0;i<width*7/10;i++)
		putchar(B_B);
	update_xy();
	
	return def;
}
void alert(char *str)//default
{

	
	gotoxy(2,height-1);
	int n=strlen(str)+1;
	char str2[100];
	strcpy(str2,"echo \"");	//Normal Print Not Working << NI
	strcat(str2,str);
	strcat(str2,"\"");
	system(str2);
	
	system("sleep 1");
	


	gotoxy(2,height-1);
	int i;
	for(i=0;i<n;i++)
		putchar(B_B);
	
	update_xy();
	
}