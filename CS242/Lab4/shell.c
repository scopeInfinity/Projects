#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>



void parse(char *);
int main(int argc, char const *argv[])
{
	int lno = 1;
	while(1)
	{
		char line[1024];
		printf("%d> ",lno++ );
		scanf(" %[^\n]",line);
		parse(line);

	}
	return 0;
}
void function_cd(char *arguments)
{
	int ret = chdir(arguments);
	if(ret!=0)
		printf("Error!\n");
}

void function_pwd(char *arguments)
{
	char cwd[300];
	getcwd(cwd,300);
	printf("%s\n",cwd );
}

void function_echo(char *arguments)
{
	char newargument[1024];
	strcpy(newargument+1, arguments);
	newargument[0] = ' ';
	char *word = strtok(newargument,"$");//abcd$HEY asd
	while(word)
	{
		printf("%s",word );
		word = strtok(NULL," \t\n:\"\'[]{}<>,.?/\\");
		if(word)
		{
			char *value = getenv(word);
			if(value)
				printf("%s",value);
			else //No Envirnment Varible Found
				printf("$%s",word );
		}
	word = strtok(NULL,"$");
	}
	printf("\n");
} 

void function_exit(char *arguments)
{
	exit(0);
}

extern char **environ;

void function_env(char *arguments)
{
	char **env = environ;
	for (; *env; env++) 
    	printf("%s\n", *env);
  
}

void function_setenv(char *arguments)
{
	char *name = strtok(arguments," ");
	char *value = strtok(NULL,"");
	setenv(name,value,1);
}


void parse(char *line)
{
	char *str = strtok(line," ");
	char *arguments = strtok(NULL,"");
	if(!strcmp(str,"cd"))
		function_cd(arguments);
	else if(!strcmp(str,"pwd"))
		function_pwd(arguments);
	else if(!strcmp(str,"echo"))
		function_echo(arguments);
	else if(!strcmp(str,"exit"))
		function_exit(arguments);
	else if(!strcmp(str,"env"))
		function_env(arguments);
	else if(!strcmp(str,"setenv"))
		function_setenv(arguments);
	else printf("Invalid Command!\n");
}

