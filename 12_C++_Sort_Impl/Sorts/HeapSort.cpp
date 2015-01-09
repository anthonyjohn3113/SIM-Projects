// Name: Garcia Anthony John Abril
// ID: 4321819

#include "Sorts.h"

void siftDownRecursive(int*, int, int);
void siftDownIterative(int*, int, int);;
void makeHeap(int*, int);

// siftDown using recursion
void siftDownRecursive(int *array, int index, int size)
{
	// since array starts with 0, additional 1 is required so that
	// when index = 0, left = 1 and right = 2
	int left = (2 * index) + 1;
	int right = (2 * index) + 2;
	int parent = index;

	// set left as the larger value compared to index value
	incrementComparison();
	if ((left <= size) && (array[left] > array[index]))
		parent = left;

	// set right as the larger value compared to index value
	incrementComparison();
	if ((right <= size) && (array[right] > array[parent]))
		parent = right;

	// if parent value is changed, swap then recursion
	if (parent != index)
	{
		swap(array, index, parent);
		siftDownRecursive(array, parent, size);
	}
}
void siftDownIterative(int *array, int index, int size)
{
	int parent = index;
	int child;
	do
	{
		child = parent;
		int left = (2 * child) + 1;
		int right = (2 * child) + 2;

		// set left as the parentr value compared to index value
		incrementComparison();
		if ((left <= size) && (array[left] > array[parent]))
			parent = left;

		// set right as the parentr value compared to index value
		incrementComparison();
		if ((right <= size) && (array[right] > array[parent]))
			parent = right;

		// if child value is changed
		if (child != parent)
			swap(array, child, parent);

	}while(!(child == parent));
}
void makeHeap(int *array, int size)
{
	// This process calls 0.5n time of siftdown.
	// Siftdown has requires log2n times.
	// Thus makeheap needs nlog2n times.

	for (int i = size / 2; i >= 0; i--)
		siftDownRecursive(array, i, size);
}
void heapSort(int *array, int size)
{
	makeHeap(array, size);

	for (int i = size; i >= 0; i--)
	{
		swap(array, 0, i);
		siftDownRecursive(array, 0, i - 1);
	}
}
