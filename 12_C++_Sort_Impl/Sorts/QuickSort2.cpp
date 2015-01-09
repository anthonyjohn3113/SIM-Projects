// Name: Garcia Anthony John Abril
// ID: 4321819

#include "Sorts.h"

void manualSort(int*, int, int);

// Quicksort median element
void quickSort2(int *array, int left, int right)
{
	int size = right - left + 1;
	if (size <= 3) // manual sort if small
		manualSort(array, left, right);

	else
	{
		// Using median element of the first, middle, and last elements
		int middle = (left + right) / 2;

		// order left & center
		incrementComparison();
		if (array[left] > array[middle])
			swap(array, left, middle);

		// order left & right
		incrementComparison();
		if (array[left] > array[right])
			swap(array, left, right);

		// order center & right
		incrementComparison();
		if (array[middle] > array[right])
			swap(array, middle, right);

		// put pivot on right
		swap(array, middle, right - 1);

		// partition
		int pivotValue = array[right - 1];
		int LIndex = left; // right of first element
		int RIndex = right - 1; // left of pivot

		// loop forever
		while (true)
		{
			LIndex++;

			// shift left index while its value is less than pivot value
			while (array[LIndex] < pivotValue)
			{
				incrementComparison();
				LIndex++;
			}
			incrementComparison();

			RIndex--;
			// shift right index while its value is more than pivot value
			while (array[RIndex] > pivotValue)
			{
				incrementComparison();
				RIndex--;
			}
			incrementComparison();

			// if left index has not met right index then swap
			if (LIndex < RIndex)
				swap(array, LIndex, RIndex);
			else
				break;

		}

		// restore pivot
		swap(array, LIndex, right - 1);

		// recursion
		quickSort2(array, left, LIndex - 1);
		quickSort2(array, LIndex + 1, right);
	}
}

void manualSort(int *array, int left, int right)
{
	int size = right - left + 1;

	// no sort necessary
	if (size <= 1)
		return;

	// 2-sort left and right
	if (size == 2)
	{
		incrementComparison();
		if (array[left] > array[right])
			swap(array, left, right);
		return;
	}
	else
	// size is 3
	{
		// 3-sort left, center, & right

		// left, center
		incrementComparison();
		if (array[left] > array[right - 1])
			swap(array, left, right - 1);

		// left, right
		incrementComparison();
		if (array[left] > array[right])
			swap(array, left, right);

		// center, right
		incrementComparison();
		if (array[right - 1] > array[right])
			swap(array, right - 1, right);
	}
}

