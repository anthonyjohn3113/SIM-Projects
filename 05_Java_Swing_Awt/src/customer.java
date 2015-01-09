/*
CSCI213 Assignment 2
-----------------------------------------
File name: customer.java
Author: Garcia Anthony John Abril
Registration Number: 4321819
Description: customer class to store customer details
 */

import java.util.regex.Pattern;


public class customer 
{
	private String name;
	private String uname;
	private String pwd;
	private String address;
	private int phoneNum;
	private String email;
	
	public boolean setName(String nme)
	{
		if(nme.length()<3)
		{
			return false;
		}
		else
		{
			name = nme;
			return true;
		}
	}
	public String getName()
	{
		return name;
	}
	// function for setting username
	public boolean setUName(String nme)
	{
		nme = nme.toLowerCase();
		if(nme.length()<3)
		{
			return false;
		}
		else
		{
			uname = nme;
			return true;
		}
	}
	public String getUName()
	{
		return uname;
	}
	// function for setting password
	public boolean setPwd(String pw)
	{
		if(pw.length()<3)
		{
			return false;
		}
		else
		{
			pwd = pw;
			return true;
		}
	}
	public String getPwd()
	{
		return pwd;
	}
	// function for setting address
	public boolean setAdd(String add)
	{
		if(add.length()<8)
		{
			return false;
		}
		else
		{
			address = add;
			return true;
		}
	}
	public String getAdd()
	{
		return address;
	}
	// function for setting phone number
	public boolean setPNum(String num)
	{
		if(num.length()<4)
			return false;
		try
		{
			phoneNum = Integer.parseInt(num);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	public int getPNum()
	{
		return phoneNum;
	}
	
	public boolean setEmail(String data)
	{
		if (!rfc2822.matcher(data).matches())
			return false;
		else
		{
			email = data;
			return true;
		}
	}
	
	// default pattern check for valid email
	private static final 
	Pattern rfc2822 = Pattern.compile
	("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
	
	public String getEmail()
	{
		return email;
	}

}
