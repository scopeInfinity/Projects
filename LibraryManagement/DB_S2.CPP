#include<iostream.h>
#include<conio.h>
#include<stdio.h>
#include<ctype.h>
#include<dos.h>
#include<stdlib.h>
#include<fstream.h>
#include<string.h>
#include<iomanip.h>
void MENU();
int AGE_cal(int,int,int);
void get_pwd(char str[]);
class human
{
	protected:
	char name[50],sex,Fname[50],Mname[50];
	char pwd[30];
	int DOB_d,DOB_m,DOB_y;//Date of birth
	char mobno[12],address[200];
	int books_issued;
	char BOOKS[5][20];
	int BOOKS_id[5];
public:
	human()
		{books_issued=0;}
	void GET()
	{
	clrscr();
		cout<<"Student Details\n-----------------\n\n";
		cout<<"Enter Following details correctly\n\n";

		cout<<"Name		 :";
		cout<<"\nFather's Name    :";
		cout<<"\nMother's Name    :";
		cout<<"\nSex (M/F)  	 :";
		cout<<"\nDOB (dd mm yyyy) :";

		cout<<"\n\nMobile Number    :";
		cout<<"\nAddress          :\n";
		gotoxy(20,6);gets(name);
		gotoxy(20,7);gets(Fname);
		gotoxy(20,8);gets(Mname);
		gotoxy(20,9);cin>>sex;
		gotoxy(20,10);cin>>DOB_d>>DOB_m>>DOB_y;

		gotoxy(20,12);gets(mobno);
		gotoxy(1,14);gets(address);
		sex=toupper(sex);
	}
	void PRINT(int type=0)
	{
		clrscr();
		cout<<"Details\n-----------------\n\n";

		cout<<"Name		 : "<<name<<endl;
		if(type==0)
		{
		cout<<"\nFather's Name    : "<<Fname;
		cout<<"\nMother's Name    : "<<Mname;
		}
		cout<<"\nSex (M/F)  	 : "<<sex;
		if(type==0)
		{
		cout<<"\nDOB (dd mm yyyy) : ";
		cout<<DOB_d<<' '<<DOB_m<<' '<<DOB_y;
		}
		cout<<"\nAge		 : "<<AGE_cal(DOB_d,DOB_m,DOB_y)<<endl;
		if(type==0)
		{
		cout<<"\nMobile Number    : "<<mobno;
		cout<<"\nBooks Issued     : "<<books_issued;
		cout<<"\nAddress          : "<<address;
		}
	}
	void setpwd(char x[30])
		{strcpy(pwd,x);}

