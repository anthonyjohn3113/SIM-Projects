// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "SparseException.h"
#include "SparseMatrix.h"

// 1st menu
void properties();
void constructMatrix(SparseMatrix&,Element);
void findElement(SparseMatrix&,Element);
void deleteElement(SparseMatrix&,Element);
void duplicateMatrix(SparseMatrix&);
void displayRMO(SparseMatrix&);
void displayMatrix(SparseMatrix&);

// 2nd menu
void operations();
void addition();
void subtraction();
void multiplication();

void separator(char);

int main()
{
	srand(time(NULL));
	char choice;
	do
	{
		cout << "\nIntroduction to Sparse Matrices\n"
			 << "\n1.  Properties of Sparse Matrices"
			 << "\n2.  Sparse Matrices Operations"
			 << "\n9.  Quit"
			 << "\n\nYour option: ";
		cin  >> choice;
		cin.clear();
		cin.ignore(100,'\n');
		separator('=');
		switch(choice)
		{
			case '1': properties();
					  break;
			case '2': operations();
					  break;
		}
	
	}while(choice!='9');
	return 0;
}

void properties()
{
	char choice;
	SparseMatrix matrix;
	Element e;
	do
	{
		cout << "\nBasic Properties of Sparse Matrices\n"
			 << "\n1.  Construct sparse matrix"
			 << "\n2.  Find an element"
			 << "\n3.  Delete an element"
			 << "\n4.  Duplicate another sparse matrix"
			 << "\n5.  Display sparse matrix (row major order)"
			 << "\n6.  More user friendly display of sparse matrix"
			 << "\n9.  Quit"
			 << "\n\nYour option: ";
		cin  >> choice;
		cin.clear();
		cin.ignore(100,'\n');
		separator('=');
		switch(choice)
		{
			case '1': cout << "\nMatrix constructed will be used for this sub-menu\n";
					  constructMatrix(matrix,e);
					  separator('-');
					  displayRMO(matrix);
					  separator('=');
					  break;
			case '2': findElement(matrix,e);
					  break;
			case '3': deleteElement(matrix,e);
					  break;
			case '4': duplicateMatrix(matrix);
					  break;
			case '5': displayRMO(matrix);
					  separator('=');
					  break;
			case '6': displayMatrix(matrix);
					  break;
		}
	}while(choice!='9');
}

void constructMatrix(SparseMatrix &matrix,Element e)
{
	int no;
	
	cout << "\nHow many elements?: ";
	cin >> no;
	cin.clear();
	cin.ignore(100,'\n');
	cout << endl;

	for (int i=1; i<=no; i++)
	{
		do
		{
			e.set(rand()%MAXROW,rand()%MAXCOL);
			e.setValue(rand()%201 - 100);
		}while(matrix.exist(e));

		matrix.insertElement(e);

		e.printElement();
		cout << " inserted to matrix\n";
	}
}

void findElement(SparseMatrix &matrix,Element e)
{
	char choice;
	do
	{
		int row, col;
		cout << "\nFind an element";
		cout << "\nRow number = ";
		cin  >> row;
		cin.clear();
		cin.ignore(100,'\n');

		cout << "Column number = ";
		cin  >> col;
		cin.clear();
		cin.ignore(100,'\n');

		e.set(row,col);

		if(matrix.exist(e))
			e.setValue(matrix.findValue(e));
		else
			e.setValue(0);

		cout << "Value = " << e.getValue();

		cout << "\nContinue (Y/N)?: ";
		cin  >> choice;
		cin.clear();
		cin.ignore(100,'\n');
		choice = toupper(choice);

	}while(choice=='Y');

	separator('=');
}

void deleteElement(SparseMatrix &matrix,Element e)
{
	char choice;
	do
	{
		int row, col;

		try
		{
			cout << "\nDelete an element";
			cout << "\nRow number = ";
			cin  >> row;
			cin.clear();
			cin.ignore(100,'\n');

			cout << "Column number = ";
			cin  >> col;
			cin.clear();
			cin.ignore(100,'\n');

			e.set(row,col);

			if(!matrix.exist(e))
				throw SparseException("Element not found, delete fail\n");
			else
				cout << "Element found and deleted\n";
			if(!matrix.deleteElement(e))
				throw SparseException("Delete fail\n");
		}
		catch(SparseException s)
		{
			s.printMessage();
		}

		cout << "\nContinue (Y/N)?: ";
		cin  >> choice;
		cin.clear();
		cin.ignore(100,'\n');
		choice = toupper(choice);

	}while(choice=='Y');

	separator('-');

	cout << "\nSummary after the deletion\n";
	displayRMO(matrix);

	separator('=');
}

