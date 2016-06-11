#include <stdio.h>
#include <string.h>


int contains(char *,char *);
int main(int argc,char **argv)
{
  if(argc<3)
  {
    printf("Usage : ./grep <filename> <string>\n");
    return -1;
  }
  FILE *file = fopen(argv[1],"r");
  char buffer[1024];
  while(1)
  {
    fscanf(file,"%[^\n]",buffer);
    fscanf(file,"%*c"); //Ignore newline
    if(feof(file))
      break;
    if(contains(buffer,argv[2]))
      printf("%s\n",buffer );
  }
  return 0;
}
int contains(char *line,char *str)
{
  char copy[1024];
  strcpy(copy,line);

  int len = strlen(line);
  int lens = strlen(str);
  if(lens>len) return 0;
  char *myword = (copy+len-1)-(lens-1);//Last Possible Match
  while(myword>=copy)
  {
    *(myword+lens) = '\0';
    if(strcmp(myword,str)==0)
      return 1;
    myword--;
  }
  return 0;
}