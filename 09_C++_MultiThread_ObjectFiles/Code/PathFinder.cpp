#include "PathFinder.h"

int main()
{
	time(&startTime);

	if(Initialize() == EXIT_FAILURE)
		return EXIT_FAILURE;

	if(Start() == EXIT_FAILURE)
		return EXIT_FAILURE;

	Output();

	if(Close() == EXIT_FAILURE)
		return EXIT_FAILURE;

	return EXIT_SUCCESS;
}

int Initialize()
{
	string temp;
	do
	{
		cout << "\n\nEnter Number of Solutions to be submitted (Leave blank for default of 1): ";
		getline(cin,temp);

		if(temp == "")
			maxNoOfSolutions = 1;
		else
			maxNoOfSolutions = atoi(temp.c_str());

	} while(maxNoOfSolutions < 1);

	ifstream infile(filename.c_str());
	if(!infile)
	{
		cout << "\nFile not found\n" << endl;
		return EXIT_FAILURE;
	}
	else if(infile.peek() == ifstream::traits_type::eof())
	{
		cout << "\nFile empty\n" << endl;
		return EXIT_FAILURE;
	}

	AllocateProgramsVariableMemory();
	mazeObj->LoadMaze(filename);
	soln = new Assignm3::Maze(mazeObj->getLength(),mazeObj->getBreadth(),mazeObj->getStartLocation(),mazeObj->getEndLocation());
	discoveredASolutionPath = false;
	globalPathFinderResource.usedThreadNameIndex = 0;
	globalPathFinderResource.noOfDeadEndPathsFound = 0;
	globalPathFinderResource.noOfBarriersDiscovered = 0;
	globalPathFinderResource.noOfDangerAreaDiscovered = 0;
	noOfPathsSubmitted = 0;
	noOfActiveThreads = 0;
	noOfSolutions = 0;

	return EXIT_SUCCESS;
}

int Close()
{
	for(int i=0; i<MAX_NO_OF_THREADS; i++)
	{
		if (pthread_join(globalPathFinderResource.activeThreadArray[i], NULL) != 0)
			return EXIT_FAILURE;
	}

	DeallocateProgramsVariableMemory();

	return EXIT_SUCCESS;
}

int Start()
{
	while(discoveredASolutionPath == false)
	{
		if(isActiveThread[2] == false && noOfActiveThreads > 0)
		{
			if (pthread_create(&globalPathFinderResource.activeThreadArray[2],NULL,DisplayInfo,NULL)!= 0)
				return EXIT_FAILURE;
			else
				isActiveThread[2] = true;
		}
		if(noOfActiveThreads<MAX_NO_OF_THREADS-1)
		{
			for(int i=0; i<MAX_NO_OF_THREADS-1; i++)
			{
				if(isActiveThread[i] == false)
				{
					PathFinderParameterInfo thread_info;
					thread_info.threadName = THREAD_NAMES[globalPathFinderResource.usedThreadNameIndex % 63];
					thread_info.threadIDArrayIndex = i;
					globalPathFinderResource.activeThreadParamArray[i] = &thread_info;

					if (pthread_create(&globalPathFinderResource.activeThreadArray[i],NULL,FindAPath,(void*)&thread_info)!= 0)
						return EXIT_FAILURE;
					else
					{
						isActiveThread[i] = true;
						globalPathFinderResource.usedThreadNameIndex++;
						noOfActiveThreads++;
						pthread_mutex_lock(&thread_mutex);
						cout << "Thread '" << thread_info.threadName << "' has been created !!" << endl;
						pthread_mutex_unlock(&thread_mutex);
					}
				}
			}
		}
	}

	return EXIT_SUCCESS;
}

void Output()
{
	// Let all threads finish
	usleep(200000);

	pthread_mutex_lock(&thread_mutex);
	cout << "Finished Finding a SAFE PATH !!" << endl;
	cout << "Printing submitted maze solution ..." << endl;
	submitMazeSolnObj->printSubmittedSolution(studentName,studentNum);
	submitMazeSolnObj->saveSubmittedSolution(studentName,studentNum);
	getStats(false);
	getStats(true);
	pthread_mutex_unlock(&thread_mutex);
}

void pressEnter()
{
	cout << "\nPress Enter to Continue\n";
	cin.clear();
	cin.ignore(100,'\n');
}

