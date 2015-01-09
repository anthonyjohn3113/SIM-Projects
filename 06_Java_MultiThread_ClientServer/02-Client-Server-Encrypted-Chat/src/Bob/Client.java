// Done By: Garcia Anthony John Abril
// Student ID: 4321819

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client
{
	private static String firstMessage = "HELLO";
	private JFrame LogInFrame;
	private JFrame ChatFrame;
	private JTextField portno;
	private JTextField host;
	private JTextField username;
	private JTextField password;
	private JTextArea loginInfo;
	private JTextArea mainChatBox;
	private JTextArea chatArea;
	private JLabel label;
	private JButton loginbutton;
	private DatagramSocket clientSocket;
	private byte[] receiveData;
	private byte[] sendData;
	private String sentence;
	private InetAddress IPAddress;
	private int port;
	private RC4 rc4;
	private String serverName, KA, KB, K;

	public static void main(String args[])
	{
		new Client();
	}

	public Client()
	{
		KB = generateKey(64);
		LogIn();
	}

	private void LogIn()
	{
		LogInFrame = new JFrame("Log In");
		LogInFrame.setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0 };
		LogInFrame.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints c = new GridBagConstraints();

		// For repositioning frame in respect to screen size
		// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// LogInFrame.setLocation(dim.width/2-LogInFrame.getSize().width/2,
		// dim.height/2-LogInFrame.getSize().height/2);

		label = new JLabel("To start the Client, enter the following.");

		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		LogInFrame.add(label, c);

		label = new JLabel("Username: ");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		LogInFrame.add(label, c);

		label = new JLabel("Password: ");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 2;
		LogInFrame.add(label, c);

		label = new JLabel("Host: ");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 3;
		LogInFrame.add(label, c);

		label = new JLabel("Port No.: ");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 4;
		LogInFrame.add(label, c);

		username = new JTextField(10);
		username.setText("Bob");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 1;
		LogInFrame.add(username, c);

		password = new JPasswordField(10);
		password.setText("123456");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 2;
		LogInFrame.add(password, c);

		host = new JTextField(10);
		host.setText("127.0.0.1");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 3;
		LogInFrame.add(host, c);

		portno = new JTextField(10);
		portno.setText("4444");
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 4;
		LogInFrame.add(portno, c);

		// Connect button with listener to set up connection with the server
		loginbutton = new JButton("Log In");
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 5;
		loginbutton.addActionListener(new LogInListener());
		LogInFrame.add(loginbutton, c);

		// Text area to show connection information
		loginInfo = new JTextArea(2, 5);
		loginInfo.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 6;
		loginInfo.setOpaque(false);
		LogInFrame.add(loginInfo, c);

		LogInFrame.pack();
		LogInFrame.setVisible(true);
		LogInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class LogInListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				if (password.getText().isEmpty() || password.getText().length() != 6)
				{
					loginInfo.setText("Error: Password is not 6 digits");
				}
				else
				{
					try
					{
						Integer.parseInt(password.getText());
						rc4 = new RC4(password.getText());
						port = Integer.parseInt(portno.getText());
						Chat();
						new Thread(new Listen()).start();
					}
					catch (NumberFormatException nfe)
					{
						loginInfo.setText("Error: Password is not 6 digits");
					}
				}
			}
			catch (Exception e1)
			{
				loginInfo.setText("Error: " + e1.getMessage());
			}
		}
	}

	private DatagramSocket Connect()
	{
		try
		{
			clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(host.getText());

			receiveData = new byte[65527];
			sendData = new byte[65527];

			append("Client is connecting to " + host.getText() + " with port " + port + "\n");
			append("Sending server initial RC4 ecnrypted message to establish connection.." + "\n");

			sentence = rc4.encrypt(firstMessage.toCharArray());
			sendMessage(sentence);

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			append("Server has replied!" + "\n");

			sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");

			if (rc4.decrypt(sentence.toCharArray()).equals(firstMessage))
			{
				append("Password matches server password!" + "\n");
				sentence = rc4.encrypt(username.getText().toCharArray());
				sendMessage(sentence);

				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);

				sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				serverName = rc4.decrypt(sentence.toCharArray());
				append("Connected to " + serverName + "!\n");
				append("Exchanging keys with " + serverName + "...\n");
				
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);

				sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				KA = rc4.decrypt(sentence.toCharArray());
				
				sentence = rc4.encrypt(KB.toCharArray());
				sendMessage(sentence);
				
				K = sha1(KA.concat(KB));
				
				append("Chat is now secure!\n");
				append("-----------------------------------------------------------\n");

				chatArea.setEnabled(true);

			}
			else
			{
				LogInFrame.setVisible(true);
				ChatFrame.dispose();
				loginInfo.setText("Error: Password Does Not Match Server's");
				password.setText("");
			}
		}
		catch (Exception e)
		{
			LogInFrame.setVisible(true);
			ChatFrame.dispose();
			loginInfo.setText("Error: " + e.getMessage());
			password.setText("");
			clientSocket.close();
		}

		return clientSocket;
	}

	private void sendMessage(String message)
	{
		try
		{
			sendData = message.getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
		}
		catch (Exception e)
		{
			LogInFrame.setVisible(true);
			ChatFrame.dispose();
			loginInfo.setText("Error: " + e.getMessage());
			password.setText("");
			clientSocket.close();
		}
	}

	private void sendEncryptedMessage(String M)
	{
		try
		{
			String H = sha1(K.concat(M));
			String C = rc4.encrypt(M.concat(H).toCharArray());

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			sendPacket.setData(C.getBytes("UTF-8"));
			clientSocket.send(sendPacket);
		}
		catch (Exception e)
		{
			LogInFrame.setVisible(true);
			ChatFrame.dispose();
			loginInfo.setText("Error: " + e.getMessage());
			password.setText("");
			clientSocket.close();
		}
	}

	private String sha1(String input) throws NoSuchAlgorithmException
	{
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++)
		{
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	class Listen implements Runnable
	{
		private DatagramSocket clientSocket;

		public Listen()
		{
		}

		public void run()
		{
			clientSocket = Connect();
			byte[] receiveData = new byte[65527];
			try
			{
				while (true)
				{
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);

					String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
					append(serverName + "'s encrypted message: " + sentence + "\n");
					
					String MH = rc4.decrypt(sentence.toCharArray());
				
					if(MH.length() >= 40)
					{
						String M = MH.substring(0, MH.length()-40);
						String H = MH.substring(MH.length()-40, MH.length());
						String Hprime = sha1(K.concat(M));
						
						if(H.equals(Hprime))
						{
							if(M.equals("EXITEDWITHCODE0"))
							{
								LogInFrame.setVisible(true);
								ChatFrame.dispose();
								loginInfo.setText("Error: Server has disconnected");
								password.setText("");
								clientSocket.close();
							}
							else	
								append(serverName + ": " + M);
						}
						else
						{
							append("Decryption Error\n");
						}
					}
					else
					{
						append("Decryption/Message Delivery Error\n");
					}
				}
			}
			catch (Exception e)
			{
				LogInFrame.setVisible(true);
				ChatFrame.dispose();
				loginInfo.setText("Error: " + e.getMessage());
				password.setText("");
				clientSocket.close();
			}
		}
	}

	private void Chat()
	{
		try
		{
			LogInFrame.setVisible(false);

			ChatFrame = new JFrame();
			ChatFrame.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			// ChatFrame.setLocation(dim.width / 2 - ChatFrame.getSize().width /
			// 2,
			// dim.height / 2 - ChatFrame.getSize().height / 2);

			mainChatBox = new JTextArea();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			mainChatBox.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(mainChatBox);
			scrollPane.setPreferredSize(new Dimension(600, 200));

			ChatFrame.add(scrollPane, c);

			label = new JLabel(" ");
			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.BOTH;
			ChatFrame.add(label, c);

			chatArea = new JTextArea();
			chatArea.setPreferredSize(new Dimension(600, 30));
			chatArea.addKeyListener(new chatSendListener());
			c.gridx = 0;
			c.gridy = 2;
			c.fill = GridBagConstraints.BOTH;
			chatArea.setEnabled(false);
			ChatFrame.add(chatArea, c);

			ChatFrame.pack();
			ChatFrame.setVisible(true);
			ChatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ChatFrame.addWindowListener(new closeChat());
		}
		catch (Exception e)
		{
			LogInFrame.setVisible(true);
			ChatFrame.dispose();
			loginInfo.setText("Error: " + e.getMessage());
		}
	}

	private class chatSendListener implements KeyListener
	{

		@Override
		public void keyTyped(KeyEvent e)
		{
			if (e.getKeyChar() == KeyEvent.VK_ENTER && chatArea.getText().length()>1)
			{
				try
				{
					if (chatArea.getText().toLowerCase().contains("exit"))
					{
						sendEncryptedMessage("EXITEDWITHCODE0");
						ChatFrame.setVisible(false);
						ChatFrame.dispose();
						LogInFrame.dispose();
						System.exit(0);
					}
					else
					{
						append(username.getText() + ": " + chatArea.getText());
						sendEncryptedMessage(chatArea.getText());
						chatArea.setText("");
					}
				}
				catch (Exception e2)
				{
					LogInFrame.setVisible(true);
					ChatFrame.dispose();
					loginInfo.setText("Error: " + e2.getMessage());
					e2.printStackTrace();
				}
			}
			else if(chatArea.getText().length()<1)
				chatArea.setText("");
		}

		@Override
		public void keyPressed(KeyEvent e){}
		@Override
		public void keyReleased(KeyEvent e){}
	}
	
	public class closeChat implements WindowListener 
	{

		@Override
		public void windowActivated(WindowEvent arg0) {}
		@Override
		public void windowClosed(WindowEvent arg0) {}
		@Override
		public void windowClosing(WindowEvent arg0) 
		{
			sendEncryptedMessage("EXITEDWITHCODE0");
			ChatFrame.setVisible(false);
			ChatFrame.dispose();
			LogInFrame.dispose();
			System.exit(0);
		}
		@Override
		public void windowDeactivated(WindowEvent arg0) {}
		@Override
		public void windowDeiconified(WindowEvent arg0) {}
		@Override
		public void windowIconified(WindowEvent arg0) {}
		@Override
		public void windowOpened(WindowEvent arg0) {}

	}


	private String generateKey(int BitSize)
	{
		String result = "";
		Random random = new Random();

		for (int i = 0; i < BitSize; i++)
		{
			if (random.nextBoolean())
				result = result.concat("0");
			else
				result = result.concat("1");
		}

		return result;
	}
	
	private void append(String text)
	{
		mainChatBox.append(text);
		mainChatBox.setCaretPosition(mainChatBox.getDocument().getLength());
	}

}
