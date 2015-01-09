/*
CSCI213 Assignment 3
-----------------------------------------
File name: Server.java
Author: Garcia Anthony John Abril
Registration Number: 4321819
Description: Server side program
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;

import javax.swing.*;

public class Server extends JFrame 
{
  // Text area for displaying contents
  private static JTextArea jta = new JTextArea();
  
  private static int rowbycols = 5;
  private static String answer;
  private static int score;
  private static int noofletters;
  private static int noofletters2;
  private static String[] words;
  private static int wordNum;
  private static int wordNumTwo;
  
  ServerSocket serverSocket;

  public static void main(String[] args) 
  {
    new Server();
  }

  public Server() 
  {
	    // Place text area on the frame
	    setLayout(new BorderLayout());
	    add(new JScrollPane(jta), BorderLayout.CENTER);
	
	    setTitle("Find The Word Puzzle Game Server");
	    setSize(500, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true); 
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	    
	    int clientNo = 0;
	    
	    try 
	    {
	    	  
		      // Create a server socket
		      serverSocket = new ServerSocket(4445);
		      jta.append("Started server on port " + serverSocket.getLocalPort() + '\n');
		
		      while (true) 
		      {
			    	clientNo++;
			        // Listen for a new connection request
			        Socket socket = serverSocket.accept();
			        
			        // Find the client's host name, and IP address
			        InetAddress inetAddress = socket.getInetAddress();
			
			        // Display the client number
			        jta.append("Accepting request from client " + clientNo +
			          ": /" + inetAddress.getHostAddress() + '\n');
			
			        // Create a new thread for the connection
			        HandleAClient task = new HandleAClient(socket,clientNo);
			
			        // Start the new thread
			        new Thread(task).start();
		      }
	    }
	    catch(IOException ex) 
	    {
	    	jta.append("Connection Error: "+ ex.getMessage() + "\n");
	    }
  }

  // Define the thread class for handling new connection
  class HandleAClient implements Runnable 
  {
	  // A connected socket
	  private Socket socket;
	  private int currclientNo;
	  private ObjectOutputStream toClient = null;
	  private ObjectInputStream fromClient = null;
	  
	  // Construct a thread 
	  public HandleAClient(Socket socket,int currclientNo)
	  {
		  this.socket = socket;
		  this.currclientNo = currclientNo;
	  }

	  // Run a thread 
	  public void run() 
	  {
		  // default all data
		  String data;
		  String response  = "";
		  String hints = "";
		  String usehints = "";
		  String curranswer = "";
		  boolean reversewords = false;
		  int noofwords = 1;
		  try 
		  {
			  // Create data input and output streams
	    	  toClient = new ObjectOutputStream(socket.getOutputStream());
	    	  fromClient = new ObjectInputStream(socket.getInputStream());
	        
	    	  // Continuously serve the client
	    	  while (true) 
	    	  {
	    		  try 
	    		  {
	    			  // continuously waiting for response from client
	    			  response = (String)fromClient.readObject();
	    			  
	    			  // when client sends a start game 
	    			  if(response.equals("startgame"))
	    			  {
	    				  // puzzle size is obtained from client
	    				  rowbycols = (int)fromClient.readObject();
	    				  score = 0;
	    				  
	    				  // no of words chosen by client, 1 or 2
	    				  noofwords = (int)fromClient.readObject();
	    				  
	    				  // check if client wants hints
	    				  usehints = (String)fromClient.readObject();
	    				  
	    				  // check if user wants reverse words
	    				  response = (String)fromClient.readObject();
	    				  
	    				  if(response.equals("true"))
	    					  reversewords = true;
	    				  else
	    					  reversewords = false;
	    				  
	    				  // generate one or two words with hints
	    				  if(noofwords == 1)
	    				  {
	    					  data = generateString(false,reversewords);
	    					  if(data.equals("notfound"))
	    						  throw new IOException();
	    						  
	    					  curranswer = answer;
	    					  // hints for one word is 2 chars, first letter and word length
	    				  	  hints = Character.toString(words[wordNum].charAt(0)) + noofletters;
	    				  }
	    				  else
	    				  {
	    					  data = generateString(true,reversewords);
	    					  if(data.equals("notfound"))
	    						  throw new IOException();
	    					  curranswer = answer;
	    					  // hints for two words is 4 chars, first letter and word length for each word
	    					  hints = Character.toString(words[wordNum].charAt(0)) + noofletters;
	    					  hints += Character.toString(words[wordNumTwo].charAt(0)) + noofletters2;
    					  }
    					  
	    				  // sends characters to be placed in buttons, in a form of string
	    				  toClient.writeObject(data);
	    				  // send client hints
    					  if(usehints.equals("usehints"))
    						  toClient.writeObject(hints);
    					  
	    				  jta.append("Client "+ (currclientNo) + ": New game started\n");
	    				  
	    			  }
	    			  if(response.equals("confirm"))
	    			  {
	    				  // obtains clients answer (coordinates in for of string) and is checked with server's answer
	    				  String clientans = (String)fromClient.readObject();
	    				  if(clientans.equals(curranswer))
	    				  {
	    					  // calculation of score
	    					  score = rowbycols*noofletters*noofwords;
	    					  if(usehints.equals("usehints"))
	    						  score -= noofletters*noofwords;
	    					  if(reversewords)
	    						  score += noofletters*noofwords;
	    					  toClient.writeObject(score);
	    				  }
	    				  else
	    				  {
	    					  // if client has wrong answer 0 is sent
	    					  toClient.writeObject(0);
	    				  }
	    				  if(noofwords == 1)
	    				  {
	    					  // next characters to be placed in buttons, in a form of string
	    					  data = generateString(false,reversewords);
	    					  if(data.equals("notfound"))
	    						  throw new IOException();
	    					  curranswer = answer;
	    					  // hints for one word is 2 chars, first letter and word length
	    				  	  hints = Character.toString(words[wordNum].charAt(0)) + noofletters;
	    				  }
	    				  else
	    				  {
	    					  // next characters to be placed in buttons, in a form of string
	    					  data = generateString(true,reversewords);
	    					  if(data.equals("notfound"))
	    						  throw new IOException();
	    					  curranswer = answer;
	    					  // hints for two words is 4 chars, first letter and word length for each word
	    					  hints = Character.toString(words[wordNum].charAt(0)) + noofletters;
	    					  hints += Character.toString(words[wordNumTwo].charAt(0)) + noofletters2;
    					  }
    					  
	    				  toClient.writeObject(data);
	    				  // send client hints
    					  if(usehints.equals("usehints"))
    						  toClient.writeObject(hints);
    					  
	    			  }
	    		  } 
	    		  catch (ClassNotFoundException e2) 
	    		  {
	    			  jta.append("Invalid data receieved from Client "+ (currclientNo) + "\n");
	    		  } 
	    	  }
		  }
		  catch(IOException e) 
		  {
			  // Close socket for current client
			  jta.append("Closing Socket for Client "+ (currclientNo) + "\n");
			  try 
			  {
				this.socket.close();
			  } 
			  catch (IOException e1) 
			  {
				  jta.append("Failed to close Socket for Client "+ (currclientNo) + "\n");
			  }
		  }
	  }
  	}
  
  public static String generateString(boolean twoWords, boolean reversewords)
  {
	  try
	  {
		  // words.txt is read and words are stored in an array
		  Scanner readFile = new Scanner(new File("words.txt"));
		  
		  while(readFile.hasNextLine())
		  {
			  String data = readFile.nextLine();
			  words = data.split(";");
		  }
		  readFile.close();
	  }
	  catch(FileNotFoundException fnfe)
	  {
		  // if words.txt is not found in server, this is the output
		  jta.append("words.txt not found\n");
		  return "notfound";
	  }
	  
	  // generation of a word where the word length must not be longer than horizontal or vertical width
	  wordNum = 0;
	  do
	  {
		  wordNum = (int) (Math.random()*(words.length));
		  noofletters = words[wordNum].length();
	  }while(noofletters > rowbycols);
	  
	  // number of letters of the word is taken note and orientation is also randomly generated
	  int orientation1 = (int) (Math.random() * (3));
	  int maxx, minx, maxy, miny, x, y;
	  // randomizes whether reverse would be given for the word or not
	  int randreverse = (int) (Math.random() * 2);
	  noofletters = words[wordNum].length();
	  
	  // 2d character array is used to store the word after fixing orientation and position
	  char[][] puzzle = new char [rowbycols][rowbycols];
	  for(int i=0; i<rowbycols; i++)
	  {
		  for(int j=0; j<rowbycols; j++)
		  {
			  puzzle[i][j] = ' ';
		  }
	  }
	  
	  switch(orientation1)
	  {
	  		// horizontal
	  		case 0:  // formula for coordinates for horizontal orientation to prevent word from being concatenated
	  				 maxx = rowbycols - noofletters + 1;
	  				 maxy = rowbycols;
	  				 minx = 0;
	  				 miny = 0;
	  				 x = (int)(minx + Math.random() * (maxx-minx));
	  				 y = (int)(miny + Math.random() * (maxy-miny));
	  				 // if two words are needed another word is generated in this function
	  				if(twoWords)
	  					secondWord(words,wordNum,puzzle,orientation1,x,y,reversewords);
	  				// word is stored in the character array
	  				 for(int i=0;i<noofletters;i++)
	  				 {
	  					 // if user wants reverse word and if randomized reverse word is given then word is reversed
	  					 if(reversewords && randreverse == 1)
	  						 puzzle[x][y] = words[wordNum].charAt(noofletters - i - 1);
	  					 else
	  						puzzle[x][y] = words[wordNum].charAt(i);
	  					 x++;
	  				 }
	  				 break;
	  		// vertical
	  		case 1: maxx = rowbycols;
	  				maxy = rowbycols - noofletters + 1;
	  				minx = 0;
	  				miny = 0;
	  				x = (int)(minx + Math.random() * (maxx-minx));
	  				y = (int)(miny + Math.random() * (maxy-miny));
	  				if(twoWords)
	  					secondWord(words,wordNum,puzzle,orientation1,x,y,reversewords);
	  				for(int i=0;i<noofletters;i++)
	  				{
	  					if(reversewords && randreverse == 1)
	  						 puzzle[x][y] = words[wordNum].charAt(noofletters - i - 1);
	  					 else
	  						puzzle[x][y] = words[wordNum].charAt(i);
	  					 y++;
	  				}
	  				break;
	  		// diagonal going up
	  		case 2: maxx = rowbycols - noofletters + 1;
	  				maxy = rowbycols;
	  				minx = 0;
	  				miny = noofletters - 1;
	  				x = (int)(minx + Math.random() * (maxx-minx));
	  				y = (int)(miny + Math.random() * (maxy-miny));
	  				if(twoWords)
	  					secondWord(words,wordNum,puzzle,orientation1,x,y,reversewords);
	  				for(int i=0;i<noofletters;i++)
	  				 {
	  					if(reversewords && randreverse == 1)
	  						puzzle[x][y] = words[wordNum].charAt(i);
	  					else
	  						puzzle[x][y] = words[wordNum].charAt(noofletters - i - 1);
	  					 x++;
	  					 y--;
	  				 }
	  				break;
	  		//diagonal going down
	  		default:maxx = rowbycols - noofletters + 1;
	  				maxy = rowbycols - noofletters + 1;
	  				minx = 0;
	  				miny = 0;
	  				x = (int)(minx + Math.random() * (maxx-minx));
	  				y = (int)(miny + Math.random() * (maxy-miny));
	  				if(twoWords)
	  					secondWord(words,wordNum,puzzle,orientation1,x,y,reversewords);
	  				for(int i=0;i<noofletters;i++)
	  				{
	  					if(reversewords && randreverse == 1)
	  						puzzle[x][y] = words[wordNum].charAt(noofletters - i - 1);
	  					else
	  						puzzle[x][y] = words[wordNum].charAt(i);
	  					 x++;
	  					 y++;
	  				}
	  				break;
	  				
	  }
	  
	  String dataToSend = ArraytoString(puzzle);
	  return dataToSend;
  }
  
  private static char getALetter()
  {
	  // generates a random letter
	  String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	  int num = (int)(Math.random() * 26);
	  return alphabet.charAt(num);
  }
  
  private static String ArraytoString(char puzzle[][])
  {
	  // data stores a string to be sent to client, string is used for the letters placed on the buttons
	  String data = "";
	  answer = "";
	  for(int i=0; i<rowbycols; i++)
	  {
		  for(int j=0; j<rowbycols; j++)
		  {
			  // if there is no letter in array, a randomly generated letter is obtained
			  if(puzzle[i][j] == ' ')
				  data += getALetter();
			  else
			  {
				  // if there is a letter, it is stored and the coordinates are added to the answer
				  // which will be used for checking clients answer
				  data += Character.toUpperCase(puzzle[i][j]);
				  answer += Integer.toString(i) + Integer.toString(j);
			  }
		  }
	  }
	  
	  return data;
  }
  
  private static void secondWord(String[] words,int wordNum,char[][]puzzle,int orientation1,int x1,int y1,boolean reversewords)
  {
	  // boolean variable to ensure that there is a loop which repeats until a second word is generated with non-overlapping coordinates
	  boolean notvalid = true;
	  while(notvalid)
	  {
		  // randomized orientation
		  int orientation2 = (int) (Math.random() * (3));
		  int x2,y2,maxx,maxy,minx,miny,nooftries = 0;
		  int randreverse = (int) (Math.random() * 2);
		  
		  wordNumTwo = 0;
		  do
		  {
			  // generation of random number to obtain second word from words array
			  wordNumTwo = (int) (Math.random()*(words.length));
			  noofletters2 = words[wordNumTwo].length();
		  }while(noofletters2 > rowbycols || wordNumTwo == wordNum);
		  // number of letters of second word
		  noofletters2 = words[wordNumTwo].length();

		  // horizontal
		  if(orientation2 == 0)
		  {
			  // formula for coordinates for horizontal orientation to prevent word from being concatenated
			  maxx = rowbycols - noofletters2 + 1;
	  		  maxy = rowbycols;
	  		  minx = 0;
	  		  miny = 0;
	  		  do
	  		  {
	  			  // coordinates generated and a break is added if no valid coordinates are found within 10 tries
	  			  // to prevent a forever loop, also overlap function is called to check if first and second word
	  			  // coordinates overlap
	  			  x2 = (int)(minx + Math.random() * (maxx-minx));
	  			  y2 = (int)(miny + Math.random() * (maxy-miny));
	  			  nooftries++;
	  			  if(nooftries>10)
	  				  break;
	  		  }while(overlap(x1,y1,x2,y2,orientation1,orientation2));
	  		  if(nooftries<10)
			  {
	  			  // letters of the word is placed in the puzzle character 2d array
	  			  for(int i=0;i<noofletters2;i++)
	  			  {
	  				  	// if user wants reverse word and if randomized reverse word is given then word is reversed
		  				if(reversewords && randreverse == 1)
	  						puzzle[x2][y2] = words[wordNumTwo].charAt(noofletters2 - i - 1);
	  					else
	  						puzzle[x2][y2] = words[wordNumTwo].charAt(i);
	  			  		x2++;
	  			  }
	  			  notvalid = false;
			  }
			}
			  
			// vertical
			else if(orientation2 == 1)
			{
				// formula for coordinates for vertical orientation to prevent word from being concatenated
				maxx = rowbycols;
		  		maxy = rowbycols - noofletters2 + 1;
		  		minx = 0;
		  		miny = 0;
		  		do
		  		{
		  			x2 = (int)(minx + Math.random() * (maxx-minx));
		  			y2 = (int)(miny + Math.random() * (maxy-miny));
		  			nooftries++;
		  			if(nooftries>10)
			  			  break;
		  		}while(overlap(x1,y1,x2,y2,orientation1,orientation2));
				if(nooftries<10)
				{
					 for(int i=0;i<noofletters2;i++)
		  			 {
						 if(reversewords && randreverse == 1)
		  						puzzle[x2][y2] = words[wordNumTwo].charAt(noofletters2 - i - 1);
		  				else
		  						puzzle[x2][y2] = words[wordNumTwo].charAt(i);
						 y2++;
		  			 }
					 notvalid = false;
				 }
			}
					 	
			// diagonal going up
			else if(orientation2 == 2)
			{
				// formula for coordinates for diagonal orientation to prevent word from being concatenated
				maxx = rowbycols - noofletters2 + 1;
		  		maxy = rowbycols;
		  		minx = 0;
		  		miny = noofletters2 - 1;
		  		do
				{
		  			x2 = (int)(minx + Math.random() * (maxx-minx));
		  			y2 = (int)(miny + Math.random() * (maxy-miny));
		  			nooftries++;
		  			if(nooftries>10)
			  			 break;
				}while(overlap(x1,y1,x2,y2,orientation1,orientation2));
		  		if(nooftries<10)
				{
					 for(int i=0;i<noofletters2;i++)
		  			 {
						 if(reversewords && randreverse == 1)
							 puzzle[x2][y2] = words[wordNumTwo].charAt(i);
						 else
							 puzzle[x2][y2] = words[wordNumTwo].charAt(noofletters2 - i - 1);
						 x2++;
						 y2--;
		  			 }
					 notvalid = false;
				 }
			}
			// diagonal going down
			else 
			{
				// formula for coordinates for diagonal orientation to prevent word from being concatenated
				maxx = rowbycols - noofletters2 + 1;
				maxy = rowbycols - noofletters2 + 1;
				minx = 0;
				miny = 0;
				do
				{
					x2 = (int)(minx + Math.random() * (maxx-minx));
					y2 = (int)(miny + Math.random() * (maxy-miny));
					nooftries++;
					if(nooftries>10)
						break;
				}while(overlap(x1,y1,x2,y2,orientation1,orientation2));
				if(nooftries<10)
				{
					for(int i=0;i<noofletters2;i++)
					{
						if(reversewords && randreverse == 1)
	  						puzzle[x2][y2] = words[wordNumTwo].charAt(noofletters2 - i - 1);
	  					else
	  						puzzle[x2][y2] = words[wordNumTwo].charAt(i);
						x2++;
						y2++;
		  			}
					notvalid = false;
				}
				  				 
			}
	  }

  }
  
  private static boolean overlap(int x1, int y1, int x2, int y2,int orientation1, int orientation2)
  {
	  boolean overlapping = false;
	  
	  // string arrays for storing coordinates
	  String[] array1 = new String [noofletters];
	  String[] array2 = new String [noofletters2];
	  
	  switch(orientation1)
	  {
	  	  // horizontal
		  case 0:	for(int i=0;i<noofletters;i++)
					{
						 array1[i] = Integer.toString(x1) + Integer.toString(y1); 
						 x1++;
					}
			  		break;
		  // vertical
		  case 1:	for(int i=0;i<noofletters;i++)
					{
						 array1[i] = Integer.toString(x1) + Integer.toString(y1); 
						 y1++;
					}
			  		break;
		  // diagonal going up
		  case 2:	for(int i=0;i<noofletters;i++)
					{
						 array1[i] = Integer.toString(x1) + Integer.toString(y1); 
						 x1++;
						 y1--;
					}
			  		break;
		  //diagonal going down
		  default:	for(int i=0;i<noofletters;i++)
					{
						 array1[i] = Integer.toString(x1) + Integer.toString(y1); 
						 x1++;
						 y1++;
					}
			  		break;
	  }
	  
	  switch(orientation2)
	  {
	  	  // horizontal
		  case 0:	for(int i=0;i<noofletters2;i++)
					{
						 array2[i] = Integer.toString(x2) + Integer.toString(y2); 
						 x2++;
					}
			  		break;
		  // vertical
		  case 1:	for(int i=0;i<noofletters2;i++)
					{
						 array2[i] = Integer.toString(x2) + Integer.toString(y2); 
						 y2++;
					}
			  		break;
		  // diagonal going up
		  case 2:	for(int i=0;i<noofletters2;i++)
					{
						 array2[i] = Integer.toString(x2) + Integer.toString(y2); 
						 x2++;
						 y2--;
					}
			  		break;
		  //diagonal going down
		  default:	for(int i=0;i<noofletters2;i++)
					{
						 array2[i] = Integer.toString(x2) + Integer.toString(y2); 
						 x2++;
						 y2++;
					}
			  		break;
	  }
	  
	  // checks all coordinates to look for overlapping ones
	  for(int i=0;i<noofletters;i++)
	  {
		  for(int j=0;j<noofletters2;j++)
		  {
			  if(array1[i].equals(array2[j]))
			  {
				  if(Character.toString(words[wordNum].charAt(i)).equals(Character.toString(words[wordNumTwo].charAt(j))))
					  overlapping = false;
				  else
					  overlapping = true;
			  }
		  }
	  }
	  
	  return overlapping;
  }
  
}

