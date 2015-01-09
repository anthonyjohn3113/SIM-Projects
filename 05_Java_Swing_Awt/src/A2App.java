/*
CSCI213 Assignment 2
-----------------------------------------
File name: A2app.java
Author: Garcia Anthony John Abril
Registration Number: 4321819
Description: main functions that drive the whole program
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.text.*;

public class A2App
{
	// globally used objects
	private static NumberFormat NumF;
	private static JButton button;
	private static JLabel label;
	
	// panels
	private static JPanel intro_panel;
	private static JPanel catalogue_panel;
	private static JPanel order_panel;
	private static JPanel track_panel;
	
	// catalogue panel
	private static JTextField totalBooks;
	private static JTextField totalCost;
	private static JTextField memberNum;
	private static JTextField promoCode;
	
	// order panel
	private static JTextArea step1;
	private static JTextField pWord;
	private static JTextField uName;
	private static JTextField Name;
	private static JTextField NewpWord;
	private static JTextField NewRepWord;
	private static JTextField NewuName;
	private static JTextField Address;
	private static JTextField ContactNum;
	private static JTextField uEmail;
	private static JTextField dDate;
	private static JTextArea confirmed;
	private static JButton login;
	private static boolean loginenabled;
	private static JButton amend;
	private static boolean amendenabled;
	private static JButton register;
	private static boolean registerenabled;
	private static JCheckBox selfCollect;
	private static JCheckBox byPost;
	private static boolean registered;
	private static boolean loggedin;
	private static JButton confirm;
	private static boolean ordered;
	private static JButton update;
	
	// track panel
	private static JTextField orderID;
	private static JTextArea orderInfo;
	private static String orderData;
	
	// classes and frame
	private static order custorder = new order();
	private static customer cust = new customer();
	private static JFrame frame = new JFrame("Groovy Book Company");
	private static JFrame amendframe;
	
	public static void main(String[] args) 
	{
		frame.setPreferredSize(new Dimension(900,1600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tp = new JTabbedPane();
		
		intro();
		catalogue();
		order();
		track();
		
		tp.addTab("Intro",intro_panel);
		tp.addTab("Catalogue",catalogue_panel);
		tp.addTab("Order",order_panel);
		tp.addTab("Track",track_panel);
		
		JPanel Panel = new JPanel();
		Panel.setPreferredSize(new Dimension(800,1500));
		Panel.setLayout(new BorderLayout());
		frame.getContentPane().add(Panel);
		Panel.add(tp,BorderLayout.CENTER);
		
		// add scrollbar
		JScrollPane jsp = new JScrollPane(Panel);
		frame.add(jsp);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		
		// add icon for the java application
		Image icon = Toolkit.getDefaultToolkit().getImage("pics/icon.jpeg");
		frame.setIconImage(icon);
	}
	
	// intro panel
	public static void intro()
	{
		intro_panel = new JPanel();
		intro_panel.setLayout(null);
		
		label = new JLabel ("Groovy Book Company"); 
		label.setFont(new Font("Heveltica",Font.BOLD,50));
		label.setBounds(10,15,850,60);
		intro_panel.add(label);
		ImageIcon bookIcon = new ImageIcon("pics/bookshelf.jpg");
		label = new JLabel(bookIcon);
		label.setBounds(10, 75, 840, 280);
		intro_panel.add(label);
		
		label = new JLabel ("Select <Catalogue> to browse and select your order");
		label.setFont(new Font("Heveltica",Font.BOLD,20));
		label.setBounds(10,360,850,50);
		intro_panel.add(label);
		label = new JLabel ("Select <Order> to check out");
		label.setFont(new Font("Heveltica",Font.BOLD,20));
		label.setBounds(10,420,850,50);
		intro_panel.add(label);
		label = new JLabel ("Select <Track> to track your order status");
		label.setFont(new Font("Heveltica",Font.BOLD,20));
		label.setBounds(10,480,850,50);
		intro_panel.add(label);
		
		label = new JLabel ("For customers with a promotional code, please enter the code to enjoy discount on prices when you place your order.");
		label.setFont(new Font("Heveltica",Font.PLAIN,12));
		label.setBounds(30,580,850,50);
		intro_panel.add(label);
		
		label = new JLabel ("For premium members, please enter your membership code to enjoy further discount.");
		label.setFont(new Font("Heveltica",Font.PLAIN,12));
		label.setBounds(30,600,850,50);
		intro_panel.add(label);
		
		// exits the app after click
		button = new JButton("Click to Exit");
		button.addActionListener(new exitApp());
		button.setPreferredSize(new Dimension(800,45));
		button.setBounds(30,650,800,50);
		intro_panel.add(button);
	}
	
	private static class exitApp implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			// JOptionPane.showmessagedialouge is called
			displayInfo("Thank you for visiting Groovy Book Company.");
			System.exit(0);
		}
	}
	
	public static void catalogue()
	{
		catalogue_panel = new JPanel();
		catalogue_panel.setLayout(null);
		
		// used for converting double to currency format
		NumF = NumberFormat.getCurrencyInstance();
		ImageIcon bookIcon;
		
		// Header labels
		label = new JLabel("Books");
		label.setBounds(30,30,750,20);
		catalogue_panel.add(label);
		label = new JLabel("Hardcopy");
		label.setBounds(200,30,750,20);
		catalogue_panel.add(label);
		label = new JLabel("Quantity");
		label.setBounds(350,30,750,20);
		catalogue_panel.add(label);
		label = new JLabel("EBook");
		label.setBounds(500,30,750,20);
		catalogue_panel.add(label);
		label = new JLabel("Quantity");
		label.setBounds(650,30,750,20);
		catalogue_panel.add(label);
		
		// book icons, book info are shown when mouse clicked is activated
		bookIcon = new ImageIcon("pics/Book1.jpeg");
		label = new JLabel(custorder.getBookTitle(0),bookIcon,SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(20,85,125,220);
		label.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){displayBookInfo("Book1Info.png");}});
		catalogue_panel.add(label);
		
		// combobox for quantity
		QuantityPanel qty = new QuantityPanel(custorder,0,1);
		qty.setBounds(320,200,125,100);
		catalogue_panel.add(qty);
		
		// price label
		label = new JLabel(NumF.format(custorder.getPrice(0,0)));
		label.setBounds(205,210,100,12);
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,0,3);
		qty.setBounds(620,200,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(0,2)));
		label.setBounds(510,210,100,12);
		catalogue_panel.add(label);

		bookIcon = new ImageIcon("pics/Book2.jpeg");
		label = new JLabel(custorder.getBookTitle(1),bookIcon,SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(20,300,125,220);
		label.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){displayBookInfo("Book2Info.png");}});
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,1,1);
		qty.setBounds(320,415,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(1,0)));
		label.setBounds(205,425,100,12);
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,1,3);
		qty.setBounds(620,415,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(1,2)));
		label.setBounds(510,425,100,12);
		catalogue_panel.add(label);
		
		bookIcon = new ImageIcon("pics/Book3.jpeg");
		label = new JLabel(custorder.getBookTitle(2),bookIcon,SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(20,515,125,220);
		label.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){displayBookInfo("Book3Info.png");}});
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,2,1);
		qty.setBounds(320,630,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(2,0)));
		label.setBounds(205,640,100,12);
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,2,3);
		qty.setBounds(620,630,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(2,2)));
		label.setBounds(510,640,100,12);
		catalogue_panel.add(label);
		
		bookIcon = new ImageIcon("pics/Book4.jpeg");
		label = new JLabel(custorder.getBookTitle(3),bookIcon,SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(20,730,125,220);
		label.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){displayBookInfo("Book4Info.png");}});
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,3,1);
		qty.setBounds(320,845,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(3,0)));
		label.setBounds(205,855,100,12);
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,3,3);
		qty.setBounds(620,845,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(3,2)));
		label.setBounds(510,855,100,12);
		catalogue_panel.add(label);
		
		bookIcon = new ImageIcon("pics/Book5.jpeg");
		label = new JLabel(custorder.getBookTitle(4),bookIcon,SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(20,945,125,220);
		label.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){displayBookInfo("Book5Info.png");}});
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,4,1);
		qty.setBounds(320,1060,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(4,0)));
		label.setBounds(205,1070,100,12);
		catalogue_panel.add(label);
		
		qty = new QuantityPanel(custorder,4,3);
		qty.setBounds(620,1060,125,100);
		catalogue_panel.add(qty);
		
		label = new JLabel(NumF.format(custorder.getPrice(4,2)));
		label.setBounds(510,1070,100,12);
		catalogue_panel.add(label);
		
		// updates upon mouse movement
		label = new JLabel("Total No. Of Books: ");
		label.setBounds(470,1220,250,30);
		catalogue_panel.add(label);
		totalBooks = new JTextField(20);
		catalogue_panel.addMouseMotionListener(new MouseMotionEvent());
		totalBooks.setBounds(610,1220,200,30);
		totalBooks.setEditable(false);
		catalogue_panel.add(totalBooks);
		
		label = new JLabel("Total Price: ");
		label.setBounds(522,1260,250,30);
		catalogue_panel.add(label);
		totalCost = new JTextField(20);
		catalogue_panel.addMouseMotionListener(new MouseMotionEvent());
		totalCost.setBounds(610,1260,200,30);
		totalCost.setEditable(false);
		catalogue_panel.add(totalCost);
		
		label = new JLabel("Membership number: ");
		label.setBounds(459,1300,250,30);
		catalogue_panel.add(label);
		memberNum = new JTextField(20);
		memberNum.setBounds(610,1300,200,30);
		catalogue_panel.add(memberNum);
		
		label = new JLabel("Promotional code: ");
		label.setBounds(478,1340,250,30);
		catalogue_panel.add(label);
		promoCode = new JTextField(20);
		promoCode.setBounds(610,1340,200,30);
		catalogue_panel.add(promoCode);
		
		label = new JLabel("To Continue, go to <ORDER> Tab.");
		label.setBounds(300,1390,250,30);
		catalogue_panel.add(label);
		
	}
	
	
	private static class MouseMotionEvent extends JPanel implements MouseMotionListener 
	{
		public MouseMotionEvent() 
		  {
			    addMouseMotionListener(this);
		  }

		  public void mouseMoved(MouseEvent e) 
		  {
			  // updates total cost and total number of books
			  NumF = NumberFormat.getCurrencyInstance();
			  totalBooks.setText(custorder.getTotal());
			  totalCost.setText(NumF.format(custorder.getCost()));
		  }

		  public void mouseDragged(MouseEvent e) 
		  {
		  }

	}
	
	public static void order()
	{
		order_panel = new JPanel();
		order_panel.setLayout(null);
		
		// mouse movement to update step 1 text area, enables buttons and display confirmed order
		order_panel.addMouseMotionListener(new MouseMotionEvent2());
		
		step1 = new JTextArea(35,200);
		step1.setBounds(0,0,858,210);
		step1.setEditable(false);
		order_panel.add(step1);
		
		// labels and text fields for existing customer
		label = new JLabel("Step 2a - Existing Customer");
		label.setBounds(0,215,300,20);
		order_panel.add(label);
		label = new JLabel("Please Login.");
		label.setBounds(0,235,300,20);
		order_panel.add(label);
		
		label = new JLabel("Username:");
		label.setBounds(0,265,300,20);
		order_panel.add(label);
		
		uName = new JTextField(60);
		uName.setBounds(460,265,400,35);
		order_panel.add(uName);
		
		label = new JLabel("Password:");
		label.setBounds(0,295,300,20);
		order_panel.add(label);
		
		pWord = new JPasswordField(60);
		pWord.setBounds(460,295,400,35);
		order_panel.add(pWord);
		
		// button for logging in, listener logs in user
		loginenabled = false;
		login = new JButton("Login");
		login.setBounds(460,325,200,35);
		login.addActionListener(new LoginListener());
		order_panel.add(login);
		
		// button for amending details of logged in customer, calls another function
		amendenabled = false;
		amend = new JButton("Amend Details");
		amend.setBounds(660,325,200,35);
		amend.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){amenddetails();}});
		order_panel.add(amend);
		
		// labels and text fields for new customer
		label = new JLabel("Step 2b - New Customer");
		label.setBounds(0,350,300,20);
		order_panel.add(label);
		label = new JLabel("Please Register. (For new customers)");
		label.setBounds(0,370,300,20);
		order_panel.add(label);
		
		label = new JLabel("Name:");
		label.setBounds(0,400,300,20);
		order_panel.add(label);
		
		Name = new JTextField(60);
		Name.setBounds(460,390,400,35);
		order_panel.add(Name);
		
		label = new JLabel("Username:");
		label.setBounds(0,430,300,20);
		order_panel.add(label);
		
		NewuName = new JTextField(60);
		NewuName.setBounds(460,420,400,35);
		order_panel.add(NewuName);
		
		label = new JLabel("Password:");
		label.setBounds(0,460,300,20);
		order_panel.add(label);
		
		NewpWord = new JPasswordField(60);
		NewpWord.setBounds(460,450,400,35);
		order_panel.add(NewpWord);
		
		label = new JLabel("Re-Enter Password:");
		label.setBounds(0,490,300,20);
		order_panel.add(label);
		
		NewRepWord = new JPasswordField(60);
		NewRepWord.setBounds(460,480,400,35);
		order_panel.add(NewRepWord);
		
		label = new JLabel("Address:");
		label.setBounds(0,520,300,20);
		order_panel.add(label);
		
		Address = new JTextField(60);
		Address.setBounds(460,510,400,35);
		order_panel.add(Address);
		
		label = new JLabel("Contact Number:");
		label.setBounds(0,550,300,20);
		order_panel.add(label);
		
		ContactNum = new JTextField(60);
		ContactNum.setBounds(460,540,400,35);
		order_panel.add(ContactNum);
		
		label = new JLabel("Email:");
		label.setBounds(0,580,300,20);
		order_panel.add(label);
		
		uEmail = new JTextField(60);
		uEmail.setBounds(460,570,400,35);
		order_panel.add(uEmail);
		
		// register button, listener registers customer
		registerenabled = false;
		register = new JButton("Register");
		register.setBounds(460,600,400,35);
		register.addActionListener(new RegisterListener());
		order_panel.add(register);
		
		label = new JLabel("Step 3 - Delivery Option");
		label.setBounds(0,640,300,20);
		order_panel.add(label);
		label = new JLabel("Select your preferred delivery option.");
		label.setBounds(0,660,300,20);
		order_panel.add(label);
		
		selfCollect = new JCheckBox("Self-Collect");
		selfCollect.setBounds(5,685,150,30);
		byPost =  new JCheckBox("By Post");
		byPost.setBounds(175,685,100,30);
		
		// checkbox are grouped to allow only one check
		ButtonGroup deliveryGroup = new ButtonGroup();
		deliveryGroup.add(selfCollect);
		deliveryGroup.add(byPost);
		// listener updates the selected value
		DeliveryListener DListener= new DeliveryListener();
		selfCollect.addActionListener(DListener);
		byPost.addActionListener(DListener);
		order_panel.add(selfCollect);
		order_panel.add(byPost);
		
		label = new JLabel("Delivery Date:");
		label.setBounds(300,685,300,30);
		order_panel.add(label);
		
		// upon clicking text clears
		dDate = new JTextField("DD/MM/YYYY",115);
		dDate.setBounds(390,685,115,35);
		dDate.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){dDate.setText("");}});
		order_panel.add(dDate);
		
		label = new JLabel("Delivery Time:");
		label.setBounds(540,685,300,30);
		order_panel.add(label);
		
		// time combobox
		TimePanel time = new TimePanel(custorder);
		time.setBounds(630,685,125,100);
		order_panel.add(time);
		
		// button activates confirm purchase
		ordered = false;
		confirm = new JButton("Confirm");
		confirm.setBounds(5,730,400,35);
		confirm.addActionListener(new ConfirmListener());
		order_panel.add(confirm);
		
		// text shown after confirmed purchase
		confirmed = new JTextArea(20,200);
		confirmed.setBounds(5,770,400,100);
		confirmed.setOpaque(false);
		confirmed.setEditable(false);
		order_panel.add(confirmed);
		
	}
	
	
	private static class MouseMotionEvent2 extends JPanel implements MouseMotionListener 
	{
		public MouseMotionEvent2() 
		  {
			  addMouseMotionListener(this);
		  }

		  public void mouseMoved(MouseEvent e) 
		  {
			  // text field for step 1 gets filled up with customer purchase details
			  step1.setText(getStep1Data());
			  
			  boolean confirmenabled = false;
			  
			  // if logged in or registered, confirm and amend buttons enabled, rest disabled
			  if(loggedin || registered)
			  {
				  amendenabled = true;
				  loginenabled = false;
				  registerenabled = false;
				  confirmenabled = true;
			  }
			  else
			  {
				  amendenabled = false;
				  loginenabled = true;
				  registerenabled = true;
				  confirmenabled = false;
			  }
			  
			  login.setEnabled(loginenabled);
			  register.setEnabled(registerenabled);
			  amend.setEnabled(amendenabled);
			  confirm.setEnabled(confirmenabled);
			  
			  // once order is done
			  if(ordered == true)
			  {
				  confirmed.setText("Thank you for your order." + "\nYour order number is " + getConfirmedOrder());
				  ordered = false;
				  reset();
			  }	
		  }

		  public void mouseDragged(MouseEvent e) 
		  {
		  }

	}
	
	public static String getStep1Data()
	{
		// string starts with null, data are added with customer selected items, most values default to 0
		String data = null;
		
		data = "Step 1 - Confirm your Order." +
		       "\nYour Order" +
			   "\n==============================================================\n";
		for(int i=0; i<5; i++)
		{
			if(custorder.getQty(i,1)!=0 || custorder.getQty(i,3)!=0)
			{
				data += custorder.getBookTitle(i) + ": ";
				if(custorder.getQty(i,1)!=0)
					data += custorder.getQty(i,1) + " Hardcopy Book(s) ";
				if(custorder.getQty(i,3)!=0)
					data += custorder.getQty(i,3) + " EBook(s) license(s) ";
				data += "\n";
			}
		}
		NumF = NumberFormat.getCurrencyInstance();
		data += "==============================================================\n" +
		       "No of Books Ordered: " + custorder.getTotal() + 
		       "\tTotal Cost: " + NumF.format(custorder.getCost()) +
		       getDeliveryMethod() + finalcost() +
		       "\n==============================================================\n";
		return data;
	}
	
	private static String finalcost()
	{
		//string for obtaining final cost, includes promotions, membership discount and delivery charges
		NumF = NumberFormat.getCurrencyInstance();
		String data = "";
		double delivery = 0;
		
		if(Integer.parseInt(custorder.getTotal())!=0)
		{
			switch(custorder.getDeliveryMethod())
			{
				// case 2 and 4 are by post delivery methods, 2 with only hard copy, 4 with both types (ebooks are emailed)
				case 2: 
				case 4: delivery = custorder.getNumofHbooks() * 3;
						data += "\nBy Post Delivery Charge ($3/Hard copy Book): " + NumF.format(delivery);
						break;
			}
		}
		data += "\nFinal cost ";
		double discount = 0;
		
		if(!memberNum.getText().equals("") || !promoCode.getText().equals(""))
		{
			if(!memberNum.getText().equals(""))
			{
				if(checkmemberNum()!=0)
				{
					data += "(" + checkmemberNum()*100 + "% discount for premium membership) ";
					discount += checkmemberNum();
				}
			}
			if(!promoCode.getText().equals(""))
			{
				if(checkpromoCode()!=0)
				{
					data += "(" + checkpromoCode()*100 + "% discount for promo code) ";
					discount += checkpromoCode();
				}
			}
			
		}
		custorder.setFinalCost(discount,delivery);
		data += ": " + NumF.format(custorder.getFinalCost());
		
		return data;
	}
	
	private static String getDeliveryMethod()
	{
		// types of delivery method, lines are displayed in step 1 text field
		String data = "";
		if(Integer.parseInt(custorder.getTotal())!=0)
		{
			switch(custorder.getDeliveryMethod())
			{
				case 1: data += "\nDelivery Method: Self collection for hardcopy books only";
						break;
				case 2: data += "\nDelivery Method: By post for hardcopy books only";
						break;
				case 3: data += "\nDelivery Method: Self collection for hardcopy books and email for ebooks";
						break;
				case 4: data += "\nDelivery Method: By post for hardcopy books and email for ebooks";
						break;
				case 5: data += "\nDelivery Method: Email for ebooks only (No hardcopy books selected)";
			}
		}
		return data;
	}
	
	private static double checkmemberNum()
	{
		try 
		{
			//Checks for premium membership code and obtains the discount percentage
			Scanner readFile= new Scanner(new File("MembershipCode.txt"));
			while(readFile.hasNextLine())
			{
				String data = readFile.nextLine();
				String[] memberInfo = data.split(";");
				
				if(memberInfo[0].equals(memberNum.getText()))
				{
					readFile.close();
					return Double.parseDouble(memberInfo[1]);
				}
			}
			readFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			return 0;
		}
		return 0;
	}
	
	private static double checkpromoCode()
	{
		try 
		{
			//Checks for  promo code and obtains the discount percentage
			Scanner readFile= new Scanner(new File("PromoCode.txt"));
			while(readFile.hasNextLine())
			{
				String data = readFile.nextLine();
				String[] promoInfo = data.split(";");
				
				if(promoInfo[0].equals(promoCode.getText()))
				{
					readFile.close();
					return Double.parseDouble(promoInfo[1]);
				}
			}
			readFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			return 0;
		}
		return 0;
	}
	
	private static class LoginListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			// check if customer registered to prevent him from logging in when he just registered
			// also checks if customer exists and stores customer info to class from text file
			if(!registered)
				loggedin = custCheck();
			else
				displayError("You just registered, please proceed to Step 3");
		}	
	}
	
	public static boolean custCheck()
	{
		try 
		{
			// checks if customer exists and stores customer info to class from text file
			Scanner readFile= new Scanner(new File("customerInfo.txt"));
			while(readFile.hasNextLine())
			{
				String data = readFile.nextLine();
				String[] custInfo = data.split(";");
				
				if(custInfo[1].equals(uName.getText()))
				{
					if(custInfo[2].equals(pWord.getText()))
					{
						cust.setName(custInfo[0]);
						cust.setUName(custInfo[1]);
						cust.setPwd(custInfo[2]);
						cust.setAdd(custInfo[3]);
						cust.setPNum(custInfo[4]);
						cust.setEmail(custInfo[5]);
						displayInfo("You are now logged in. Continue to step 3");
						readFile.close();
						return true;
					}
					else
					{
						displayError("Wrong Password");
						pWord.setText("");
						readFile.close();
						return false;
					}
				}
			}
			readFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			displayError("User not found");
			return false;
		}
		uName.setText("");
		pWord.setText("");
		displayError("User not found");
		return false;
	}
	
	private static class RegisterListener implements ActionListener
	{
		// validation check for registering for new customer
		public void actionPerformed(ActionEvent e) 
		{
			registered = true;
			if(!loggedin)
			{
				if(cust.setName(Name.getText()) == false)
				{
					displayError("Name must be more than 3 characters");
					Name.setText("");
					registered = false;
				}
				if(cust.setUName(NewuName.getText()) == false)
				{
					displayError("Username must be more than 3 characters");
					NewuName.setText("");
					registered = false;
				}
				if(existuNameCheck())
				{
					displayError("Username already exist");
					NewuName.setText("");
					registered = false;
				}
				if(cust.setPwd(NewpWord.getText()) == false)
				{
					displayError("Password must be more than 3 characters");
					NewpWord.setText("");
					registered = false;
				}
				if(!NewRepWord.getText().equals(NewpWord.getText()))
				{
					displayError("Password does not match, please re-type");
					NewpWord.setText("");
					NewRepWord.setText("");
					registered = false;
				}
				if(cust.setAdd(Address.getText()) == false)
				{
					displayError("Address must be more than 8 characters");
					Address.setText("");
					registered = false;
				}
				if(cust.setPNum(ContactNum.getText()) == false)
				{
					displayError("Contact Number must numbers and more than 4 digits");
					ContactNum.setText("");
					registered = false;
				}
				if(cust.setEmail(uEmail.getText()) == false)
				{
					displayError("Email invalid. Try again");
					uEmail.setText("");
					registered = false;
				}
				if(registered)
				{
					// updates customerinfo.txt
					rewriteCustomerinfo();
					displayInfo("You are now registered. Continue to step 3");
					registered = true;
					loggedin = true;
				}
			}
			else
				displayError("Register failed. You just logged in, please proceed to Step 3");
		}	
	}
	
	public static boolean existuNameCheck()
	{
		try 
		{
			// checks if username already exists to prevent overlapping records
			Scanner readFile= new Scanner(new File("customerInfo.txt"));
			while(readFile.hasNextLine())
			{
				String data = readFile.nextLine();
				String[] custInfo = data.split(";");
				
				if(custInfo[1].equals(NewuName.getText()))
				{
					readFile.close();
					return true;
				}
			}
			readFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			return false;
		}
		return false;
	}
	
	public static void rewriteCustomerinfo()
	{
		try
		{
			// updates customer file with new customer registration
			File file = new File("Customerinfo.txt");
	 
	    		//if file doesn't exists, then create it
	    	if(!file.exists())
	    	{
	    		file.createNewFile();
	    	}
	    		//true = append file
	    	FileWriter fileWritter = new FileWriter(file.getName(),true);
	    	BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	
	    	String data = cust.getName() + ";" + cust.getUName() + ";" +
						  cust.getPwd()  + ";" + cust.getAdd()   + ";" +
						  cust.getPNum() + ";" + cust.getEmail() + "\n";
	    	    
	    	bufferWritter.write(data);
	    	bufferWritter.close();
		}
		catch(IOException e)
		{
	    	e.printStackTrace();
	    }
		
	}
	
	private static class DeliveryListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			// gets the delivery method selection from checkboxes and set the selection into the orderclass
			Object source = e.getSource();
			
			custorder.setDeliveryMethod(0);
			
			if(source == selfCollect)
				custorder.setDeliveryMethod(1);
			if(source == byPost)
				custorder.setDeliveryMethod(2);
			if(custorder.getNumofEbooks()!=0 && custorder.getNumofHbooks()!=0 && source == selfCollect)
				custorder.setDeliveryMethod(3);
			if(custorder.getNumofEbooks()!=0 && custorder.getNumofHbooks()!=0 && source == byPost)
				custorder.setDeliveryMethod(4);
			if(custorder.getNumofEbooks()!=0 && custorder.getNumofHbooks()==0 )
				custorder.setDeliveryMethod(5);
				
		}	
	}
	
	private static class ConfirmListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			// checks if order is empty, date error, or selected delivery time before confirmation of order
			if(Integer.parseInt(custorder.getTotal()) == 0)
				displayError("Order list is empty");
			else if(custorder.getDeliveryMethod()==0)
				displayError("Pick a delivery method (For Ebooks only order pick any method)");
			else if(!isValidDate(dDate.getText()))
				dDate.setText("DD/MM/YYYY");
			else if(custorder.getSelectedTime()==0)
				displayError("Pick delivery/pick-up time");
			else
			{
				ordered = true;
				custorder.setdDate(dDate.getText());
			}
				
		}	
	}
	
	private static String getConfirmedOrder()
	{
		// unique number generated as order id
		String ordernum = ordernumgenerator();

		try
		{
			// data to be written in order text file
    		String data = ordernum 				+ ";" + 
    					  cust.getUName()  		+ ";" + 
    					  custorder.getdDate() 	+ ";" +
    					  custorder.getTime(custorder.getSelectedTime()) + ";" +
    					  custorder.getDeliveryMethodString() + ";" + "PENDING\n";
 
    		File file = new File("orderData.txt");
 
    		//if file doesn't exists, then create it
    		if(!file.exists())
    		{
    			file.createNewFile();
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    	    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	    bufferWritter.write(data);
    	    bufferWritter.close();
    	}
		catch(IOException e)
		{
    		e.printStackTrace();
    	}
		return ordernum;
	}
	
	private static String ordernumgenerator()
	{
		// unique order is generated using date and time format
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	private static boolean isValidDate(String date)
	{
		
		if(date.length()!=10)
		{
			// invalid length
			displayError("Invalid date format, must be DD/MM/YYYY");
	    	return false;
		}
		
		if(date.charAt(2)!='/' && date.charAt(6)!='/')
		{
			// slashes are not used
			displayError("Invalid date format, must be DD/MM/YYYY");
	    	return false;
		}
		
		// initialization of number of days for every month
		int[] months = {31,29,31,30,31,30,31,31,30,31,30,31};
		int day, month, year;
	    try
	    {
	    	// conversion of date to integers
	    	day = Integer.parseInt(date.substring(0,2));
	    	month = Integer.parseInt(date.substring(3,5));
	    	year = Integer.parseInt(date.substring(6,10));
	    }
	    catch(NumberFormatException e)
	    {
	    	// non numeric for date values found
	    	displayError("Invalid date format: format is DD/MM/YYYY, zeros must be included");
	    	return false;
	    }

	    if(year<2013)
	    {
	    	
	    	displayError("Invalid year: past booking not allowed");
	    	return false;
	    }
	    
	    if(month<1 || month>12)
	    {
	    	displayError("Invalid month");
	    	return false;
	    }
	    if(day<1)
	    {
	    	System.out.println("Invalid day");
	    	return false;
	    }
	    
	    // initialization of current time
	    Calendar cal = Calendar.getInstance();
	    
	    if(year > (cal.get(1)+1))
	    {
	    	displayError("Invalid year: only current year allowed");
	    	return false;
	    }
	    
	    // checking with current date
	    if(year == cal.get(1))
	    {
	    	if(month < (cal.get(2)+1))
	    	{
	    		displayError("Invalid month: past booking not allowed");
	    		return false;
	    	}
	    		
	    	else if(month == (cal.get(2)+1))
	    	{
	    		if(day == cal.get(5))
	    		{
	    			displayError("Invalid day: same day booking not allowed");
	    			return false;
	    		}
	    		else if(day < cal.get(5))
	    		{
	    			displayError("Invalid day: past booking not allowed");
	    			return false;
	    		}
	    	}
	    }
	    
	    // check for leap year
	    if((year%4)==0)
	    {
	    	if(day>months[month-1])
	    	{
	    		displayError("Invalid day");
	    		return false;
	    	}
	    }
	    else if((year%4)!=0)
	    {
	    	if(month==2 && day>28)
	    	{
	    		displayError("Invalid day");
	    		return false;
	    	}
	    	else if(day>months[month-1])
		    {
	    		displayError("Invalid day");
		    	return false;
		    }
	    }
	    	
	    return true;
	}
	
	// following functions are for pop out messaged for book info, error info and other info
	private static void displayBookInfo(String location)
	{
		final ImageIcon icon = new ImageIcon("pics/" + location);
		JOptionPane.showMessageDialog(frame, icon, "About", JOptionPane.PLAIN_MESSAGE);
	}
	
	private static void displayError(String data)
	{
		JOptionPane.showMessageDialog(frame,data, "Error", JOptionPane.WARNING_MESSAGE);
	}

	private static void displayInfo(String data)
	{
		JOptionPane.showMessageDialog(frame,data, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	
	public static void track()
	{
		// tracking of order panel
		track_panel = new JPanel();
		track_panel.setLayout(null);
		
		label = new JLabel("Please enter order ID");
		label.setBounds(0,10,300,30);
		track_panel.add(label);
		
		label = new JLabel("Order ID: ");
		label.setBounds(0,50,300,30);
		track_panel.add(label);
		
		orderID = new JTextField(20);
		orderID.setBounds(55,50,200,30);
		track_panel.add(orderID);
		
		// listener checks for order id from order text file
		JButton track = new JButton("Track");
		track.setBounds(250,50,100,30);
		track.addActionListener(new TrackListener());
		track_panel.add(track);
		
		// listener updates the text area with order data found
		orderInfo = new JTextArea(20,200);
		orderInfo.setBounds(0,90,858,150);
		orderInfo.addMouseMotionListener(new MouseMotionEvent3());
		orderInfo.setEditable(false);
		orderInfo.setOpaque(false);
		track_panel.add(orderInfo);
	}
	
	private static class TrackListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// checks if record id found, if it is data is stored to be displayed at order panel
			if(recordfound() != null)
			{
				String [] orderInfo = recordfound().split(";");
				orderData = "Username: " 						+ orderInfo[1] +
				      	   	"\n\nDate of delivery/pick-up: " 	+ orderInfo[2] +
				      	   	"\n\nTime of delivery/pick-up: "	+ orderInfo[3] +
				      	    "\n\nDelivery option: "				+ orderInfo[4] +
				      	    "\n\nStatus: "						+ orderInfo[5] +
				      	    "\n";
			}
			else
				displayError("Order ID not found");
		}
	}
	private static String recordfound()
	{
		try
		{
			// looks for order info using order id provided by customer
			Scanner readFile = new Scanner(new File("orderData.txt"));
			
			while(readFile.hasNextLine())
			{
				String data = readFile.nextLine();
				String [] orderInfo = data.split(";");
				
				if(orderInfo[0].equals(orderID.getText()))
				{
					readFile.close();
					return data;
				}
				
			}
			readFile.close();
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
		return null;
	}
	private static class MouseMotionEvent3 extends JPanel implements MouseMotionListener 
	{
		// displays order information upon mouse movement
		public MouseMotionEvent3() 
		  {
			  addMouseMotionListener(this);
		  }

		  public void mouseMoved(MouseEvent e) 
		  {
			  orderInfo.setText(orderData);
		  }
		  public void mouseDragged(MouseEvent e) 
		  {
		  }
	}
	
	private static void reset()
	{
		// resets all text fields, customer and order class
		// called after completion of order
		totalBooks.setText("");
		totalCost.setText("");
		memberNum.setText("");
		promoCode.setText("");
		
		loggedin = false;
		registered = false;
		
		pWord.setText("");
		uName.setText("");
		Name.setText("");
		NewpWord.setText("");
		NewRepWord.setText("");
		NewuName.setText("");
		Address.setText("");
		ContactNum.setText("");
		uEmail.setText("");
		dDate.setText("DD/MM/YYYY");
		
		cust.setAdd("");
		cust.setEmail("");
		cust.setName("");
		cust.setPNum("");
		cust.setPwd("");
		cust.setUName("");
		
		custorder.setdDate("");
		custorder.setDeliveryMethod(0);
		custorder.setFinalCost(0,0);
		custorder.setQty(0,1,0);
		custorder.setQty(0,3,0);
		custorder.setQty(1,1,0);
		custorder.setQty(1,3,0);
		custorder.setQty(2,1,0);
		custorder.setQty(2,3,0);
		custorder.setQty(3,1,0);
		custorder.setQty(3,3,0);
		custorder.setQty(4,1,0);
		custorder.setQty(4,3,0);
		custorder.setTime(0);
	}
	private static void amenddetails()
	{
		// new frame and panel for amend details
		amendframe = new JFrame("Amend Details");
		amendframe.setPreferredSize(new Dimension(900,500));
		amendframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel amendpanel = new JPanel();
		amendpanel.setLayout(null);
		
		label = new JLabel("Name:");
		label.setBounds(5,20,300,20);
		amendpanel.add(label);
		
		Name = new JTextField(60);
		Name.setBounds(460,10,400,35);
		Name.setText(cust.getName());
		amendpanel.add(Name);
		
		// username is fixed as it is used for linking back with old record to prevent overlapping
		label = new JLabel("Username (cannot be changed):");
		label.setBounds(5,50,300,20);
		amendpanel.add(label);
		
		NewuName = new JTextField(60);
		NewuName.setBounds(460,40,400,35);
		NewuName.setText(cust.getUName());
		NewuName.setEditable(false);
		amendpanel.add(NewuName);
		
		label = new JLabel("Password:");
		label.setBounds(5,80,300,20);
		amendpanel.add(label);
		
		NewpWord = new JPasswordField(60);
		NewpWord.setBounds(460,70,400,35);
		NewpWord.setText(cust.getPwd());
		amendpanel.add(NewpWord);
		
		label = new JLabel("Re-Enter Password:");
		label.setBounds(5,110,300,20);
		amendpanel.add(label);
		
		NewRepWord = new JPasswordField(60);
		NewRepWord.setBounds(460,100,400,35);
		NewRepWord.setText(cust.getPwd());
		amendpanel.add(NewRepWord);
		
		label = new JLabel("Address:");
		label.setBounds(5,140,300,20);
		amendpanel.add(label);
		
		Address = new JTextField(60);
		Address.setBounds(460,130,400,35);
		Address.setText(cust.getAdd());
		amendpanel.add(Address);
		
		label = new JLabel("Contact Number:");
		label.setBounds(5,170,300,20);
		amendpanel.add(label);
		
		ContactNum = new JTextField(60);
		ContactNum.setBounds(460,160,400,35);
		ContactNum.setText(Integer.toString(cust.getPNum()));
		amendpanel.add(ContactNum);
		
		label = new JLabel("Email:");
		label.setBounds(5,200,300,20);
		amendpanel.add(label);
		
		uEmail = new JTextField(60);
		uEmail.setBounds(460,190,400,35);
		uEmail.setText(cust.getEmail());
		amendpanel.add(uEmail);
		
		label = new JLabel("Change existing details and click update to save changes");
		label.setBounds(5,240,500,20);
		amendpanel.add(label);
		
		// frame is not shown, then disposed after exiting
		JButton exit = new JButton("Exit");
		exit.setBounds(460,230,200,35);
		exit.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){amendframe.setVisible(false); amendframe.dispose();}});
		amendpanel.add(exit);
		
		// listener checks new customer details and updates the customer file
		update = new JButton("Update");
		update.setBounds(660,230,200,35);
		update.addActionListener(new AmendListener());
		amendpanel.add(update);
		
		amendframe.getContentPane().add(amendpanel);
		amendframe.pack();
		amendframe.setAlwaysOnTop(true);
		amendframe.setVisible(true);
	}
	
	private static class AmendListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			//checks new customer details and updates the customer file
			boolean updated = true;
			if(cust.setName(Name.getText()) == false)
			{
				displayAmendError("Name must be more than 3 characters");
				Name.setText("");
				updated = false;
			}
			if(cust.setUName(NewuName.getText()) == false)
			{
				displayAmendError("Username must be more than 3 characters");
				NewuName.setText("");
				updated = false;
			}
			if(cust.setPwd(NewpWord.getText()) == false)
			{
				displayAmendError("Password must be more than 3 characters");
				NewpWord.setText("");
				updated = false;
			}
			if(!NewRepWord.getText().equals(NewpWord.getText()))
			{
				displayAmendError("Password does not match, please re-type");
				NewpWord.setText("");
				NewRepWord.setText("");
				updated = false;
			}
			if(cust.setAdd(Address.getText()) == false)
			{
				displayAmendError("Address must be more than 8 characters");
				Address.setText("");
				updated = false;
			}
			if(cust.setPNum(ContactNum.getText()) == false)
			{
				displayAmendError("Contact Number must numbers and more than 4 digits");
				ContactNum.setText("");
				updated = false;
			}
			if(cust.setEmail(uEmail.getText()) == false)
			{
				displayAmendError("Email invalid. Try again");
				uEmail.setText("");
				updated = false;
			}
			// once no more errors, file is updated
			if(updated)
			{
				amendCustomerinfo();
				displayAmendError("You have updated your details");
			}
		}
		// pop out dialogue to show input errors
		private static void displayAmendError(String data)
		{
			JOptionPane.showMessageDialog(amendframe,data, "Amend Details Info", JOptionPane.WARNING_MESSAGE);
		}
		
		public static void amendCustomerinfo()
		{
			try 
			{
				// customer details are rewritten to a new file and once old record is found it is skipped
				// and the rest of file is rewritten
				Scanner readFile= new Scanner(new File("customerInfo.txt"));
				File outfile = new File("temp.txt");
				if(!outfile.exists())
		    	{
		    		outfile.createNewFile();
		    	}
				while(readFile.hasNextLine())
				{
					String data = readFile.nextLine();
					String[] custInfo = data.split(";");
					data += "\n";
						
					if(custInfo[1].equals(uName.getText()))
					{
						data = cust.getName() + ";" + cust.getUName() + ";" +
					    	   cust.getPwd()  + ";" + cust.getAdd()   + ";" +
							   cust.getPNum() + ";" + cust.getEmail() + "\n";
					}
					try
					{
				    	FileWriter fileWritter = new FileWriter(outfile.getName(),true);
				    	BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				    	    
				    	bufferWritter.write(data);
				    	bufferWritter.close();
					}
					catch(IOException e)
					{
				    	e.printStackTrace();
				    }
				}
				readFile.close();
				
				// old file is deleted and new temporary file is renamed to the original file
				File file = new File("customerInfo.txt");
				file.delete();
				File tempfile = new File("temp.txt");
				tempfile.renameTo(file);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
	}
}


class QuantityPanel extends JPanel implements ActionListener
{
	// panel for combo box for quantity of books
	private JComboBox<String> quantityCB;
	private int qty,bookno,booktype;
	order currOrder;
	
	public QuantityPanel(order custorder, int bno, int btype)
	{
		currOrder = custorder;
		bookno = bno;
		booktype = btype;
		String[] quantity = {"0","1","2","3","4","5","6","7","8","9","10"};
		quantityCB = new JComboBox<String>(quantity);
		quantityCB.setSelectedIndex(0);
		quantityCB.addActionListener(this);
		
		add(quantityCB);
		setPreferredSize(new Dimension(50,10));
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		// quantity is obtained and stored in order class
		qty = quantityCB.getSelectedIndex();
		currOrder.setQty(bookno, booktype, qty);
	}

}

class TimePanel extends JPanel implements ActionListener
{
	// panel for time combo box
	private JComboBox<String> TimeCB;
	order currOrder;
	
	public TimePanel(order custorder)
	{
		currOrder = custorder;
		// time string are obtained from order class, time can be edited in the class
		String[] time = {currOrder.getTime(0),currOrder.getTime(1),currOrder.getTime(2),currOrder.getTime(3),currOrder.getTime(4),currOrder.getTime(5)};
		TimeCB = new JComboBox<String>(time);
		TimeCB.setSelectedIndex(0);
		TimeCB.addActionListener(this);
		
		add(TimeCB);
		setPreferredSize(new Dimension(50,10));
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		// selected time is set in the order class
		currOrder.setTime(TimeCB.getSelectedIndex());
	}

}