// Name: Garcia Anthony John Abril
// ID: 4321819

#include "Sorts.h"

// Quicksort random element
void quickSort3(int *array, int left, int right)
{
	if (left < right)
	{
		// ensure that values generated are always random
		srand(time(NULL));
		// set pivot to a random element in the array
		int pivotIndex = left + rand() % (right - left + 1);
		int pivot = array[pivotIndex];

		swap(array, pivotIndex, right); // move pivot element to the end
		pivotIndex = right;

		int i = left - 1;

		// loop until left index meet right index
		for (int j = left; j < right; j++)
		{
			incrementComparison();
			// if left index value is less than pivot, swap
			if (array[j] <= pivot)
			{
				i = i + 1;
				swap(array, i, j);
			}
		}

		// put back pivot to correct position
		swap(array, i + 1, pivotIndex);
		int mid = i + 1;
		//recursion
		quickSort3(array, left, mid - 1);
		quickSort3(array, mid + 1, right);
	}
}
