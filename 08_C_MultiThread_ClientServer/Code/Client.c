/*
 * ForkClientThread.c
 *
 *  Created on: 28 Jan, 2014
 *      Author: JaGsTeRz
 */

#include "Client.h"

int main()
{
	displayInstructions();
	if(Connect()==0)
		printf("\n *** Thank you for using, have a nice day! ***\n\n\n");
}

void displayInstructions()
{
	printf("\n\n");
	printf("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
	printf("    Welcome to the Country Info Directory Service    \n");
	printf("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
	printf("\nUsage: ");
	printf("\n1) At the '>' prompt, type in the name of the country you wish to search");
	printf("\n2) To end program, type in 'end'\n\n");
}

int Connect()
{
	hsock = socket(AF_INET, SOCK_STREAM, 0);
	if (hsock == -1)
	{
		printf ("Client : Error initializing socket %d\n",errno);
		return 1;
	}

	p_int = (int*) malloc (sizeof(int));
	*p_int = 1;

	if ( (setsockopt (hsock, SOL_SOCKET, SO_REUSEADDR, (char*)p_int, sizeof(int)) == -1 ) ||
		 (setsockopt (hsock, SOL_SOCKET, SO_KEEPALIVE, (char*)p_int, sizeof(int)) == -1 ) )
	{
		printf ("Client : Error setting options %d\n",errno);
		free (p_int);
		return 1;
	}
	free (p_int);

	my_addr.sin_family = AF_INET ;
	my_addr.sin_port = htons (host_port);

	memset ( &(my_addr.sin_zero), 0, 8 );
	my_addr.sin_addr.s_addr = inet_addr (host_name);

	if ( connect (hsock, (struct sockaddr*) &my_addr, sizeof(my_addr)) == -1 )
	{
		if ( (err = errno) != EINPROGRESS )
		{
			fprintf (stderr, "Client : Error connecting socket %d\n", errno);
			return 1;
		}
	}

	startMessaging();

	close (hsock);

	return 0;

}   // end main ()

int startMessaging()
{

	while(1)
	{
		getCountry();

		if(strcmp(input,"end")==0)
			return 0;

		else
		{
			sendData();
			recvData();
			if(storeData(recvdata))
			{
				displayMenu();
				displayInstructions();
			}
			else
				printf("\nError - Country Not Found! ('%s')\n\n",input);
		}

	}
	return 0;
}

void getCountry()
{
	memset (input, '\0', Max_Buffer_Size);

	printf ("Please enter country > ");
	fgets (input, Max_Buffer_Size, stdin);
	input [strlen (input) - 1] = '\0';
}

int sendData()
{
	if ( (bytecount = send (hsock, input, strlen(input),0)) == -1 )
	{
		fprintf (stderr, "Client : Error sending data %d\n", errno);
		return 1;
	}
	return 0;
}

int recvData()
{
	memset (recvdata, '\0', Max_Buffer_Size);
	if ( (bytecount = recv (hsock, recvdata, Max_Buffer_Size, 0)) == -1 )
	{
		fprintf (stderr, "Client : Error receiving data %d\n", errno);
		return 1;
	}

	return 0;
}

int storeData(char recvdata[])
{
	char temp[Max_Buffer_Size];
	strcpy(temp,recvdata);

	if(strcmp(temp,"Invalid")!=0)
	{
		char* pch = strtok (temp, LINE_DATA_DELIMITER);

		// 1) Retrieve TLD
		strcpy (ctryRec.TLD, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 2) Retrieve Country
		strcpy (ctryRec.Country, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 3) Retrieve FIPS104
		strcpy (ctryRec.FIPS104, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 4) Retrieve ISO2
		strcpy (ctryRec.ISO2, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 5) Retrieve ISO3
		strcpy (ctryRec.ISO3, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 6) Retrieve ISONo
		ctryRec.ISONo = atof (pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 7) Retrieve Capital
		strcpy (ctryRec.Capital, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 8) Retrieve Region
		strcpy (ctryRec.Region, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 9) Retrieve Currency
		strcpy (ctryRec.Currency, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 10) Retrieve CurrencyCode
		strcpy (ctryRec.CurrencyCode, pch);
		pch = strtok (NULL, LINE_DATA_DELIMITER);

		// 11) Retrieve Population
		ctryRec.Population = atof (pch);

		return 1;
	}

	return 0;
}

void exportData(CountryRecordType ctryRec)
{
	char fName[Max_Buffer_Size];
	strcpy(fName,ctryRec.Country);
	strcat(fName,".txt");
	FILE * pFile = fopen (fName , "w");

	if(pFile == NULL)
	{
		printf("Error opening file!\n");
		exit(-1);
	}

	fprintf (pFile,"\n");
	fprintf (pFile,"TLD			: %s\n", ctryRec.TLD);
	fprintf (pFile,"Country		\t: %s\n", ctryRec.Country);
	fprintf (pFile,"FIPS104		\t: %s\n", ctryRec.FIPS104);
	fprintf (pFile,"ISO2			: %s\n", ctryRec.ISO2);
	fprintf (pFile,"ISO3			: %s\n", ctryRec.ISO3);
	fprintf (pFile,"ISONo			: %lf\n", ctryRec.ISONo);
	fprintf (pFile,"Capital		\t: %s\n", ctryRec.Capital);
	fprintf (pFile,"Region			: %s\n", ctryRec.Region);
	fprintf (pFile,"Currency		: %s\n", ctryRec.Currency);
	fprintf (pFile,"CurrencyCode	\t: %s\n", ctryRec.CurrencyCode);
	fprintf (pFile,"Population		: %lf\n", ctryRec.Population);
	fprintf (pFile,"\n");

	fclose(pFile);

	printf("\nFile exported to %s \n\n", fName);
}

void displayRecordContent(CountryRecordType ctryRec)
{
	printf("\n");
	printf ("TLD			: %s\n", ctryRec.TLD);
	printf ("Country		\t: %s\n", ctryRec.Country);
	printf ("FIPS104		\t: %s\n", ctryRec.FIPS104);
	printf ("ISO2			: %s\n", ctryRec.ISO2);
	printf ("ISO3			: %s\n", ctryRec.ISO3);
	printf ("ISONo			: %0.2f\n", ctryRec.ISONo);
	printf ("Capital		\t: %s\n", ctryRec.Capital);
	printf ("Region			: %s\n", ctryRec.Region);
	printf ("Currency		: %s\n", ctryRec.Currency);
	printf ("CurrencyCode	\t: %s\n", ctryRec.CurrencyCode);
	printf ("Population		: %0.2f\n", ctryRec.Population);
	printf("\n");
}

void displayMenu()
{
	while(1)
	{
		printf ("\nCurrent Country Selected - %s", ctryRec.Country);
		printf ("\n1) Export to file");
		printf ("\n2) Compare with another country");
		printf ("\n3) Display country details");
		printf ("\n4) Return to input country");
		printf("\n");

		memset (input, '\0', Max_Buffer_Size);

		printf ("Please enter choice > ");
		fgets (input, Max_Buffer_Size, stdin);
		input [strlen (input) - 1] = '\0';

		if(strcmp(input,"1")==0)
			exportData(ctryRec);
		else if(strcmp(input,"2")==0)
			compareCountries();
		else if(strcmp(input,"3")==0)
			displayRecordContent(ctryRec);
		else if(strcmp(input,"4")==0)
			return;
		else
			printf ("Invalid Input\n");
	}
}

void compareCountries()
{
	CountryRecordType ctryRecOrg;
	ctryRecOrg = ctryRec;

	getCountry();
	sendData();
	recvData();
	if(storeData(recvdata))
	{
		printf("\nCurrent country selected\n");
		displayRecordContent(ctryRecOrg);
		printf("\nSecond country selected\n");
		displayRecordContent(ctryRec);
		ctryRec = ctryRecOrg;
	}
	else
		printf("\nError - Country Not Found! ('%s')\n\n",input);
}

