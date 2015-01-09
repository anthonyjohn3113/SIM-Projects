#!/bin/bash
clear
declare -a TITLE
declare -a AUTHOR
declare -a PRICE
declare -a -i QTYAVAIL
declare -a -i QTYSOLD
declare -a TOTALSALES
declare -i COUNT=0
declare bookexist
declare -i position
declare FILENAME="./BookDB.txt"

function readTextFile
{
	#check existance of bookdb file, create the file if not exist else continue
	if ! [ -f $FILENAME ] ; then
		touch$FILENAME
	fi
	
	while IFS=':' read -a LINE
	do
		TITLE[$COUNT]=${LINE[0]};
		AUTHOR[$COUNT]=${LINE[1]};
		PRICE[$COUNT]=${LINE[2]};
		QTYAVAIL[$COUNT]=${LINE[3]};
		QTYSOLD[$COUNT]=${LINE[4]};
		let COUNT++;
	done <$FILENAME
}

function writeTextFile
{
	for ((i=0; $i<${#TITLE[@]}; i++))
	do
		echo "${TITLE[$i]}:${AUTHOR[$i]}:${PRICE[$i]}:${QTYAVAIL[$i]}:${QTYSOLD[$i]}"
	done >$FILENAME
}

function press_enter
{
    echo ""
    echo -n "Press Enter to continue"
    read
    clear
}

function book_exist()
{
	for ((i=0; $i<${#TITLE[@]}; i++))
	do
		if test "${TITLE[$i]}" == "$1"; then
			if test "${AUTHOR[$i]}" == "$2"; then
				bookexist=true
				position=$i
			fi
		fi
	done
}

function add_new_book
{
	bookexist=true;
	while $bookexist
	do
		bookexist=false;
		invalid=true;
		while $invalid
		do
			invalid=false;
	    	read -p "Title: "  temp1
			echo""
			if test "$temp1" == ""; then
				tput setf 4;echo "Empty Input";tput setf 7;
				echo""
				invalid=true;
			fi
		done
		
		invalid=true;
		while $invalid
		do
			invalid=false
			read -p "Author: " temp2
			echo""
			if test "$temp2" == ""; then
				tput setf 4;echo "Empty Input";tput setf 7;
				echo""
				invalid=true;
			fi
		done
		
		book_exist "$temp1" "$temp2"
		
		if $bookexist; then
			tput setf 4;echo "Error! Book already exists!";tput setf 7;
			echo""
		fi
	done
	
	TITLE[$COUNT]=$temp1
	AUTHOR[$COUNT]=$temp2 
	
	invalid=true
	while $invalid
	do
		invalid=false;
		read -p "Price: " temp3
		echo""
		awk 'BEGIN{if ('$temp3'>'0') exit 1}'
		if [ $? -eq 1 ]; then
			PRICE[$COUNT]=$temp3;
		else
			tput setf 4;echo "Invalid Price";tput setf 7;
			echo""
			invalid=true;
		fi
	
	done
	
	invalid=true
	while $invalid
	do
		invalid=false;
		read -p "Qty Available:  " temp4
		echo""
		if [[ $temp4 =~ ^[\-0-9]+$ ]] && (( $temp4 > 0)); then
			QTYAVAIL[$COUNT]=$temp4;
		else
			tput setf 4;echo "Invalid Qty";tput setf 7;
			echo""
			invalid=true;
		fi
	done
		
	invalid=true
	while $invalid
	do
		invalid=false;
		read -p "Qty Sold: " temp5
		echo""
		if [[ $temp5 =~ ^[\-0-9]+$ ]] && (( $temp5 > 0)); then
			QTYSOLD[$COUNT]=$temp5;
		else
			tput setf 4;echo "Invalid Qty";tput setf 7;
			echo""
			invalid=true;
		fi
	done
	
	let COUNT++;
	
	echo "New book title ‘"$temp1"’ added successfully!"
	
}

function remove_existing_book
{
	exist=false;
	invalid=true;
	while $invalid
	do
		invalid=false;
    	read -p "Title: "  temp1
		echo""
		if test "$temp1" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			echo""
			invalid=true;
		fi
	done
	
	invalid=true;
	while $invalid
	do
		invalid=false
		read -p "Author: " temp2
		echo""
		if test "$temp2" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			echo""
			invalid=true;
		fi
	done
	
	book_exist "$temp1" "$temp2"
	
	if $bookexist; then

		unset TITLE[$position]
		unset AUTHOR[$position]
		unset PRICE[$position]
		unset QTYAVAIL[$position]
		unset QTYSOLD[$position]
		
		let COUNT--;
				
		echo "Book Title ‘"$temp1"’ removed successfully!"
		
	else
		tput setf 4;echo "Error! Book does not exists!";tput setf 7;
		echo""
	fi
}

function update_book_info
{
    bookexist=false;
	invalid=true;
	while $invalid
	do
		invalid=false;
    	read -p "Title: "  temp1
		echo""
		if test "$temp1" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			invalid=true;
		fi
	done
	
	invalid=true;
	while $invalid
	do
		invalid=false
		read -p "Author: " temp2
		echo""
		if test "$temp2" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			invalid=true;
		fi
	done
	
	book_exist "$temp1" "$temp2"
	x=$position

	if $bookexist; then
		
		echo "Book found!"
		echo ""
		
		selection=''
		until [ "$selection" = "f" ]; 
		do
			bookexist=false;
			echo "	a) Update Title"
			echo "	b) Update Author"
			echo "	c) Update Price"
			echo "	d) Update Qty Available"
			echo "	e) Update Qty Sold"
			echo "	f) Back to main menu"
			echo ""
			
			read -p "Please enter your choice: " selection
			
			case $selection in
				'a' ) 
						read -p "New Title: " temp1
						book_exist "$temp1" "${AUTHOR[$x]}"
						if $bookexist; then
							tput setf 4;echo "Error! Book already exists!";tput setf 7;
						else
							TITLE[$x]=$temp1
							echo "Book’s Title has been updated successfully!"
						fi
						press_enter ;;
							
				'b' )
						read -p "New Author: " temp2
						book_exist "${TITLE[$x]}" "$temp2"
						if $bookexist; then
							tput setf 4;echo "Error! Book already exists!";tput setf 7;
						else
							AUTHOR[$x]=$temp2
							echo "Book’s Author has been updated successfully!"
						fi
						press_enter ;;
				'c' )
						read -p "New Price: " temp3
						awk 'BEGIN{if ('$temp3'>'0') exit 1}'
						if [ $? -eq 1 ]; then
							PRICE[$x]=$temp3;
							echo "Book’s Price has been updated successfully!"
						else
							tput setf 4;echo "Invalid Price";tput setf 7;
						fi
						press_enter ;;
				'd' )
						read -p "New Qty Available:  " temp4
						if [[ $temp4 =~ ^[\-0-9]+$ ]] && (( $temp4 > 0)); then
							QTYAVAIL[$x]=$temp4;
							echo "Book’s Qty Available has been updated successfully!"
						else
							tput setf 4;echo "Invalid Qty";tput setf 7;
						fi
						press_enter ;;
				'e' )
						read -p "New Qty Sold: " temp5
						if [[ $temp5 =~ ^[\-0-9]+$ ]] && (( $temp5 > 0)); then
							QTYSOLD[$x]=$temp5;
							echo "Book’s Qty Sold has been updated successfully!"
						else
							tput setf 4;echo "Invalid Qty";tput setf 7;
							invalid=true;
						fi
						press_enter ;;
				'f' )	break;;
				 *	) tput setf 4;echo "Please enter a, b, c, d, e or f";tput setf 7; press_enter
			esac
			clear
		done
	else
		tput setf 4;echo "Error! Book does not exists!";tput setf 7;
	fi
	
}

