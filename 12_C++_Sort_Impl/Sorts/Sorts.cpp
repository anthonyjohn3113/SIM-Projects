// Name: Garcia Anthony John Abril
// ID: 4321819

#include "Sorts.h"

int outputMode();
void sorts(int*, int, char *);
void sortOutMode(int *, int, char *, int, int);
int readFromFile(int*, char*, int);
bool isSorted(int*, int);
void incrementSwap();

int main()
{
	char another;
	int dSize = 0;
	do
	{
		// filenames
		char file1[] = "A3Data01.txt";
		char file2[] = "A3Data02.txt";

		// input for data size
		do
		{
			cout << "\nEnter data size: ";
			cin >> dSize;
			cin.clear();
			cin.ignore(100, '\n');
			// input data size as -1, output mode for easier export of values
			if(dSize == -1)
				return outputMode();
			else if (dSize < 1)
				cout << "Size must be more than 0" << endl;
		} while (dSize < 1);

		int *array1 = new int[dSize];
		int *array2 = new int[dSize];

		int size1 = readFromFile(array1, file1, dSize);
		int size2 = readFromFile(array2, file2, dSize);

		if (size1 == -1 || size2 == -1)
			return -1;

		// switch case to decide which data to use
		int datachoice;
		cout << "\nDisplay which data: " << "\n1. " << file1 << " data"
			 << "\n2. " << file2 << " data" << "\n3. Exit" << "\nChoice: ";
		cin >> datachoice;
		cin.clear();
		cin.ignore(100, '\n');

		switch (datachoice)
		{
		case 1:
			sorts(array1, size1, file1);
			break;
		case 2:
			sorts(array2, size2, file2);
			break;
		case 3:
			break;
		default:
			cout << "Invalid choice" << endl;
		}

		cout << "\nAnother(y/n)?: ";
		cin >> another;
		cin.clear();
		cin.ignore(100, '\n');

	} while (another == 'y');

	return 0;
}

// automated output for data export
int outputMode()
{
	int dSize = 0;
	char sortType[4][200] =
	{ "Quicksort first element", "Quicksort median element",
	  "Quicksort random element", "Heapsort" };
	char outType[2][200] = {"swaps", "comparisons"};
	for (int k = 0; k < 2; k++)
	{
		for (int j = 0; j < 4; j++)
		{
			cout << "Data 1 number of " << outType[k] << ": " << sortType[j] << endl;
			for (int i = 1; i <= 10; i++)
			{
				char file1[] = "A3Data01.txt";
				char file2[] = "A3Data02.txt";

				dSize = i * 1000;

				int *array1 = new int[dSize];
				int *array2 = new int[dSize];

				int size1 = readFromFile(array1, file1, dSize);
				int size2 = readFromFile(array2, file2, dSize);

				if (size1 == -1 || size2 == -1)
					return -1;

				int sortchoice = j + 1;
				int sorc = k + 1;
				sortOutMode(array1, size1, file1, sortchoice, sorc);
			}
		}
		for (int j = 0; j < 4; j++)
		{
			cout << "Data 2 number of " << outType[k] << ": " << sortType[j] << endl;
			for (int i = 1; i <= 10; i++)
			{
				char file1[] = "A3Data01.txt";
				char file2[] = "A3Data02.txt";

				dSize = i * 1000;

				int *array1 = new int[dSize];
				int *array2 = new int[dSize];

				int size1 = readFromFile(array1, file1, dSize);
				int size2 = readFromFile(array2, file2, dSize);

				if (size1 == -1 || size2 == -1)
					return -1;

				int sortchoice = j + 1;
				int sorc = k + 1;
				sortOutMode(array2, size2, file2, sortchoice, sorc);
			}
		}
	}
	return 0;
}

// reading of values from the file
int readFromFile(int *array, char *fileName, int size)
{
	fstream file;
	file.open(fileName, ios::in);

	if (!file)
	{
		cout << fileName << " failed to open" << endl;
		return -1;
	}

	int noOfInt;
	file >> noOfInt;

	int count = 0;
	while (count < size && file >> array[count])
		count++;

	file.close();

	return count;
}

// sorting of data without choice of which sort
void sortOutMode(int *array, int size, char * fileName, int sortchoice, int sorc)
{
	extern int swapCount;
	swapCount = 0;
	extern int comparisonCount;
	comparisonCount = 0;

	switch (sortchoice)
	{
	case 1:
		quickSort1(array, 0, size - 1);
		break;
	case 2:
		quickSort2(array, 0, size - 1);
		break;
	case 3:
		quickSort3(array, 0, size - 1);
		break;
	case 4:
		heapSort(array, size - 1);
		break;
	default:
		cout << "Invalid choice" << endl;
	}

	if (isSorted(array, size))
	{
		switch (sorc)
		{
		case 1:
			cout << swapCount << endl;
			break;
		case 2:
			cout << comparisonCount << endl;
			break;
		}
	}
}

// sort menu for normal operation
void sorts(int *array, int size, char * fileName)
{
	int sortchoice;
	char sortType[4][200] =
	{ "Quicksort first element", "Quicksort median element",
	  "Quicksort random element", "Heapsort" };
	cout << "\nDisplay which sort: " << "\n1. " << sortType[0] << "\n2. "
			<< sortType[1] << "\n3. " << sortType[2] << "\n4. " << sortType[3]
			<< "\nChoice: ";
	cin >> sortchoice;
	cin.clear();
	cin.ignore(100, '\n');

	extern int swapCount;
	swapCount = 0;
	extern int comparisonCount;
	comparisonCount = 0;

	cout << "\nBefore Sort:" << endl;
	display(array, size);

	switch (sortchoice)
	{
	case 1:
		quickSort1(array, 0, size - 1);
		break;
	case 2:
		quickSort2(array, 0, size - 1);
		break;
	case 3:
		quickSort3(array, 0, size - 1);
		break;
	case 4:
		heapSort(array, size - 1);
		break;
	default:
		cout << "Invalid choice" << endl;
	}

	cout << "\n\nAfter Sort:" << endl;
	display(array, size);

	if (isSorted(array, size))
	{
		cout << "\nNumber of swaps for " << sortType[sortchoice - 1]
			 << " with data from " << fileName << " = " << swapCount << endl;
		cout << "Number of comparisons for " << sortType[sortchoice - 1]
			 << " with data from " << fileName << " = " << comparisonCount
			 << endl;
	}
}

// swap function which is available globally
void swap(int *array, int i, int j)
{
	int temp = array[i];
	array[i] = array[j];
	array[j] = temp;
	incrementSwap();
}

// display the array values
void display(int *array, int size)
{
	cout << endl;
	for (int count = 0; count < size;)
	{
		for (int j = 0; j < 100; j++)
		{
			if (count >= size)
				break;
			else
				cout << left << setw(6) << array[count];
			count++;
		}
		cout << endl;
	}
}

// check if the array is sorted
bool isSorted(int *array, int size)
{
	for (int i = 1; i < size; i++)
	{
		if (array[i] < array[i - 1])
		{
			cout << "\nError in sort with values: " << array[i] << " "
				 << array[i - 1];
			return false;
		}
	}
	return true;
}

// increment comparison value
void incrementComparison()
{
	extern int comparisonCount;
	comparisonCount++;
}

// increment swap value
void incrementSwap()
{
	extern int swapCount;
	swapCount++;
}

