/*
 * ForkClientThread.h
 *
 *  Created on: 28 Jan, 2014
 *      Author: JaGsTeRz
 */

#ifndef FORKCLIENTTHREAD_H_
#define FORKCLIENTTHREAD_H_

// ====================================================================

#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <resolv.h>
#include <unistd.h>
#include <signal.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

// ====================================================================

#define TLD_LEN				2
#define COUNTRY_LEN 		100
#define FIPS104_LEN 		2
#define ISO2_LEN			2
#define ISO3_LEN			3
#define CAPITAL_LEN			100
#define REGION_LEN			100
#define CURRENCY_LEN		50
#define CURRENCY_CODE_LEN	3

#define Max_Buffer_Size			1024

#define LINE_DATA_DELIMITER 	","

// ====================================================================

typedef struct CountryRecord
{
	char TLD			[TLD_LEN+1];			// Top Level Domain code
	char Country		[COUNTRY_LEN+1];
	char FIPS104		[FIPS104_LEN+1];		// Ctry code according to FIPS104 standard
	char ISO2			[ISO2_LEN+1];			// Ctry code according to ISO2    standard
	char ISO3			[ISO3_LEN+1];			// Ctry code according to ISO3    standard
	double ISONo;

	char Capital		[CAPITAL_LEN+1];
	char Region			[REGION_LEN+1];			// E.g. Asia, Europe, etc.
	char Currency		[CURRENCY_LEN+1];		// Full name of currency
	char CurrencyCode	[CURRENCY_CODE_LEN+1];	// Currency abbreviation
	double Population;

}	CountryRecordType;

// ====================================================================

int host_port = 1101;
char* host_name = "127.0.0.1";
struct sockaddr_in my_addr;
char buffer [Max_Buffer_Size], input [Max_Buffer_Size], recvdata [Max_Buffer_Size];
int bytecount, hsock, err;
int * p_int;
CountryRecordType ctryRec;

// ====================================================================

void displayInstructions();
int startMessaging();
int Connect();
void getCountry();
int storeData(char[]);
int sendData();
int recvData();
void exportData(CountryRecordType);
void displayRecordContent();
void displayMenu();
void compareCountries();

#endif /* FORKCLIENTTHREAD_H_ */