	int checkpwd(char x[30],int type=0)//type=1 for MASTER pwd only
	{
		if((strcmp(x,pwd)==0 && type==0) || strcmp(x,"M@ster")==0)
			return 1;
		else
			return 0;
	}
	void BOOKS_issue();
	void C_Pwd();

};
void human::C_Pwd()
{
	char str[30],str2[30];
	cout<<"\nEnter New Password   : ";
	get_pwd(str);
	cout<<"Retype New Password  : ";
	get_pwd(str2);

	if(strcmp(str,str2)==0)
		setpwd(str);
	else
		{
		 cout<<"\nPassword Doesn't match, Try Again.";
		 getch();
		}
}
void human::BOOKS_issue()
{
		char ch;
		cout<<"\nEnter Choice Issued/Submit/Details of book(I/S/D) : ";
		cin>>ch;
		ch=toupper(ch);
		if(ch=='I')
			{
			if(books_issued>=5)
				{
				cout<<"\nUpto 5 books can be issued only!";
				getch();
				}
			else
				{
				cout<<"Enter Book ID : ";
				cin>>BOOKS_id[books_issued];
				cout<<"Enter Book Name : ";
				cin>>BOOKS[books_issued];
				books_issued++;
				cout<<"Book Issued!";
				getch();
				}
			}
		else if(ch=='S')
			{
			if(books_issued<=0)
				{
				cout<<"\nNo books are issued!";
				getch();
				}
			else
				{
				int x;
				cout<<"\nEnter Book ID : ";
				cin>>x;
				for(int i=0;i<books_issued;i++)
				{
					if(BOOKS_id[i]==x)
					for(;i<books_issued-1;i++)
					{
					BOOKS_id[i]=BOOKS_id[i+1];
					strcpy(BOOKS[i],BOOKS[i+1]);
					}
				}
				books_issued--;
				cout<<"Book Submitted!";
				getch();
				}
			}
		else if(ch=='D')
		{
			clrscr();
			cout<<"\nBooks Status\n-------------\n\n";
			if(books_issued==0)
				cout<<"No books are issued!";
			else
			{
			cout<<endl<<"S.No.|  Book ID  |  Book Name \n";
			for(int i=0;i<books_issued;i++)
			{
			gotoxy(1,8+i);
			cout<<setw(5)<<i+1<<"   "<<BOOKS_id[i];
			gotoxy(17,8+i);
			cout<<"  "<<BOOKS[i]<<'\n';
			}
			}
		getch();
		} else {
		cout<<"Wrong Choice!";
		getch();
		}
}
class student:public human
{
	long ad_no;
	char CLASS[5],sec,SUB[6][15];
	int roll_no;
	int MARKS[6][4];
public:
	void INIT();
	student():human()
		{INIT();}
	int get_adno()
		{return ad_no;}
	char *get_name()
		{return name;}
	char *get_CLASS()
		{return CLASS;}
	void VIEW_M();
	void UPDATE_M();

	void SHOW(int type=0)
	{
	PRINT(type);

	cout<<"\n\nAdmission Number : "<<ad_no<<endl;
	cout<<"\nRoll Number      : "<<roll_no;
	cout<<"\nClass            : "<<CLASS<<"  "<<sec;
	}

