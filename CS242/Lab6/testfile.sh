if [ -f $1 ] || [ -d $1 ];then
	ls -ld $1 | awk '{print $1}'
else
	echo "File Doesn't Exists"
fi 
