/*
CSCI213 Assignment 3
-----------------------------------------
File name: Client.java
Author: Garcia Anthony John Abril
Registration Number: 4321819
Description: Client side program
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;


public class Client extends JFrame
{
	// variables used for storing game details
	private static int rowbycols = 5;
	private static int noofwords = 1;
	private static int usehints = 0;
	private static int reversewords = 0;
	private static int defaultnoofgames = 5; // editable, no of rounds per game
	private static String letters;
	private static int clientScore = 0;
	
	// frames for the game
	private static JFrame ConnectGUIFrame;
	private static JFrame GuessAWordGUIFrame;
	private static JFrame SetDifficultyFrame;
	
	// globally used label
	private static JLabel label;
	
	// objects in ConnectGuiFrame
	private static JTextField hostname;
	private static JTextField portno;
	private static JButton connectbutton;
	private static JTextArea connect;
	
	// objects in GuessAWordGuiFrame
	private static JTextArea score;
	private static JButton[][] buttons;
	private static boolean[][] selected;
	private static JButton confirm;
	private static JButton newgame;
	
	// objects in SetDifficutlyFrame
	private static JCheckBox level1;
	private static JCheckBox level2;
	private static JCheckBox hints;
	private static JCheckBox reverse;
	private static String hintstring;
	private static JButton startgame;
	private static JComboBox<String> puzzlesize;
	private static int noofcurrentgames = defaultnoofgames;
	
	// Streams and socket for connection and data transfer
	private static ObjectOutputStream toServer = null;
	private static ObjectInputStream fromServer = null;
	private static Socket socket;
	
	public static void main(String[] args) 
	{
		// Allows multiple copies of the program to be run on the same machine
		new Client();
	}
	
	public Client()
	{
		ConnectGUI();
	}
	
	public static void ConnectGUI()
	{
		ConnectGUIFrame = new JFrame ("Find The Word Puzzle Game");
		ConnectGUIFrame.setResizable(false);
		ConnectGUIFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// For repositioning frame in respect to screen size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		ConnectGUIFrame.setLocation(dim.width/2-ConnectGUIFrame.getSize().width/2, dim.height/2-ConnectGUIFrame.getSize().height/2);
 
		label = new JLabel("To connect to a Server, enter the following.");	
		c.fill = GridBagConstraints.HORIZONTAL;	
		c.gridx = 0;		
		c.gridy = 0;
		ConnectGUIFrame.add(label, c);
		
		label = new JLabel("Hostname: ");	
		c.fill = GridBagConstraints.CENTER;	
		c.gridx = 0;		
		c.gridy = 1;
		ConnectGUIFrame.add(label, c);
		
		label = new JLabel("Port No.: ");	
		c.fill = GridBagConstraints.CENTER;	
		c.gridx = 0;		
		c.gridy = 2;
		ConnectGUIFrame.add(label, c);
		
		hostname = new JTextField(20);
		c.fill = GridBagConstraints.CENTER;	
		c.gridx = 1;		
		c.gridy = 1;
		ConnectGUIFrame.add(hostname, c);
		
		portno = new JTextField(20);
		c.fill = GridBagConstraints.CENTER;	
		c.gridx = 1;		
		c.gridy = 2;
		ConnectGUIFrame.add(portno, c);
		
		// Connect button with listener to set up connection with the server
		connectbutton = new JButton("Connect");
		c.fill = GridBagConstraints.BOTH;	
		c.gridx = 1;		
		c.gridy = 3;
		connectbutton.addActionListener(new ConnectListener());
		ConnectGUIFrame.add(connectbutton, c);
		
		// Text area to show connection information
		connect = new JTextArea(5,5);
		connect.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridx = 0;		
		c.gridy = 4;
		connect.setOpaque(false);
		ConnectGUIFrame.add(connect, c);
		
		ConnectGUIFrame.pack();
		ConnectGUIFrame.setVisible(true);
		ConnectGUIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static class ConnectListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			try 
			{
				// Create a socket to connect to the server
				socket = new Socket(hostname.getText(),Integer.parseInt(portno.getText()));
				
				// Create an input stream to receive data from the server
			    toServer = new ObjectOutputStream(socket.getOutputStream());
			    
			    // Create an output stream to send data to the server
			    fromServer = new ObjectInputStream(socket.getInputStream());
			    
			    connect.setText("Connected.");
			    // Disables connect button to prevent multiple connection
			    connectbutton.setEnabled(false);
			    // Runs difficulty frame one connection is done
			    SetDifficulty();
			}
			catch (IOException ex) 
			{
				// Error shown when there is problem trying to connect to server
				connect.setText("Cannot get connected: " + ex.getMessage() + "\n\nPlease Try Again.");
			}
			catch (NumberFormatException nfe)
			{
				// Error shown when port number is consist of alphabets
				connect.setText("Cannot get connected: " + nfe.getMessage() + "\n\nPlease Try Again.");
			}
		}
	}
	
	public static void GuessAWordGUI()
	{
		GuessAWordGUIFrame = new JFrame("Find The Word Puzzle Game");
		GuessAWordGUIFrame.setResizable(false);
		GuessAWordGUIFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Score is shown and updated here, hints are also shown in the same text area
		score = new JTextArea(5,20);
		score.setText(getScoreText());
		score.setOpaque(false);
		score.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		GuessAWordGUIFrame.add(score,c);
		
		
		// A panel to store all buttons
		JPanel buttonPanel = new JPanel();
		// A variable grid layout to allow for different puzzle size
		buttonPanel.setLayout(new GridLayout(rowbycols,rowbycols,0,0));
		
		// button 2d array
		buttons = new JButton [rowbycols+1][rowbycols+1];
		// boolean 2d array
		selected = new boolean[rowbycols+1][rowbycols+1];
		
		int letnum = 0;
		
		for(int i=0;i<rowbycols;i++)
		{
			for(int j=0;j<rowbycols;j++)
			{
				// character shown on the button, string is received from server
				try
				{
					buttons[i][j] = new JButton(Character.toString(letters.charAt(letnum)));
				}
				catch(NullPointerException e)
				{
					connect.setText("Cannot get connected: Connection error");
					// frame is disposed and connect button is enabled to allow client to try and connect again
					GuessAWordGUIFrame.dispose();
					connectbutton.setEnabled(true);
				}
				// default button border and text color when it is not pressed or selected
				Font newButtonFont=new Font("Arial Black",Font.BOLD,12);  
				buttons[i][j].setPreferredSize(new Dimension(80,60));
				buttons[i][j].setFont(newButtonFont);
				buttons[i][j].setForeground(Color.BLACK);
				Border line = new LineBorder(Color.BLACK);
				Border lineS = new LineBorder(Color.RED);
				Border margin = new EmptyBorder(10, 30, 10, 30);
				final Border compound = new CompoundBorder(line,margin);
				final Border compoundS = new CompoundBorder(lineS, margin);
				buttons[i][j].setBorder(compound);
				buttons[i][j].setOpaque(false);
				
				// storing of variable details into the 2d array class
				final int x = i;
				final int y = j;
				
				// mouse listener to change border and text color upon selection and change the boolean status
				buttons[i][j].addMouseListener
				(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						if(selected[x][y])
						{
							buttons[x][y].setBorder(compound);
							buttons[x][y].setForeground(Color.BLACK);
						}
						else
						{
							buttons[x][y].setBorder(compoundS);
							buttons[x][y].setForeground(Color.BLUE);
						}
						
						selected[x][y] = !selected[x][y];
					}
				}
				);
				
				buttonPanel.add(buttons[i][j],c);
				letnum++;
 			}
		}
		// add panel to the frame
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		GuessAWordGUIFrame.add(buttonPanel,c);
		
		// blank text area for spacing, can be used to display other texts also
		JTextArea temp = new JTextArea(1,10);
		temp.setOpaque(false);
		temp.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		GuessAWordGUIFrame.add(temp,c);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(20,50));
		bottomPanel.setLayout(new GridLayout(1,2,0,0));
		
		// confirm button activates a listener and if no of rounds(current games) has reached 0
		// button is disabled to force user to start new game
		confirm = new JButton ("Confirm");
		if(noofcurrentgames>0)
			confirm.addMouseListener(new ConfirmListener());
		else
		{
			confirm.setEnabled(false);
			noofcurrentgames = defaultnoofgames;
		}
		bottomPanel.add(confirm);
		
		// new game button with listener
		newgame = new JButton ("New Game");
		newgame.addMouseListener(new NewGameListener());
		bottomPanel.add(newgame);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 3;
		GuessAWordGUIFrame.add(bottomPanel,c);
		
		GuessAWordGUIFrame.pack();
		GuessAWordGUIFrame.setVisible(true);
		GuessAWordGUIFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private static class ConfirmListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			try 
			{
				// upon clicking confirm, a confirm message is sent to sever
				// then client's answer, in a form of coordinates stored in a string, is sent to server
				toServer.writeObject("confirm");
				toServer.writeObject(getAnswer());
				toServer.flush();
				try 
				{
					// first response expected from server is client score or 0
					int response = (int)fromServer.readObject();
					clientScore += response;
					noofcurrentgames--;
					if(response!=0 && noofcurrentgames>=0)
					{
						displayInfo(GuessAWordGUIFrame,"Congrats! You scored: " + response,"Score!");
					}
					if(response == 0)
					{
						displayError(GuessAWordGUIFrame,"Sorry, you didn't score. Try again","Try Again");
					}
					if(noofcurrentgames<1)
					{
						displayInfo(GuessAWordGUIFrame,"Congrats! Your final score is " + clientScore,"Final Score!");
					}
					try 
					{
						// second response is the next letters for the puzzle
						letters = (String)fromServer.readObject();
						if(usehints == 1)
							// if hints is enabled a string containing first letter and word length is expected to be received
							hintstring = (String)fromServer.readObject();
					} 
					catch (ClassNotFoundException e1) 
					{
						connect.setText("Cannot get connected: Connection Error");
						// frame is disposed and connect button is enabled to allow client to try and connect again
						GuessAWordGUIFrame.dispose();
						connectbutton.setEnabled(true);
					}
					// frame is disposed and new one generated to show updated score and buttons
					GuessAWordGUIFrame.dispose();
					GuessAWordGUI();
				} 
				catch (ClassNotFoundException e1) 
				{
					connect.setText("Cannot get connected: Connection Error");
					// frame is disposed and connect button is enabled to allow client to try and connect again
					GuessAWordGUIFrame.dispose();
					connectbutton.setEnabled(true);
				}
			} 
			catch (IOException e1) 
			{
				// if server is closed, io exception is obtained
				connect.setText("Cannot get connected: Connection lost");
				// frame is disposed and connect button is enabled to allow client to try and connect again
				GuessAWordGUIFrame.dispose();
				connectbutton.setEnabled(true);
			}
		}
	}
	
	private static class NewGameListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			// upon starting new game, the current game frame is disposed and difficulty frame is shown
			GuessAWordGUIFrame.dispose();
			SetDifficultyFrame.setVisible(true);
		}
	}
	
	private static String getScoreText()
	{
		// function to get total score and hints if it is enabled
		String data = "";
		
		if(noofcurrentgames>0)
				data += "Round no.: " + (defaultnoofgames-noofcurrentgames+1) + "/" + defaultnoofgames + "\nTotal Score: " ;
		else
			data += "Game over" + "\nFinal Score: " ;
		
		data += Integer.toString(clientScore);
		
		if(usehints == 1 && noofwords == 1)
			data += "\n\nWord starts with letter " + Character.toUpperCase(hintstring.charAt(0)) + " and has " + hintstring.charAt(1) + " letters\n";
		if(usehints == 1 && noofwords == 2)
		{
			data += "\n\nFirst Word starts with letter " + Character.toUpperCase(hintstring.charAt(0)) + " and has " + hintstring.charAt(1) + " letters";
			data += "\nSecond Word starts with letter " + Character.toUpperCase(hintstring.charAt(2)) + " and has " + hintstring.charAt(3) + " letters\n";
		}
		
		return data;
	}
	
	public static void SetDifficulty()
	{
		SetDifficultyFrame = new JFrame ("Find The Word Puzzle Game");
		SetDifficultyFrame.setPreferredSize(new Dimension(400,200));
		SetDifficultyFrame.setLayout(new GridBagLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		SetDifficultyFrame.setLocation(dim.width/2-SetDifficultyFrame.getSize().width/2, dim.height/2-SetDifficultyFrame.getSize().height/2);
		GridBagConstraints c = new GridBagConstraints();
		
		// Two checkboxes grouped together for client to select level (no of words)
		level1 = new JCheckBox ("Level 1 (one word)",true);
		level2 = new JCheckBox ("Level 2 (two words)");
		// checkbox for client to select if hints should be shown
		hints = new JCheckBox ("Use hints");
		reverse = new JCheckBox("Include reverse position");
		ButtonGroup levelgroup = new ButtonGroup();
		levelgroup.add(level1);
		levelgroup.add(level2);
		level1.addActionListener(new LevelListener());
		level2.addActionListener(new LevelListener());
		hints.addActionListener(new LevelListener());
		reverse.addActionListener(new LevelListener());
		
		c.gridx = 0;
		c.gridy = 0;
		SetDifficultyFrame.add(level1,c);
		c.gridx = 1;
		c.gridy = 0;
		SetDifficultyFrame.add(level2,c);
		c.gridx = 0;
		c.gridy = 1;
		SetDifficultyFrame.add(hints,c);
		c.gridx = 1;
		c.gridy = 1;
		SetDifficultyFrame.add(reverse,c);
		
		JPanel puzzle = new JPanel();
		puzzle.setLayout(new GridLayout(1,2));
		
		label = new JLabel("Rows by Columns");
		puzzle.add(label);
		
		// combo box for client to select puzzle size
		String[] size = {"5x5", "6x6", "7x7", "8x8", "9x9", "10x10"};
		puzzlesize = new JComboBox<String> (size);
		puzzlesize.setSelectedIndex(0);
		puzzlesize.addActionListener(new LevelListener());
		puzzle.add(puzzlesize);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.CENTER;
		SetDifficultyFrame.add(puzzle,c);
		
		// start game button with a listener
		startgame = new JButton("Start Game");
		startgame.setPreferredSize(new Dimension(30,40));
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		startgame.addMouseListener(new StartListener());
		SetDifficultyFrame.add(startgame,c);
		
		SetDifficultyFrame.pack();
		SetDifficultyFrame.setVisible(true);
		SetDifficultyFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private static class LevelListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// listener checks for user game preferences and set them accordingly
			Object source = e.getSource();
			if(source == level1)
				noofwords = 1;
			if(source == level2)
				noofwords = 2;
			if(hints.isSelected())
				usehints = 1;
			else
				usehints = 0;
			if(reverse.isSelected())
				reversewords = 1;
			else
				reversewords = 0;
			rowbycols = puzzlesize.getSelectedIndex() + 5;
		}
	}
	
	private static class StartListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			try 
			{
				// after clicking start button, data are sent to server
				toServer.writeObject("startgame");
				toServer.writeObject(rowbycols);
				toServer.writeObject(noofwords);
				if(usehints == 1)
					toServer.writeObject("usehints");
				else
					toServer.writeObject("nohints");
				if(reversewords == 1)
					toServer.writeObject("true");
				else
					toServer.writeObject("false");
				toServer.flush();
				clientScore = 0;
				try 
				{
					// letters to be placed on the buttons are obtained
					letters = (String)fromServer.readObject();
					if(usehints == 1)
						hintstring = (String)fromServer.readObject();
				} 
				catch (ClassNotFoundException e2) 
				{
					connect.setText("Cannot get connected: Connection error");
					// frame is disposed and connect button is enabled to allow client to try and connect again
					SetDifficultyFrame.dispose();
					connectbutton.setEnabled(true);
				}
			} 
			catch (IOException e1) 
			{
				connect.setText("Cannot get connected: Connection lost");
				SetDifficultyFrame.dispose();
				connectbutton.setEnabled(true);
			}
			
			// after game objects are obtained from server, the main game frame is shown
			SetDifficultyFrame.setVisible(false);
			GuessAWordGUI();
		}
	}
	
	private static String getAnswer()
	{
		// this obtains the coordinates of client's answer and converts them to a single string
		String answer = "";
		for(int i=0;i<rowbycols;i++)
		{
			for(int j=0;j<rowbycols;j++)
			{
				if(selected[i][j])
					answer += Integer.toString(i) + Integer.toString(j);
			}	
		}
		return answer;
	}
	
	// message dialogs used globally
	private static void displayError(JFrame frame, String data, String title)
	{
		JOptionPane.showMessageDialog(frame,data,title, JOptionPane.WARNING_MESSAGE);
	}

	private static void displayInfo(JFrame frame, String data, String title)
	{
		JOptionPane.showMessageDialog(frame,data,title, JOptionPane.INFORMATION_MESSAGE);
	}
}


