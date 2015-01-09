// Name: Garcia Anthony John Abril
// ID: 4321819

#ifndef SORTS_H
#define SORTS_H

#include <iostream>
#include <fstream>
#include <iomanip>
using namespace std;

static int swapCount = 0;
static int comparisonCount = 0;

void swap(int*, int, int);
void display(int*, int);
void incrementComparison();
void quickSort1(int*, int, int);
void quickSort2(int*, int, int);
void quickSort3(int*, int, int);
void heapSort(int*, int);

#endif // SORTS_H