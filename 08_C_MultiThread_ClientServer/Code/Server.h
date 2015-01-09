/*
 * ForkServerThread.h
 *
 *  Created on: 28 Jan, 2014
 *      Author: JaGsTeRz
 */

#ifndef FORKSERVERTHREAD_H_
#define FORKSERVERTHREAD_H_

#include "CountryData.h"

time_t rawtime;
struct tm *info;

int Connect();
void* SocketHandler(void*,int);
void ProcessReceivedData(char[]);


#endif /* FORKSERVERTHREAD_H_ */
