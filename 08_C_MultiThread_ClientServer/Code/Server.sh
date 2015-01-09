 #!/bin/bash

clear
#gcc Server.c CountryData.c -pthread -o Server
gcc Server.c CountryData.c -o Server

./Server 
