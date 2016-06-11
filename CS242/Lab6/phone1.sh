echo -n "Enter the name to search:"
read input
input=$(echo $input | tr '[:upper:]' '[:lower:]')
echo "Output"
while read line;
do
	IFS=',' read -a array <<< "$line"
        
	if [[ "${array[1],,}" == *"$input"* ]]
	then
		echo "Full Name: ${array[1]}";
                echo "Designation: ${array[2]}";
                echo "Phone No: ${array[3]}";
                echo "Email ID: ${array[4]}";
                echo "Room No: ${array[5]}";
		echo
	fi
done < phonebook.txt
