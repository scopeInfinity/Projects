#include <iostream>
#include <set>
#include <algorithm>

using namespace std;

void tolower(string &s){
	string a;
	for (int i = 0; s[i]!='\0'; ++i) {
		if(s[i]>='A' && s[i]<='Z')
			s[i]+='a'-'A';
		if(s[i]>='a' && s[i]<='z')
			a+=s[i];
	}
	s=a;

}

int main(int argc, char const *argv[])
{
	if(argc<2)
	{
		cerr<<"No Argument Given!\n";
		return -1;
	}
	string mine = string(argv[1]);
	tolower(mine);
	sort(mine.begin(),mine.end());
	cout<<mine<<endl;
	string s;
	set<string> possible;
	while(getline(cin, s)) {
		tolower(s);
		string org = s;
		sort(s.begin(),s.end());
		if (s == mine)
			possible.insert(org);
	}
	if(possible.size()>0)
	{
		cout<<"\nPossible Outputs : \n";
		for (std::set<string>::iterator i = possible.begin(); i != possible.end(); ++i)
		{
			cout<<*i<<endl;	
		}
	}
	else
		cout<<"\nNo Possible Match Found!\n";
	return 0;
}