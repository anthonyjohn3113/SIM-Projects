/*
 * ForkServerThread.c
 *
 *  Created on: 28 Jan, 2014
 *      Author: JaGsTeRz
 */


#include "Server.h"

// -------------------------------------------------------------------------------------------

int main()
{
	readData();
	Connect();
}

// -------------------------------------------------------------------------------------------

int Connect()
{
	time( &rawtime );
	info = localtime(&rawtime);
	int host_port= 1101;

	struct sockaddr_in my_addr;

	int hsock;
	int * p_int ;
	int err;

	socklen_t addr_size = 0;
	int* csock;
	struct sockaddr_in sadr;

	int childpid;

	signal (SIGCHLD, SIG_IGN);

	hsock = socket (AF_INET, SOCK_STREAM, 0);
	if (hsock == -1)
	{
		printf ("%sServer : Error initializing socket %d\n",asctime(info), errno);
		return 0;
	}

	// free the socket
	p_int = (int*) malloc (sizeof(int));
	*p_int = 1;

	if ( (setsockopt(hsock, SOL_SOCKET, SO_REUSEADDR, (char*)p_int, sizeof(int)) == -1 ) ||
		 (setsockopt(hsock, SOL_SOCKET, SO_KEEPALIVE, (char*)p_int, sizeof(int)) == -1 ) )
	{
		printf ("%sServer : Error setting options %d\n",asctime(info), errno);
		free (p_int);
		return 0;
	}
	free (p_int);


	my_addr.sin_family = AF_INET;
	my_addr.sin_port = htons (host_port);

	memset (&(my_addr.sin_zero), 0, 8);
	my_addr.sin_addr.s_addr = INADDR_ANY ;

	if ( bind (hsock, (struct sockaddr*)&my_addr, sizeof(my_addr)) == -1 )
	{
		fprintf (stderr,"%sServer : Error binding to socket, some other program is listening on this port! %d\n",asctime(info),errno);
		return 0;
	}

	if ( listen (hsock, 10) == -1 )
	{
		fprintf(stderr, "%sServer : Error listening %d\n",asctime(info),errno);
		return 0;
	}

	//Now lets do the server stuff

	addr_size = sizeof(struct sockaddr_in);

	printf ("\n\nServer has started with PID %d\n",getpid());
	printf ("Press 'control + c' to terminate\n\n");
	while (1)
	{
		time(&rawtime);
		info = localtime(&rawtime);

		printf ("%sServer : Waiting for a connection\n",asctime(info));
		csock = (int*) malloc (sizeof(int));

		if ( (*csock = accept (hsock, (struct sockaddr*)&sadr, &addr_size)) != -1 )
		{
			printf("---------------------\n");
			printf("%sServer : Received connection from %s\n",asctime(info),inet_ntoa(sadr.sin_addr));

			switch ( childpid=fork() )
			{
				case -1:    //error
					fprintf (stderr, "%sServer : Error forking the child %d\n",asctime(info),errno);
					exit (0);
					break;
				case 0:     // in the child ...
					printf ("%sServer : Forked child PID %d \n",asctime(info), getpid());
					SocketHandler (csock, getpid());
					printf("---------------------\n");
					exit (0);
					break;
				default:    // in the server ...
					close (*csock);
					free (csock);
					break;

			}   // end switch ...
		}
		else
		{
			fprintf (stderr, "%sServer : Error accepting %d\n",asctime(info), errno);
		}

	}   // end infinite while loop ...

	return 0;

}   // end main () ...

// -------------------------------------------------------------------------------------------

void* SocketHandler (void* lp, int childpid)
{
	while(1)
	{

		printf("---------------------\n");
		int *csock = (int*) lp;

		char buffer [Max_Buffer_Size];
		int buffer_len = Max_Buffer_Size;
		int bytecount;

		memset (buffer, 0, buffer_len);
		if ( (bytecount = recv(*csock, buffer, buffer_len, 0)) == -1 )
		{
			fprintf (stderr, "%sServer : Error receiving data %d from Client %d\n",asctime(info), errno, childpid);
			free (csock);
			return 0;
		}

		if(bytecount==0)
		{
			printf ("%sServer : Client %d has terminated\n",asctime(info),childpid);
			return 0;
		}
		printf ("%sServer : Received bytes %d from Client %d\n",asctime(info), bytecount,childpid);
		printf ("%sServer : Received message \"%s\" from Client %d\n",asctime(info), buffer,childpid);

		if(strcmp(buffer,"end")==0)
			return 0;

		if(strcmp(buffer,"export")!=0)
		{
			ProcessReceivedData(buffer);

			if ( (bytecount = send(*csock, buffer, strlen(buffer), 0))== -1)
			{
				fprintf (stderr, "%sServer : Error sending data %d\n",asctime(info), errno);
				free (csock);
				return 0;
			}

			printf ("%sServer : Sent bytes %d to Client %d\n",asctime(info), bytecount,childpid);
			printf ("%sServer : Sent message \"%s\" to Client %d\n",asctime(info), buffer,childpid);
			printf("---------------------\n");
		}

	}
	return 0;
}   // end SocketHandler () ...

void ProcessReceivedData(char buffer[])
{
	char temp[Max_Buffer_Size];
	getCountryDataString(temp,buffer);
	strcpy(buffer,temp);
	buffer [strlen (buffer)] = '\0';
}

