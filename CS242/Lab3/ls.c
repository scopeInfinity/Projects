#include <stdio.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <unistd.h>
#include <grp.h>
#include <pwd.h>

//For Each File,, print Complete Detail
void printFileInfo(char *);

int main(void)
{
  struct dirent **namelist;
  int n = scandir(".", &namelist, NULL, alphasort);
  if (n < 0)
    return -1; //Error
  else
  {
    int i;
    //i=2, for skipping . and ..
    for(i=0;i<n;i++)
    {
        printFileInfo(namelist[i]->d_name);
    }
  }
  return 0; //Normal Exit
}

//Converting Epoc time to String
char buffer_time[100];
void setTime(time_t _time)
{
  strcpy(buffer_time,ctime(&_time));
  buffer_time[strlen(buffer_time)-1]='\0';
}

//Get Group Name from groupid
char *getGroup(int id)
{
  return getgrgid(id)->gr_name;
}

//Get Username from userid
char *getUsername(int id)
{
  return getpwuid(id)->pw_name;
}

//Buffer to store filepermission
char buffer_mode[11];
void setMode(int mode)
{
  buffer_mode[0]='-';
  int i;

  //List of Permission to extract from mode.
  int FLAGS[]={S_IRUSR,S_IWUSR,S_IXUSR,S_IRGRP,S_IWGRP,S_IXGRP,S_IROTH,S_IWOTH,S_IXOTH};
  for(i=0;i<9;i++)
  {
    char x='-';
    if(mode&FLAGS[i])
    {
      if(i%3==0)
        x='r';
      else if(i%3==1)
        x='w';
      else 
        x='x';
    }
    buffer_mode[i+1]=x;

  }
  buffer_mode[10]='\0'; //Terminating the String
}

//Print complete details of file using filename
void printFileInfo(char *name)
{
  struct stat sblock; //All Info of File
  if (stat(name, &sblock) == -1) {
      //In case of Error Return
       return;
  }

  int userid = sblock.st_uid;
  int groupid = sblock.st_gid;
  //File Size in bytes
  int size = sblock.st_size;
  //Permission
  int mode = sblock.st_mode; 
  //Last Modified Time
  int mod_time = sblock.st_mtime;
  setTime(mod_time);
  setMode(mode);
  //Printing row in formatted way
  printf("%s %-10s %-10s %10d %s %s\n",buffer_mode, getGroup(groupid), getUsername(userid), size, buffer_time, name);

}