void *DisplayInfo(void * vptr_args)
{
	pthread_mutex_lock(&thread_mutex);
	// 1 sec = 1000000 usec
	usleep(300000);
	cout << "============================================================" << endl;
	time(&endTime);
	cout << "Elapsed Time: " << difftime(endTime,startTime) << endl;
	cout << "Latest Update..." << endl;
	cout << "============================================================" << endl;
	cout << endl
		 << "Dead End Paths Found   : " << globalPathFinderResource.noOfDeadEndPathsFound << endl
		 << "Barriers Discovered    : " << globalPathFinderResource.noOfBarriersDiscovered << endl
	 	 << "Danger Area Discovered : " << globalPathFinderResource.noOfDangerAreaDiscovered << endl
	 	 << endl;

	isActiveThread[2] = false;
	pthread_mutex_unlock(&thread_mutex);

	return EXIT_SUCCESS;
}

void getStats(bool printToFile)
{
	std::ofstream ofile;
	std::streambuf * buf;

	if(printToFile)
	{
		string fName = studentName + "_" + studentNum + ".txt";
		ofile.open(fName.c_str(), ios::app);
		buf = ofile.rdbuf();
	}
	else
		buf = std::cout.rdbuf();

	std::ostream os(buf);

	os	<< "Discoveries" << endl
		<< "Dead End Paths Found   : " << globalPathFinderResource.noOfDeadEndPathsFound << endl
		<< "Barriers Discovered    : " << globalPathFinderResource.noOfBarriersDiscovered << endl
		<< "Danger Area Discovered : " << globalPathFinderResource.noOfDangerAreaDiscovered << endl
		<< "No Of Threads Started  : " << globalPathFinderResource.usedThreadNameIndex << endl
		<< "No Of Paths Submitted  : " << noOfPathsSubmitted << endl
		<< "No Of Solns Submitted  : " << noOfSolutions << endl << endl;

	VectorOfPointStructType finalPath = getShortestPathRev();

	os  << "Shortest Path Found    : " << endl;
		finalPath.pop_back();
		mazeObj->ShowPathGraphically(finalPath,os);
}

void *FindAPath(void *vptr_args)
{
	PathFinderParameterInfo thread_info = *((PathFinderParameterInfo*)vptr_args);

	// Location of current position, starting position, ending position
	Point startPos = mazeObj->getStartLocation();
	Point endPos = mazeObj->getEndLocation();
	thread_info.currentLocation = startPos;

	VectorOfPointStructType pathTaken;
	VectorOfPointStructType pathChecked;
	pathTaken.push_back(thread_info.currentLocation);


	do
	{
		if(discoveredASolutionPath)
		{
			isActiveThread[thread_info.threadIDArrayIndex] = false;
			noOfActiveThreads--;
			pthread_exit (NULL);
		}

		Point up(thread_info.currentLocation.x,thread_info.currentLocation.y+1);
		Point down(thread_info.currentLocation.x,thread_info.currentLocation.y-1);
		Point left(thread_info.currentLocation.x-1,thread_info.currentLocation.y);
		Point right(thread_info.currentLocation.x+1,thread_info.currentLocation.y);

		srand(time(NULL));
		string randNum = "0123";
		for(int i=0;i<4;i++)
		{
			int x = (std::rand () % randNum.size());
			char y = randNum[x];
			randNum.erase(std::remove(randNum.begin(), randNum.end(), y), randNum.end());

			int k;
			if(thread_info.threadIDArrayIndex == 0)
				k = i;
			else
				k = y - '0';

			switch(k)
			{
				case 0:pathChecked.push_back(up);break;
				case 1:pathChecked.push_back(down);break;
				case 2:pathChecked.push_back(left);break;
				case 3:pathChecked.push_back(right);break;
			}

		}

		int countEnd = 0;
		if(mazeObj->IsThereBarrier(up) || soln->IsThereDanger(up))
			countEnd++;
		if(mazeObj->IsThereBarrier(down) || soln->IsThereDanger(down))
			countEnd++;
		if(mazeObj->IsThereBarrier(left) || soln->IsThereDanger(left))
			countEnd++;
		if(mazeObj->IsThereBarrier(right) || soln->IsThereDanger(right))
			countEnd++;
		if(countEnd == 3)
		{
			pthread_mutex_lock(&thread_mutex);
			cout << "Thread '" << thread_info.threadName << "' hits a dead end near ";
			thread_info.currentLocation.display(cout);
			cout << " !!" << endl;
			globalPathFinderResource.noOfDeadEndPathsFound++;
			pthread_mutex_unlock(&thread_mutex);
		}

		int back = pathChecked.size()-1;
		thread_info.currentLocation = pathChecked[back];
		pathChecked.pop_back();

		int countPop = 0;
		while(mazeObj->IsThereBarrier(thread_info.currentLocation) || pathObj->isLocationInPath(thread_info.currentLocation,pathTaken)
			  || soln->IsThereDanger(thread_info.currentLocation) || outOfBoundary(thread_info.currentLocation))
		{
			if(pathChecked.size() < 1)
			{
				isActiveThread[thread_info.threadIDArrayIndex] = false;
				noOfActiveThreads--;
				pthread_exit (NULL);
			}
			if(mazeObj->IsThereBarrier(thread_info.currentLocation))
			{
				pthread_mutex_lock(&thread_mutex);
				submitBarrier(thread_info.currentLocation,pathTaken);
				pthread_mutex_unlock(&thread_mutex);
			}

			thread_info.currentLocation = pathChecked.back();
			pathChecked.pop_back();
			countPop++;
		}

		if(mazeObj->IsThereDanger(thread_info.currentLocation))
		{
			pthread_mutex_lock(&thread_mutex);
			cout << "Thread '" << thread_info.threadName << "' stepped into DANGER at ";
			thread_info.currentLocation.display(cout);
			cout << " !!" << endl;
			submitDanger(thread_info.currentLocation,pathTaken);
			isActiveThread[thread_info.threadIDArrayIndex] = false;
			noOfActiveThreads--;
			cout << "Thread '" << thread_info.threadName << "' is dead! It's sacrifice shall not be in vain!" << endl;
			pthread_mutex_unlock(&thread_mutex);
			pthread_exit (NULL);
		}

		if(countPop > 3)
		{
			Point temp = pathTaken.back();
			while(!temp.isConnected(thread_info.currentLocation))
			{
				pathTaken.pop_back();
				temp = pathTaken.back();
			}
			pathTaken.push_back(thread_info.currentLocation);
		}

		if(countPop < 4)
			pathTaken.push_back(thread_info.currentLocation);

		if(thread_info.currentLocation == endPos)
		{
			if(!discoveredASolutionPath)
			{
				cout << "Thread '" << thread_info.threadName << "' just found a solution! Well done!!" << endl;
				pthread_mutex_lock(&thread_mutex);
				submitSoln(thread_info.currentLocation,pathTaken);
				pthread_mutex_unlock(&thread_mutex);

				isActiveThread[thread_info.threadIDArrayIndex] = false;
				noOfActiveThreads--;
				pthread_exit (NULL);
			}
		}

	}while(!discoveredASolutionPath);

	return EXIT_SUCCESS;
}