function search_book
{
    read -p "Title: "  temp1
	echo""
	read -p "Author: " temp2
	echo""
	
	total=0;
	declare -a booksfound
	
	if test "$temp1" == ""; then
		for ((i=0; $i<${#TITLE[@]}; i++))
		do
			if echo "${AUTHOR[$i]}" | grep -q -io "$temp2"; then
				booksfound[$total]="${TITLE[$i]}, ${AUTHOR[$i]}, ${PRICE[$i]}, ${QTYAVAIL[$i]}, ${QTYSOLD[$i]}"
				let total++
			fi
		done
		
	elif test "$temp2" == ""; then
		for ((i=0; $i<${#TITLE[@]}; i++))
		do
			if echo "${TITLE[$i]}" | grep -q -io "$temp1"; then
				booksfound[$total]="${TITLE[$i]}, ${AUTHOR[$i]}, ${PRICE[$i]}, ${QTYAVAIL[$i]}, ${QTYSOLD[$i]}"
				let total++
			fi
		done
		
	else
		for ((i=0; $i<${#TITLE[@]}; i++))
		do
			if echo "${TITLE[$i]}" | grep -q -io "$temp1"; then
				if echo "${AUTHOR[$i]}" | grep -q -io "$temp2"; then
					booksfound[$total]="${TITLE[$i]}, ${AUTHOR[$i]}, ${PRICE[$i]}, ${QTYAVAIL[$i]}, ${QTYSOLD[$i]}"
					let total++
				fi
			fi
		done
	fi
	
	if test "$total" = 0; then
		tput setf 4;echo "Book not found!";tput setf 7;
		echo""
	else
		echo "Found $total records:"
		for ((i=0; $i<$total; i++))
		do
			echo "${booksfound[$i]}";
		done
	fi
}

function process_book_sold
{
    bookexist=false;
	invalid=true;
	while $invalid
	do
		invalid=false;
    	read -p "Title: "  temp1
		if test "$temp1" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			invalid=true;
		fi
	done
	echo""
	
	invalid=true;
	while $invalid
	do
		invalid=false
		read -p "Author: " temp2
		if test "$temp2" == ""; then
			tput setf 4;echo "Empty Input";tput setf 7;
			invalid=true;
		fi
	done
	echo""
	
	book_exist "$temp1" "$temp2"
	
	if $bookexist; then
		invalid=true;
		while $invalid
		do
				invalid=false
				read -p "No. of copies sold: " tempsold
				echo""
				balance=${QTYAVAIL[$position]}-$tempsold
				if [[ $tempsold =~ ^[\-0-9]+$ ]] && (( $tempsold > -1)); then
					
					if (( $balance > -1 )); then
						echo ""
						echo "Current book info: "
						echo "${TITLE[$position]}, ${AUTHOR[$position]}, ${PRICE[$position]}, ${QTYAVAIL[$position]}, ${QTYSOLD[$position]}"
						QTYAVAIL[$position]=${QTYAVAIL[$position]}-$tempsold
						QTYSOLD[$position]=${QTYSOLD[$position]}+$tempsold
						echo ""
						echo "New book info: "
						echo "${TITLE[$position]}, ${AUTHOR[$position]}, ${PRICE[$position]}, ${QTYAVAIL[$position]}, ${QTYSOLD[$position]}"
					else
					    tput setf 4;echo "No. of copies sold exceeds qty available";tput setf 7;
						echo ""
						invalid=true
					fi
					
				else
					tput setf 4;echo "Invalid Input";tput setf 7;
					echo""
					invalid=true
				fi
		done
	else
		tput setf 4;echo "Error! Book does not exists!";tput setf 7;
		echo""
	fi
}

function inventory_summary_report
{
	printf "%-40s\t%-15s\t$%-10s\t%-5s\t%-5s\t%-10s\n" "Title" "Author" "Price" "Qty Avail." "Qty Sold" "Total Sales"
	echo "---------------------------------------------------------------------------------------------------------------------------"
  	for ((i=0; $i<${#TITLE[@]}; i++))
	do
		printf "%-40s\t%-15s\t$%-0.2f\t\t%-3d\t\t%-3d\t\t$%-0.2f\n" "${TITLE[$i]}" "${AUTHOR[$i]}" "${PRICE[$i]}" "${QTYAVAIL[$i]}" "${QTYSOLD[$i]}" "$(echo ${PRICE[$i]}*${QTYSOLD[$i]} | bc)"
	done
}

readTextFile;
selection=0
until [ "$selection" = "7" ]; 
do
    echo ""
    echo ""
    echo "Advanced Book Inventory System"
    echo ""
    echo ""
    echo "	1.) Add new book"
    echo "	2.) Remove existing book info"
    echo "	3.) Update book info and quantity"
    echo "	4.) Search for book by title/author"
    echo "	5.) Process a book sold"
    echo "	6.) Inventory summary report"
    echo "	7.) Quit"
	echo ""

    read -p "Enter selection: " selection
    echo ""
    case $selection in
        1 ) add_new_book; press_enter;;
        2 ) remove_existing_book; press_enter ;;
        3 ) update_book_info; press_enter ;;
        4 ) search_book; press_enter ;;
        5 ) process_book_sold; press_enter;;
        6 ) inventory_summary_report; press_enter;;
        7 ) break ;;
        * ) tput setf 4;echo "Please enter 1, 2, 3, 4, 5, 6 or 7";tput setf 7; press_enter
    esac
	clear;
done
writeTextFile