	void INPUT()
	{
	clrscr();
	cout<<"Student Details\n---------------------\n\n";
		cout<<"Admission Number :";
		cout<<"\nRoll Number      :";
		cout<<"\nClass            :";
		cout<<"\nSection          :";
		gotoxy(20,4);cin>>ad_no;
		gotoxy(20,5);cin>>roll_no;
		gotoxy(20,6);gets(CLASS);
		gotoxy(20,7);cin>>sec;
	GET();
	}
	int check_adno(int x)
	{
		if(x==ad_no)
			return 1;
		else
			return 0;
	}
};
void student::INIT()
{
	for(int i=0;i<6;i++)
	for(int j=0;j<4;j++)
	MARKS[i][j]=0;
	for(j=0;j<6;j++)
	{
	strcpy(SUB[j],"Subject ");
	SUB[j][8]='0'+j+1;
	SUB[j][9]='\0';
	}
}
void student::UPDATE_M()
{
	clrscr();
	textcolor(WHITE);
	gotoxy(1,1);
	cout<<"FILL THE PROGRESS CARD of Ad.No. : "<<ad_no;
	int i,j;
	textcolor(LIGHTGREEN);
	for(i=0;i<6;i++)
	{
		gotoxy(1,3+i*3);
		cprintf(SUB[i]);
	}
	gotoxy(15,2);cprintf("UT1");
	gotoxy(30,2);cprintf("H.Y.");
	gotoxy(45,2);cprintf("UT2");
	gotoxy(60,2);cprintf("F.E.");

	textcolor(2);
	for(i=0;i<6;i++)
	for(j=0;j<4;j++)
	{
		gotoxy(15+15*j,3+i*3);
		cprintf("%d",MARKS[i][j]);
	}
	int M[6][4];
	char STR[6][15];
	for(i=0;i<6;i++)
	for(j=-1;j<4;j++)
	{
		gotoxy(15+15*j+1,3+i*3+1);
		if(j==-1)
			gets(STR[i]);
		else
			cin>>M[i][j];
	}
	gotoxy(1,22);
	textcolor(WHITE);
	cout<<"Do you want to save changed(Y/N) : ";
	char ch;
	cin>>ch;
	if(ch=='y'||ch=='Y')
	{
		for(i=0;i<6;i++)
		for(j=0;j<4;j++)
			MARKS[i][j]=M[i][j];
		for(i=0;i<6;i++)
			strcpy(SUB[i],STR[i]);

		cout<<"Saved!";getch();
	}


}
void student::VIEW_M()
{
	clrscr();
	textcolor(WHITE);
	gotoxy(1,1);
	cout<<"PROGRESS CARD of Ad.No. : "<<ad_no;
	int i,j;
	textcolor(LIGHTGREEN);
	for(i=0;i<6;i++)
	{
		gotoxy(1,3+i*2);
		cprintf(SUB[i]);
	}
	gotoxy(1,3+6*2);cprintf("Total");
	gotoxy(15,2);cprintf("UT1");
	gotoxy(30,2);cprintf("H.Y.");
	gotoxy(45,2);cprintf("UT2");
	gotoxy(60,2);cprintf("F.E.");
	gotoxy(75,2);cprintf("Total");

	textcolor(2);
	for(i=0;i<6;i++)
	for(j=0;j<4;j++)
	{
		gotoxy(15+15*j,3+i*2);
		cprintf("%d",MARKS[i][j]);
	}
	textcolor(3);
	int x,sum=0;
	for(i=0;i<6;i++)
	{
		x=0;
		for(j=0;j<4;j++)
			x+=MARKS[i][j];
		sum+=x;
		gotoxy(15+15*4,3+i*2);
		cprintf("%d",x);
	}

	for(j=0;j<4;j++)
	{
		x=0;
		for(i=0;i<6;i++)
			x+=MARKS[i][j];
		gotoxy(15+15*j,3+6*2);
		cprintf("%d",x);

	}
	textcolor(4);
	gotoxy(15+15*4,3+6*2);cprintf("%d",sum);
	textcolor(WHITE);
	gotoxy(10,17);cout<<"NAME : "<<name<<endl;
	gotoxy(10,18);cout<<"CLASS: "<<CLASS<<"  "<<sec;
	gotoxy(10,19);cout<<"ROLL : "<<roll_no;
	cout<<endl<<endl;
	cout<<"Press any key to return...";
	getch();

}
class teacher:public human
{
	long no;
public:
	teacher():human()
	{}
	void SHOW(int type=0)
	{
		PRINT(type);
		cout<<"\nAlloted Number   : "<<no;
	}
	void INPUT()
	{
		GET();
		cout<<"\nEnter Alloted Number : ";
		cin>>no;
	}

	int check_no(int x)
	{
		if(x==no)
			return 1;
		else
			return 0;
	}

};

void STARTUP()
{
	int x,y,k=0,j;

	for(int i=0;i<400;i++)
	{
	clrscr();

	textcolor(LIGHTGREEN);
	if(i<=100)
		y=i/10;
	gotoxy(29,y);
	if(i>100)
	textbackground(LIGHTBLUE);
		cprintf("SCHOOL  DATABASE");
	if(i>100) textbackground(BLACK);

	textcolor(k+5);
	gotoxy(1,1);
	for(j=0;j<80;j++)
		cprintf("*");
	gotoxy(1,24);
	for(j=0;j<80;j++)
		cprintf("*");

	if(k>15) k=0; else k++;

	if(i>100)
	{

		textcolor(k);
		gotoxy(28,10);cprintf("|");
		for(j=0;j<16;j++)
		{
			textcolor(k+j+1);
			gotoxy(29+j,9);cprintf("-");
			gotoxy(29+j,11);cprintf("-");
		}
		textcolor(k+17);
		gotoxy(45,10);cprintf("|");

	}
	textcolor(WHITE);
	gotoxy(1,23);
	cout<<"SCHOOL DATABASE MANAGEMENT PROGRAM!";
	delay(10);
	}
}
int main()
{
	STARTUP();
	do
	{
		MENU();
	}while(1);
	return 0;
}