bool outOfBoundary(Point currLoc)
{
	if((currLoc.x < 0) || (currLoc.x >= mazeObj->getLength()))
		return true;
	else if((currLoc.y < 0) || (currLoc.y >= mazeObj->getBreadth()))
		return true;
	return false;
}

void submitBarrier(Point currLoc, VectorOfPointStructType pathTaken)
{
	// update global
	if(find(barriers.begin(),barriers.end(),currLoc) == barriers.end())
	{
		barriers.push_back(currLoc);
		globalPathFinderResource.noOfBarriersDiscovered++;
	}
	noOfPathsSubmitted++;

	// submit path to MazeSoln
	pathTaken.push_back(currLoc);
	submitMazeSolnObj->submitPathToBarrier(pthread_self(),pathTaken);

	// update soln
	soln->updateMaze(currLoc,BARRIER_INT);
}

void submitDanger(Point currLoc, VectorOfPointStructType pathTaken)
{
	// update global
	globalPathFinderResource.noOfDangerAreaDiscovered++;
	globalPathFinderResource.discoveredDangerAreas.push_back(currLoc);
	noOfPathsSubmitted++;

	// submit path to MazeSoln
	pathTaken.push_back(currLoc);
	submitMazeSolnObj->submitPathToDangerArea(pthread_self(),pathTaken);

	// update soln
	soln->updateMaze(currLoc,DANGER_INT);
}

void submitSoln(Point currLoc, VectorOfPointStructType pathTaken)
{
	// update global
	globalPathFinderResource.solutionPath = pathTaken;
	noOfSolutions++;
	noOfPathsSubmitted++;
	allPaths.push_back(pathTaken);

	// submit path to MazeSoln
	submitMazeSolnObj->submitSolutionPath(pthread_self(),pathTaken);

	// update soln
	soln->AddNewPath(pathTaken);

	if(noOfSolutions>=maxNoOfSolutions)
		discoveredASolutionPath = true;
}

VectorOfPointStructType getShortestPathRev()
{
	int min = 0;
	for(int i=1; i<allPaths.size(); i++)
	{
		if(allPaths[i].size() < allPaths[min].size())
			min = i;
	}
	return allPaths[min];
}
