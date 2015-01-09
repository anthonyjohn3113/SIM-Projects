/*
CSCI213 Assignment 2
-----------------------------------------
File name: order.java
Author: Garcia Anthony John Abril
Registration Number: 4321819
Description: order class to store customer order details
 */

public class order 
{
	// use of 2-d array for books and book prices
	private double[][] books;
	// use of array for book titles
	private String[] booktitles;
	private int deliverymethod;
	private double totalcost;
	private double finalcost;
	private String dDate;
	// use of array for time slots
	private String[] time;
	private int timeSelected;
	
	public order()
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<5;j++)
			{
				books = new double[i][j];
			}
		}
		
		// default book titles
		booktitles = new String[5];
		booktitles[0] = "Scottish Fiction";
		booktitles[1] = "Scottish Fiction";
		booktitles[2] = "Contemporary Arts";
		booktitles[3] = "Wise Cracks";
		booktitles[4] = "Fiction";
		
		deliverymethod = 0;
		
		time = new String[6];
		time[0] = "";
		time[1] = "9.00 am";
		time[2] = "1.00 pm";
		time[3] = "3.00 pm";
		time[4] = "5.00 pm";
		time[5] = "8.00 pm";
		
		timeSelected = 0;
	}
	
	private void setDefaultPrices()
	{
		// book prices are located at [x][y] y = 0 and 2
		books[0][0] = 10;
		books[0][2] = 8;
		books[1][0] = 11;
		books[1][2] = 9;
		books[2][0] = 10;
		books[2][2] = 10;
		books[3][0] = 12;
		books[3][2] = 8;
		books[4][0] = 10;
		books[4][2] = 9;
	}
	
	public void setQty(int bookno,int booktype,int qty)
	{
		books[bookno][booktype] = qty;
	}
	
	public int getQty(int bookno,int booktype)
	{
		// casting from double to int
		return (int)books[bookno][booktype];
	}
	
	public double getPrice(int bookno,int booktype)
	{
		setDefaultPrices();
		return books[bookno][booktype];
	}
	
	public String getTotal()
	{
		// get total number of books
		return Integer.toString((int)(books[0][1] + books[0][3] + 
									  books[1][1] + books[1][3] + 
									  books[2][1] + books[2][3] + 
									  books[3][1] + books[3][3] + 
									  books[4][1] + books[4][3]));
		 
	}
	
	public double getCost()
	{
		// total cost = sum of (book quantity * book cost)
		totalcost  = books[0][1] * books[0][0];
		totalcost += books[0][3] * books[0][2];
		totalcost += books[1][1] * books[1][0];
		totalcost += books[1][3] * books[1][2];
		totalcost += books[2][1] * books[2][0];
		totalcost += books[2][3] * books[2][2];
		totalcost += books[3][1] * books[3][0];
		totalcost += books[3][3] * books[3][2];
		totalcost += books[4][1] * books[4][0];
		totalcost += books[4][3] * books[4][2];
		
		return totalcost;
	}
	
	public void setFinalCost(double discount, double delivery)
	{
		finalcost = totalcost - (totalcost*discount) + delivery;
	}
	
	public double getFinalCost()
	{
		return finalcost;
	}
	
	public String getBookTitle(int i)
	{
		return booktitles[i];
	}
	
	public void setDeliveryMethod(int order)
	{
		deliverymethod = order;
	}
	public int getDeliveryMethod()
	{
		return deliverymethod;
	}
	public String getDeliveryMethodString()
	{
		// return type of delivery method for books and ebooks
		switch(deliverymethod)
		{
			case 1: return "SELF-COLLECT";
			case 2: return "BY POST";
			case 3: return "SELF-COLLECT & EMAIL";
			case 4: return "BY POST & EMAIL";
			case 5: return "EMAIL";
			default: return "";
		}
	}
	public int getNumofEbooks()
	{
		// return total number of ebooks
		return ((int)(books[0][3] +  books[1][3] + 
				   	  books[2][3] +  books[3][3] + 
				   	  books[4][3]));
	}
	public int getNumofHbooks()
	{
		// return total number of hardcover books
		return ((int)(books[0][1] +  books[1][1] + 
				   	  books[2][1] +  books[3][1] + 
				   	  books[4][1]));
	}
	
	public void setdDate(String date)
	{
		dDate = date;
	}
	public String getdDate()
	{
		return dDate;
	}
	
	public String getTime(int i)
	{
		// return time allocated
		return time[i];
	}
	public void setTime(int i)
	{
		timeSelected = i;
	}
	public int getSelectedTime()
	{
		return timeSelected;
	}
}