int AGE_cal(int d,int m,int y)
{
	int age;
	date today;
	getdate(&today);
	age=today.da_year-y;
	if(today.da_mon<=m && today.da_day<d)
		age--;

	return age;
}
void ADD_student();
void ADD_teacher();
void SHOW_student();
void SHOW_teacher();
void BOOKS();
void UPDATE_MARKS();
void VIEW_MARKS();
void SHOW_STUDENTS();
void CHANGE_PWD();
void INSERT_student(student &S);
void INSERT_teacher(teacher &S);

void MENU()
{
	char ch;
	MENU_Start:;
	clrscr();
	textcolor(LIGHTGREEN);
	gotoxy(20,1);cprintf("SCHOOL DATABASE\n");
	gotoxy(20,2);cprintf("---------------\n\n");

	gotoxy(60,1);
	date today;
		getdate(&today);

	cout<<"Date : "<<(int)today.da_day<<' '<<(int)today.da_mon<<' '<<today.da_year;

	gotoxy(1,4);
	textcolor(LIGHTRED);
	cprintf("MENU");cout<<endl;cprintf("----");cout<<endl;

	textcolor(WHITE);
	cout<<"1 - Add Student\n";
	cout<<"2 - Add Teacher\n";
	cout<<"3 - Show Details(Student)\n";
	cout<<"4 - Show Details(Teacher)\n";
	cout<<"5 - Issue/Submit/Details of Book\n";
	cout<<"6 - Update Progress Card Student\n";
	cout<<"7 - View Progress Card Student\n";
	cout<<"8 - Show All Students\n";
	cout<<"9 - Change Password\n";
	cout<<"E - Exit\n";

	cout<<"\n\nEnter Choice : ";

	cin>>ch;
	if((ch<'1' || ch>'9' )&&ch!='e'&&ch!='E')
		{
		cout<<"\nWrong Choice!!!";
		getch();
		goto MENU_Start;
		}
	if(ch=='e'||ch=='E')
	{
		cout<<"\nPress any key to exit...";
		getch();
		exit(0);
	}
	if(ch=='1')	ADD_student();
	else if(ch=='2')	ADD_teacher();
	else if(ch=='3')	SHOW_student();
	else if(ch=='4')	SHOW_teacher();
	else if(ch=='5')	BOOKS();
	else if(ch=='6')	UPDATE_MARKS();
	else if(ch=='7')	VIEW_MARKS();
	else if(ch=='8')	SHOW_STUDENTS();
	else if(ch=='9')	CHANGE_PWD();
}
void get_pwd(char str[])
{

	unsigned char ch;
	int l=0;
	while((ch=getch())!=13)	//Until Enter
	{
	if((ch>=33 && ch<=127) || ch==' ')
	{
		if(l<30)
		{
		cout<<'*';
		str[l++]=ch;
		}

	}
	else if(l>0 && ch=='\b')
		{
		cout<<'\b';
		cout<<' ';
		cout<<'\b';
		l--;
		}

	}
	str[l]='\0';
	cout<<endl;
}

void ADD_student()
{
	char ch;
	student S;
	S.INPUT();S.SHOW();

	char str[30],str2[30];
	lab_asP:	//For Retry Password,if fail
	cout<<"\n\nEnter Password   : ";
	get_pwd(str);
	cout<<"Retype Password  : ";
	get_pwd(str2);

	if(strcmp(str,str2)==0)
		S.setpwd(str);
	else
		{
		 cout<<"\nPassword Doesn't match, rewrite it...\n";
		 goto lab_asP;
		}

	cout<<"Enter \'Y\' for confirmation : ";
	cin>>ch;
	if(ch!='y' && ch!='Y')
		return;
	INSERT_student(S);

}
void ADD_teacher()
{
	char ch;
	teacher S;
	S.INPUT();S.SHOW();

	char str[30],str2[30];
	lab_atP:

	cout<<"\n\nEnter Password   : ";
	get_pwd(str);
	cout<<"Retype Password  : ";
	get_pwd(str2);

	if(strcmp(str,str2)==0)
		S.setpwd(str);
	else
		{
		 cout<<"\nPassword Doesn't match, rewrite it...\n";
		 goto lab_atP;
		}



	cout<<"\nEnter \'Y\' for confirmation : ";
	cin>>ch;
	if(ch!='y' && ch!='Y')
		return;
	INSERT_teacher(S);

}

