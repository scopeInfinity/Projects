arrayindex=0
case "$1" in 
	-fn)
		arrayindex=1
		;;
	-dn) 
		arrayindex=2
		;;
	-pn)
		arrayindex=3
		;;
	-eid)
		arrayindex=4
		;;
	-rn)
		arrayindex=5
		;;
	*)
		arrayindex=100
		;;
esac
shift 1

echo "Output"
while read line;
do
	IFS=',' read -a array <<< "$line"
    
    match=true
    for input in "$@";
    do
    	input=$(echo $input | tr '[:upper:]' '[:lower:]')
    	if [[ "${array[$arrayindex],,}" != *"$input"* ]]
		then
			match=false
			break
		fi
	done;

	if $match && [ $# -ne 0 ]
	then
		echo "Full Name: ${array[1]}";
        echo "Designation: ${array[2]}";
        echo "Phone No: ${array[3]}";
        echo "Email ID: ${array[4]}";
        echo "Room No: ${array[5]}";
		echo
	fi
done < phonebook.txt
