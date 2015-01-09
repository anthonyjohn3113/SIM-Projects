// Name: Garcia Anthony John Abril
// ID: 4321819

#include "Sorts.h"

// Quicksort first element
void quickSort1(int *array, int left, int right)
{
	// Pivot from first element
	int pivotValue = array[left];
	int LIndex = left;
	int RIndex = right;

	// Partition
	while (LIndex <= RIndex)
	{
		LIndex++;

		// left index shifts to the right until its value is more than pivot value
		while (array[LIndex] < pivotValue && LIndex < right)
		{
			incrementComparison();
			LIndex++;
		}
		incrementComparison();

		// right index shifts to the left until its value is less than pivot value
		while (array[RIndex] > pivotValue && RIndex > left)
		{
			incrementComparison();
			RIndex--;
		}
		incrementComparison();

		// swap when left index and right index did not meet
		if (LIndex <= RIndex)
			swap(array, LIndex, RIndex);
	}

	// Swap first element with Right Index
	swap(array, left, RIndex);

	// Recursion
	if (left < RIndex - 1)
		quickSort1(array, left, RIndex - 1);
	if (right > LIndex)
		quickSort1(array, LIndex, right);
}