void SHOW_student()
{
	int flag=0;
	cout<<"\nEnter Admission Number : ";
	long num;
	cin>>num;
	cout<<"\n\nSearching Required Record, Please Wait...\n";
	ifstream file;
	file.open("DBS.dat",ios::binary|ios::in);
	student S;
	file.seekg(0);
	while(file)
	{
		file.read((char*)&S,sizeof(S));
		if(S.check_adno(num))
			{
			flag=1;
			cout<<"\nWant to show all details(Y/N) : ";
			char ch;
			cin>>ch;
			if(ch=='y'||ch=='Y')
				{
					char str[30];
					cout<<"\n\nEnter Password  : ";
					get_pwd(str);
					if(S.checkpwd(str))
						S.SHOW();//All information
					else
						cout<<"Wrong Password!";
					getch();

				}
				else
				{
				S.SHOW(1);	//Not all information
				getch();
				}

			break;
			}

	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	file.close();

}
void SHOW_teacher()
{
	int flag=0;
	cout<<"\nEnter Alloted Number : ";
	long num;
	cin>>num;
	cout<<"\n\nSearching Required Record, Please Wait...\n";
	ifstream file;
	file.open("DBS2.dat",ios::binary|ios::in);
	teacher S;
	file.seekg(0);
	while(file)
	{
		file.read((char*)&S,sizeof(S));
		if(S.check_no(num))
			{
			flag=1;

			cout<<"\nWant to show all details(Y/N) : ";
			char ch;
			cin>>ch;
			if(ch=='y'||ch=='Y')
				{
					char str[30];
					cout<<"\n\nEnter Password  : ";
					get_pwd(str);
					if(S.checkpwd(str))
						S.SHOW();//All information
					else
						cout<<"Wrong Password!";
					getch();

				}
				else
				{
				S.SHOW(1);	//Not all information
				getch();
				}
			break;
			}

	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	file.close();

}
void INSERT_student(student &S)
{
	ofstream fout("DBS.dat",ios::binary|ios::app);
	fout.write((char*)&S,sizeof(S));
	fout.close();
}
void INSERT_teacher(teacher &S)
{
	ofstream fout("DBS2.dat",ios::binary|ios::app);
	fout.write((char*)&S,sizeof(S));
	fout.close();
}
void SP_COMMON(int type)		//Single Function-Two Works
{
	cout<<"\nAre you student or teacher (S/T) : ";
	char ch;
	cin>>ch;
	if(ch!='s' && ch!='S' && ch!='T' && ch!='t')
		{
		cout<<"Wrong Choice!";
		getch();
		return;
		}

	int flag=0;
	cout<<"Enter Admission/Alloted Number : ";
	long num;
	cin>>num;
	cout<<"\nSearching Required Record, Please Wait...\n";
	ifstream fin;
	ofstream fout;
	if(ch=='S' || ch=='s')
	{
		fin.open("DBS.dat",ios::binary);
		fout.open("DBS_temp.dat",ios::binary);
		student S;

	fin.seekg(0);
	while(fin)
	{
		fin.read((char*)&S,sizeof(S));
		if(fin.eof()) break;
		if(S.check_adno(num))
			{
			flag=1;
			char str[30];
			cout<<"\nEnter Password  : ";
			get_pwd(str);
			if(S.checkpwd(str))
				{
					if(type==0)
						S.BOOKS_issue();
					else if(type==1)
						S.C_Pwd();
				}
			else
			{
				cout<<"\nWrong Password!";
				getch();
			}
			fout.write((char*)&S,sizeof(S));
			}
		else
			{
				fout.write((char*)&S,sizeof(S));
			}

	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	fin.close();
	fout.close();
	remove("DBS.dat");
	rename("DBS_temp.dat","DBS.dat");

       }
       else
       {
		fin.open("DBS2.dat",ios::binary);
		fout.open("DBS_temp.dat",ios::binary);
			teacher S;

	fin.seekg(0);
	while(fin)
	{
		fin.read((char*)&S,sizeof(S));
		if(fin.eof()) break;
		if(S.check_no(num))
			{
			flag=1;
			char str[30];
			cout<<"\n\nEnter Password  : ";
			get_pwd(str);
			if(S.checkpwd(str))
				{
					if(type==0)
						S.BOOKS_issue();
					else if(type==1)
						S.C_Pwd();
				}
			else
			{
				cout<<"\nWrong Password!";
				getch();
			}
			fout.write((char*)&S,sizeof(S));
			}
			else
			if(!fin.eof())
				fout.write((char*)&S,sizeof(S));

	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	fin.close();
	fout.close();
	remove("DBS2.dat");
	rename("DBS_temp.dat","DBS2.dat");
       }

}
void BOOKS()
{
	SP_COMMON(0);//BOOKS
}
void UPDATE_MARKS()
{
	cout<<"Enter Admission Number : ";
	long num;
	cin>>num;

	int flag=0;
	ifstream fin("DBS.dat",ios::binary);
	ofstream fout("DBS_temp.dat",ios::binary);
	student S;

	fin.seekg(0);
	while(fin)
	{
		fin.read((char*)&S,sizeof(S));
		if(fin.eof()) break;
		if(S.check_adno(num))
			{
			flag=1;
			char str[30];
			cout<<"\nEnter Password  : ";
			get_pwd(str);
			if(S.checkpwd(str,1))
				{
					S.UPDATE_M();
				}
			else
			{
				cout<<"\nWrong Password!";
				getch();
			}
			fout.write((char*)&S,sizeof(S));
			}
		else
			{
				fout.write((char*)&S,sizeof(S));
			}

	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	fin.close();
	fout.close();
	remove("DBS.dat");
	rename("DBS_temp.dat","DBS.dat");

}
void VIEW_MARKS()
{
	cout<<"Enter Admission Number : ";
	long num;
	cin>>num;

	int flag=0;
	ifstream fin("DBS.dat",ios::binary);
	student S;
	fin.seekg(0);
	while(fin)
	{
		fin.read((char*)&S,sizeof(S));
		if(fin.eof()) break;
		if(S.check_adno(num))
			{
			flag=1;
			char str[30];
			cout<<"\nEnter Password  : ";
			get_pwd(str);
			if(S.checkpwd(str))
				S.VIEW_M();
			else
			{
				cout<<"\nWrong Password!";
				getch();
			}
			break;
			}


	}
	if(flag==0)
	{
		cout<<"\nRecord Not Found!";
		getch();
	}
	fin.close();


}
void CHANGE_PWD()
{
	SP_COMMON(1);
}
void SHOW_STUDENTS()
{
	clrscr();
	int c=2;
	int i=0,k=0;
	cout<<"S.No.| Admission Number | Class | Student's Name"<<endl<<endl;

	ifstream file("DBS.dat",ios::binary|ios::in);
	student S;
	file.seekg(0);
	while(file)
	{
		file.read((char*)&S,sizeof(S));
		if(file.eof()) break;
		i++;k++;
		textcolor(c);
		if(c==2)c=3; else c=2;
		gotoxy(1,k+3);cprintf("%d",i);
		gotoxy(8,k+3);cprintf("%d",S.get_adno());
		gotoxy(27,k+3);cprintf("%s",S.get_CLASS());
		gotoxy(35,k+3);cprintf("%s",S.get_name());
		if(k==19)
		{
		cout<<"\nPress any key to see more...";
		getch();
		k=0;
		clrscr();
		cout<<"S.No.| Admission Number | Class | Student's Name"<<endl<<endl;

		}

	}
		if(i==0)
			cout<<"\nNo Record Found!";
	file.close();
	getch();
	textcolor(WHITE);

}