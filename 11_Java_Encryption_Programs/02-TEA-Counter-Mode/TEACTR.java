/* Done by Garcia Anthony John Abril */
/* Student ID 4321819 */
/* Referenced from http://derekwilliams.us/docs/CPSC-6128-TEA-Encryption.pdf */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class TEACTR
{
	private static int padding;
	private BigInteger key;
	private static Scanner sc;
	private static String wholeText;
	private static BigInteger IV;

	public static void main(String[] args)
	{
		if(args.length == 2)
		{
			
			try
			{
				sc = new Scanner(System.in);
				if(args[0].equals("-e"))
				{
					System.out.print("Enter the file name of the plaintext = ");
					String fileName = sc.nextLine();
					wholeText = readFile(fileName,false);
					
					
					BigInteger n = generateNonce(16);
					writeFile(args[1],n.toString());
					System.out.println("Key generated: " + n.toString() + " and is stored in " + args[1]);
					
					IV = generateNonce(8);
					writeFile("IV.txt",IV.toString());
					System.out.println("IV generated: " + IV.toString());
					
					System.out.print("Enter the file name of the ciphertext = ");
					fileName = sc.nextLine();
					
					TEACTR tea = new TEACTR(n);
					int[] res = tea.preEncrypt(wholeText);
					writeResultToFile(fileName,res);
						
					System.out.println("Encryption completed");
				}
				else if(args[0].equals("-d"))
				{
					wholeText = readFile(args[1],true);
					BigInteger keyInt = new BigInteger(wholeText);
					TEACTR tea = new TEACTR(keyInt);
					
					wholeText = readFile("IV.txt",true);
					IV = new BigInteger(wholeText);
					
					
					System.out.print("Enter the file name of the ciphertext = ");
					String fileName = sc.nextLine();
					int[] res = readResultFromFile(fileName);
					
					System.out.print("Enter the file name of the plaintext = ");
					fileName = sc.nextLine();
					
					wholeText = tea.preDecrypt(res);
					writeFile(fileName,wholeText);
					
					System.out.println("Decryption completed");
				}
				else
				{
					System.out.println("Usage: ");
					System.out.println("-e keyfile");
					System.out.println("\t To start encryption");
					System.out.println("-d keyfile");
					System.out.println("\t To start decryption");
				}
				
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				sc.close();
			}
		}
		else 
		{
			System.out.println("Usage: ");
			System.out.println("-e keyfile");
			System.out.println("\t To start encryption");
			System.out.println("-d keyfile");
			System.out.println("\t To start decryption");
		}
		
	}
	
	private static int[] readResultFromFile(String fileName) throws IOException, NumberFormatException
	{
		String temp = readFile(fileName,true);
		String[] resStr = temp.split("#");
		int[] res = new int[resStr.length];
		
		for(int i=0;i<resStr.length;i++)
		{
			res[i] = Integer.parseInt(resStr[i]);
		}
		return res;
	}
	
	private static String readFile(String fileName, boolean isKey) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line);
				line = br.readLine();
				if(!isKey && line != null)
					sb.append("\n");
			}
			return sb.toString();
		}
		finally
		{
			br.close();
		}
	}
	
	private static void writeResultToFile(String fileName, int [] res) throws IOException
	{
		// we use a seperator for the array of integers to be properly identified
		BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
		try
		{
			for(int i=0;i<res.length;i++)
			{
				br.write(res[i] + "#");
			}
		}
		finally
		{
			br.close();
		}
	}
	
	private static void writeFile(String fileName, String wholeText) throws IOException
	{
		BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
		try
		{
			br.write(wholeText);
		}
		finally
		{
			br.close();
		}
	}

	TEACTR(BigInteger k)
	{
		key = k;
	}

	private int[] generateKey()
	{
		byte[] keyBytes = key.toString(16).getBytes();
		int keyLen = keyBytes.length;
		int[] key = new int[4];

		// here we split the key into 4 parts where each part contains 4 bytes combined
		for (int i = 0, j = 0; j < keyLen; j += 4, i++)
		{
			key[i] = (keyBytes[j] << 24) | (((keyBytes[j + 1]) & 0xff) << 16) | (((keyBytes[j + 2]) & 0xff) << 8)
					| ((keyBytes[j + 3]) & 0xff);
		}

		return key;
	}

	private static BigInteger generateNonce(int byteSize)
	{
		SecureRandom random = new SecureRandom();
		BigInteger nonce = new BigInteger(byteSize*4, random);
		while((nonce.toString(16).length()!=byteSize))
		{
			nonce = new BigInteger(byteSize*4, random);
		}
		
		return nonce;
	}
	private int[] preEncrypt(String str) throws UnsupportedEncodingException
	{
		str = addPadding(str);
		int strLen = str.length();
		byte[] v = str.getBytes("UTF-8");
		int[] keyIntArr = generateKey();
		int[] res = new int[strLen];
		
		
		for (int i = 0; i < strLen; i += 8)
		{
			int[] IVarr = new int[2];
			byte[] IVBytes = IV.toString(16).getBytes("UTF-8");
			
			// Split IV into two 4 byte array
			IVarr[0] = (IVBytes[0] << 24) | (((IVBytes[1]) & 0xff) << 16) | (((IVBytes[2]) & 0xff) << 8) | ((IVBytes[3]) & 0xff);
			IVarr[1] = (IVBytes[4] << 24) | (((IVBytes[5]) & 0xff) << 16) | (((IVBytes[6]) & 0xff) << 8) | ((IVBytes[7]) & 0xff);
			
			// Encrypt IV
			encrypt(IVarr, keyIntArr);
			IVBytes = putBackToBytes(IVarr);
			
			// Plaintext XOR IV
			res[i] = v[i]^IVBytes[0];
			res[i+1] = v[i + 1]^IVBytes[1];
			res[i+2] = v[i + 2]^IVBytes[2];
			res[i+3] = v[i + 3]^IVBytes[3];
			res[i+4] = v[i + 4]^IVBytes[4];
			res[i+5] = v[i + 5]^IVBytes[5];
			res[i+6] = v[i + 6]^IVBytes[6];
			res[i+7] = v[i + 7]^IVBytes[7];
			
			// Increment IV
			IV = IV.add(BigInteger.ONE);
		}
		
		return res;
	}
	

	
	private String preDecrypt(int[] INTarr) throws UnsupportedEncodingException
	{
		int intCount = INTarr.length;
		byte res[] = new byte[intCount * 4];
		int[] keyIntArr = generateKey();
		
		for (int i = 0; i < intCount; i += 8)
		{
			int[] IVarr = new int[2];
			byte[] IVBytes = IV.toString(16).getBytes("UTF-8");
			
			// Split IV into two 4 byte array
			IVarr[0] = (IVBytes[0] << 24) | (((IVBytes[1]) & 0xff) << 16) | (((IVBytes[2]) & 0xff) << 8) | ((IVBytes[3]) & 0xff);
			IVarr[1] = (IVBytes[4] << 24) | (((IVBytes[5]) & 0xff) << 16) | (((IVBytes[6]) & 0xff) << 8) | ((IVBytes[7]) & 0xff);
			
			// Encrypt IV
			encrypt(IVarr, keyIntArr);
			IVBytes = putBackToBytes(IVarr);
			
			// Ciphertext XOR IV
			res[i] = (byte) (INTarr[i]^IVBytes[0]);
			res[i+1] = (byte) (INTarr[i + 1]^IVBytes[1]);
			res[i+2] = (byte) (INTarr[i + 2]^IVBytes[2]);
			res[i+3] = (byte) (INTarr[i + 3]^IVBytes[3]);
			res[i+4] = (byte) (INTarr[i + 4]^IVBytes[4]);
			res[i+5] = (byte) (INTarr[i + 5]^IVBytes[5]);
			res[i+6] = (byte) (INTarr[i + 6]^IVBytes[6]);
			res[i+7] = (byte) (INTarr[i + 7]^IVBytes[7]);
			
			// Increment IV
			IV = IV.add(BigInteger.ONE);
		}
		
		String str = new String(res,"UTF-8");
		str = removePadding(str);
		return str;
	}
	
	private byte[] putBackToBytes(int[] INTarr) throws UnsupportedEncodingException
	{
		int intCount = INTarr.length;
		int tmp[] = new int[2];
		byte res[] = new byte[intCount * 4];
		
		for (int j = 0, i = 0; i < intCount; i += 2, j += 8)
		{
			tmp[0] = INTarr[i];
			tmp[1] = INTarr[i + 1];
			
			// put back the bytes in order from the two int array
			res[j] = (byte) (tmp[0] >>> 24);
			res[j + 1] = (byte) (tmp[0] >>> 16);
			res[j + 2] = (byte) (tmp[0] >>> 8);
			res[j + 3] = (byte) (tmp[0]);
			res[j + 4] = (byte) (tmp[1] >>> 24);
			res[j + 5] = (byte) (tmp[1] >>> 16);
			res[j + 6] = (byte) (tmp[1] >>> 8);
			res[j + 7] = (byte) (tmp[1]);
		}
		return res;
	}

	private void encrypt(int[] v, int[] k)
	{
		int v0 = v[0], v1 = v[1], sum = 0, i; /* set up */
		int delta = 0x9e3779b9; /* a key schedule constant */
		int k0 = k[0], k1 = k[1], k2 = k[2], k3 = k[3]; /* cache key */
		for (i = 0; i < 32; i++)
		{
			/* basic cycle start */
			sum += delta;
			v0 += ((v1 << 4) + k0) ^ (v1 + sum) ^ ((v1 >> 5) + k1);
			v1 += ((v0 << 4) + k2) ^ (v0 + sum) ^ ((v0 >> 5) + k3);
		}
		/* end cycle */
		v[0] = v0;
		v[1] = v1;
	}

	// Decrypt is not used as only encryption of IV is needed for CTR mode
