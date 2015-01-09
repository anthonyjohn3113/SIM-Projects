#ifndef PATHFINDER_H_
#define PATHFINDER_H_

#include "Maze.h"
#include "SubmitMazeSoln.h"
#include "Path.h"
#include "Assignm3_Utils.h"
#include "Assignm3.h"
#include "ProgramLog.h"

#include <cstdlib>
#include <string>
#include <ctime>
#include <pthread.h>
#include <algorithm>
#include <unistd.h>
#include <signal.h>
#include <streambuf>

using namespace std;
using namespace Assignm3;

const string filename = "mazedata.txt";
const string studentName = "GarciaAnthonyJohnAbril";
const string studentNum = "4321819";

// Global Variables
Maze * soln;
VectorOfPointStructType barriers;
VectorOfVectorOfPointStructType allPaths;
bool isActiveThread[MAX_NO_OF_THREADS];
int noOfActiveThreads;
int noOfPathsSubmitted;
int noOfSolutions;
int maxNoOfSolutions;
time_t startTime, endTime;

// Functions
int Initialize();
int Start();
void Output();
int Close();
void *FindAPath(void *vptr_args);
void *DisplayInfo(void * vptr_args);
void getStats(bool printToFile);
bool outOfBoundary(Point currLoc);
void submitBarrier(Point currLoc, VectorOfPointStructType pathTaken);
void submitDanger(Point currLoc, VectorOfPointStructType pathTaken);
void submitSoln(Point currLoc, VectorOfPointStructType pathTaken);
VectorOfPointStructType getShortestPathRev();


#endif /* PATHFINDER_H_ */
