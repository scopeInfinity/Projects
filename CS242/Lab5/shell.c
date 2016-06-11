#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <errno.h>
#include <dirent.h>


void parse(char *);
void sequentialcmds(char *);
void print_directory_listing();

int main(int argc, char const *argv[])
{
	int lno = 1;
	size_t buf_size = 1024;
	char *line = (char *)malloc(sizeof(char)*buf_size);
	
	int enter_count = 0;
	while(1)
	{
		if(!enter_count)
			printf("%d> ",lno++ );
		int n = getline(&line,&buf_size,stdin);
		line[n-1] = '\0';
		if(line[0]=='\0') //just Enter
			{
				enter_count++;
				if(enter_count == 2)
				{
					print_directory_listing();
					enter_count = 0;
				}
			}
		else {
			enter_count = 0;
			sequentialcmds(line);
		}

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
		printf("%s",word);
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

void print_directory_listing()
{
  struct dirent **namelist;
  int n = scandir(".", &namelist, NULL, alphasort);
  if (n < 0)
    return ;
  else
  {
    int i;
    //i=2, for skipping . and ..
    for(i=0;i<n;i++)
    {
        printf("%s\n", namelist[i]->d_name);
    }
  }
  return ;
}
int execute_file(char *filename, char *arguments)
{
	//arguments is filename here
	
	if( access( filename, X_OK ) != -1 )
	{
    	int pid = fork();
    	if (pid>0)
    	{
    		//Parent
    		int sig;
    		pid_t pid = wait(&sig);
			if(sig==0)
				printf("[shell: program terminated successfully]\n");
			else
				printf("[shell: program terminated abnormally][%d]\n",WEXITSTATUS(sig));
    	}
    	else if(pid==0)
    	{

    		//Child
    		char *newargv[20] = { filename };//18 arguments max
    		int i=1;
    		char *param = strtok(arguments," ");
    		
    		do
    		{
    			newargv[i++] = param;
    		}while(i<19 && (param=strtok(NULL," ")));
    		
    		newargv[i] = NULL;

    		char *newenviron[] = { NULL };
    		execve(filename,newargv,newenviron);
    	}
    	else
    		perror("Fork Failed!!\n");
    	return 1;
	}
	else
	{
	    return 0;
	}
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
	else if(execute_file(str,arguments));
	else printf("Invalid Command!\n");
}


void sequentialcmds(char *line)
{
	//Due to nested strtok
	char *end_token;
	char *cmd = strtok_r(line,";",&end_token);
	do//echo hello ; echo bye;
	{
		parse(cmd);
	}while(cmd=strtok_r(NULL,";",&end_token));
}