//	private void decrypt(int[] v, int[] k)
//	{
//		int v0 = v[0], v1 = v[1], sum = 0xC6EF3720, i; /* set up */
//		int delta = 0x9e3779b9; /* a key schedule constant */
//		int k0 = k[0], k1 = k[1], k2 = k[2], k3 = k[3]; /* cache key */
//		for (i = 0; i < 32; i++)
//		{ /* basic cycle start */
//			v1 -= ((v0 << 4) + k2) ^ (v0 + sum) ^ ((v0 >> 5) + k3);
//			v0 -= ((v1 << 4) + k0) ^ (v1 + sum) ^ ((v1 >> 5) + k1);
//			sum -= delta;
//		} /* end cycle */
//		v[0] = v0;
//		v[1] = v1;
//	}
	
	private static String addPadding(String str)
	{
		int strLen = str.length();

		// adds padding if the number of chars aren't a multiple of 8
		padding = strLen % 8;
		if (padding != 0)
		{
			for (int i = 0; i < 8-padding; i++)
			{
				// + is used as character padding
				str += "+";
			}
		}
		
		return str;
	}
	
	private String removePadding(String str)
	{
		for(int i=str.length()-1;i>=0;i--)
		{
			if(str.charAt(i) == '+')
				str = str.substring(0,i);
		}
		return str;
	}

}