void duplicateMatrix(SparseMatrix &matrix)
{
	try
    {
		if(matrix.Head()==NULL)
			throw SparseException("Sparse matrix empty, duplicating failed\n");

		cout << "\nTest of copy constructor\n";
		SparseMatrix copy(matrix);
		cout << "\nHere is the duplication\n";
		displayRMO(copy);
		separator('-');
	}
	catch(SparseException s)
	{
		cout << endl;
		s.printMessage();
		separator('=');
	}
}

void displayRMO(SparseMatrix &matrix)
{
	cout << "\nSparse matrix - row major order display\n\n";
	NodePtr curr = new Node;
	curr = matrix.Head();

	try
    {
		if(curr == NULL)
			throw SparseException("Sparse matrix empty, printing failed\n");

		while (curr != NULL)
		{
			curr->e.printElement();
			cout << '\n';
			curr = curr->next;
		}
	}
	catch(SparseException s)
	{
		s.printMessage();
	}
}

void displayMatrix(SparseMatrix &matrix)
{
	int numarray[MAXROW][MAXCOL];
	for(int i=0;i<MAXROW;i++)
	{
		for(int j=0;j<MAXCOL;j++)
			numarray[i][j] = 0;
	}

	matrix.printMatrix(numarray);

	int row, col;
	cout << "\nUser friendly display of sparse matrix\n";

	cout << "\nEnter maximum number of row: ";
	cin  >> row;
	cin.clear();
	cin.ignore(100,'\n');

	cout << "Enter maximum number of column: ";
	cin  >> col;
	cin.clear();
	cin.ignore(100,'\n');

	separator('-');
	cout << endl;

	for(int i=0;i<row;i++)
	{
		for(int j=0;j<col;j++)
			cout << right	<< setw(4) 
			     << numarray[i][j];
		cout << endl;
	}

	separator('=');
}

void operations()
{
	char choice;
	do
	{
		cout << "\n    Matrix operations"
			 << "\nThree types of operations\n"
			 << "\n+ :  Addition"
			 << "\n- :  Subtraction"
			 << "\n* :  Multiplication"
			 << "\n9 :  Quit"
			 << "\n\nYour operation: ";
		cin  >> choice;
		cin.clear();
		cin.ignore(100,'\n');
		separator('=');
		switch(choice)
		{
			case '+': addition();
					  break;
			case '-': subtraction();
					  break;
			case '*': multiplication();
					  break;
		}

	}while(choice!='9');
}

void addition()
{
	SparseMatrix matrixA, matrixB;
	Element e;
	cout << "\nAdd two matrices A and B"
		 << "\nNote that the addition is A += B"
		 << "\ni.e. after adding, A is updated\n";

	constructMatrix(matrixA,e);
	constructMatrix(matrixB,e);
	separator('-');
	cout << "\nMatrix A\n";
	displayRMO(matrixA);
	cout << "\nMatrix B\n";
	displayRMO(matrixB);

	matrixA += matrixB;

	cout << "\n*** The sum A + B ***\n";
	displayRMO(matrixA);
	separator('=');
}

void subtraction()
{
	SparseMatrix matrixA, matrixB;
	Element e;
	cout << "\nSubtract two matrices A and B"
		 << "\nNote that the subtraction is A -= B"
		 << "\ni.e. after subtracting, A is updated\n";

	constructMatrix(matrixA,e);
	constructMatrix(matrixB,e);
	separator('-');
	cout << "\nMatrix A\n";
	displayRMO(matrixA);
	cout << "\nMatrix B\n";
	displayRMO(matrixB);

	matrixA -= matrixB;

	cout << "\n*** The difference A - B ***\n";
	displayRMO(matrixA);
	separator('=');
}

void multiplication()
{
	SparseMatrix matrixA, matrixB;
	Element e;
	cout << "\nMultiply two matrices A and B"
		 << "\nNote that the product is A *= B"
		 << "\ni.e. after multiplying, the function"
		 << "\nreturns a product\n";

	constructMatrix(matrixA,e);
	constructMatrix(matrixB,e);
	separator('-');
	cout << "\nMatrix A\n";
	displayRMO(matrixA);
	cout << "\nMatrix B\n";
	displayRMO(matrixB);

	if(matrixA.Head()!=NULL&&matrixB.Head()!=NULL)
	{
		// result goes into matrix A but is unsorted
		matrixA*=matrixB;
		// sorted result goes in to martix B
		matrixB.Sort(matrixA);
	}

	cout << "\n*** The product A * B ***\n";
	displayRMO(matrixB);
	separator('=');
}

void separator(char dash)
{
	cout << endl
		 << setw(60) << setfill(dash)
		 << dash     << setfill(' ')
		 << endl;
}

