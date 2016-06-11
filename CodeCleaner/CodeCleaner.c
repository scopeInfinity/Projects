#include<stdio.h>
void makeGood(FILE *,FILE*,int);
int main()
{
	
	FILE *fin,*fout;
	char fname[100],f2name[100];
	printf("Enter the program filename : ");
	scanf("%[^\n]",fname);
	fin=fopen(fname,"r");
	if(fin==NULL)
	{
		printf("Error in Reading File!!!!\n");
		return 1;
	}
	printf("Enter new program Filename : ");
	scanf(" %[^\n]",f2name);

	fout=fopen(".temp","w");
	if(fout==NULL)
	{
		printf("Error in Writing .temp File!!!!\n");
		return 2;
	}
	int ch;
	while((ch=fgetc(fin))!=EOF)
	{
		makeGood(fin,fout,ch);
	}
	fclose(fin);
	fclose(fout);



	fin=fopen(".temp","r");
	fout=fopen(f2name,"w");
	if(fin==NULL || fout==NULL)
	{
		printf("Error in Making Line Numbers!!!!\n");
		return 3;
	}
	int ln=0;
	fprintf(fout,"%3d ",ln+1);
	while((ch=fgetc(fin))!=EOF)
	{
		fputc(ch,fout);
		
		if(ch=='\n')
		{
			ln++;
			fprintf(fout,"%3d ",ln+1);
		}
	}
	fprintf(fout,"\n");
	fclose(fin);
	fclose(fout);
	
	return 0;
}
void makeIndent(FILE *fout,int c)
{
	int i;
	for(i=0;i<c;i++)
		fputc('\t',fout);
}
void makeGood(FILE *fin,FILE *fout,int ch)
{
	static long last_cpar_pos=-1;
	static int c_b=0;//count braces
	static int newline_done=0;
	static int last_cpar=0;
	static int c_par=0;	
	if(ch=='"')	//For string
	{
	fputc('"',fout);
	int esc_dq=0;
		
	while(((ch=fgetc(fin))!='"') || esc_dq==1 )
		{
			fputc(ch,fout);
			
			if(ch=='\\' && esc_dq==0)
				esc_dq=1;
			else if(esc_dq)
				esc_dq=0;
			
			
		}
	
	fputc('"',fout);
	return ;
	}
	if(ch!='\n' && ch!='\t' && ch!='}')
		fputc(ch,fout);
	switch(ch)
	{
		case '{':c_b++; 
		case '>': 
			fputc('\n',fout);
			makeIndent(fout,c_b);
			break;
		case '}':
			fseek(fout,-1,1);
			fputc('}',fout);
			c_b--;
			fputc('\n',fout);
			makeIndent(fout,c_b);
			break;
		case ';':
			if(last_cpar)
			{
			fseek(fout,last_cpar_pos,0);
			fputc(';',fout);
			fputc('\n',fout);
			makeIndent(fout,c_b);
			}
			else
			{
			fputc('\n',fout);
			makeIndent(fout,c_b);
			}
			break;
		case '(':c_par++;
			break;
		case ')':c_par--;
			if(c_par==0)
			{
				fputc('\n',fout);
				makeIndent(fout,c_b);
			}
			break;
	}
	if(ch==')')
		{
		last_cpar=1;
		last_cpar_pos=ftell(fout)-c_b-1;	//sub for indent and new line
		}
	else if(ch!=' ' && ch!='\n' && ch!='\t')
		last_cpar=0;	
}

