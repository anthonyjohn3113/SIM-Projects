// Done by Garcia Anthony John Abril
// Student ID 4321819

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class Hill
{
	private static BufferedWriter bw;
	private static Scanner sc;
	private static final String keyFile = "keyFile.txt";
	private static final String cipherFile = "ciphertext.txt";
	private static final String plainFile = "plaintext.txt";
	
	public static final char[] letters = {	'a','b','c','d','e','f','g',
									'h','i','j','k','l','m',
									'n','o','p','q','r','s',
									't','u','v','w','x','y',
									'z','.','?','\t','\n',' '};
	
	public static void main(String args[]) throws NoSquareException
	{
		sc = new Scanner(System.in);
		String choice = "";

		try
		{
			do
			{
				System.out.println("Welcome to hill cipher system");
				System.out.println("1. Key Generation");
				System.out.println("2. Encrypt File");
				System.out.println("3. Decrypt File");
				System.out.println("4. Check difference between Decrypted Plaintext and Original Plaintext");
				System.out.println("5. Exit");
				System.out.print("Choice: ");
				choice = sc.nextLine();
				
				String fileName = "";
				String wholeText = "";
				
				System.out.println();
				
				switch(choice.charAt(0))
				{
					case '1':
								File kfile = new File(keyFile);
								if (!kfile.exists()) 
								{
									generateKey(getKeySize(),kfile);
									System.out.println("\nKey stored in " + keyFile);
								}
								else
								{
									System.out.print(keyFile + " already exists. Create new key? (y/n): ");
									if(sc.nextLine().equals("y"))
									{
										generateKey(getKeySize(),kfile);
										System.out.println("Key stored in " + keyFile);
									}
								}
								
								break;
								
					case '2':
								System.out.print("Enter filename to be encrypted: ");
								fileName = sc.nextLine();
//								fileName = "English.txt";
								wholeText = readFile(fileName);
								
								wholeText = wholeText.replaceAll("[^a-zA-Z.?\\n\\t ]", "");
								wholeText = wholeText.toLowerCase();
								
								encrypt(wholeText);
								System.out.println("\nText has been encrypted and written to " + cipherFile);
								break;
								
					case '3':
								System.out.print("Enter filename to be decrypted: ");
								fileName = sc.nextLine();
//								fileName = cipherFile;
								wholeText = readFile(fileName);

								decrypt(wholeText);
								break;
								
					case '4':
								System.out.print("Enter filename of original plaintext: ");
								fileName = sc.nextLine();
//								fileName = "English.txt";
								wholeText = readFile(fileName);
								wholeText = wholeText.replaceAll("[^a-zA-Z.?\\n\\t ]", "");
								wholeText = wholeText.toLowerCase();
								
								String plainText = readFile(plainFile);
								
								if(diff(wholeText,plainText))
									System.out.println("\nBoth file matches!");
								else
									System.out.println("\nBoth file DO NOT match!");
								
								System.out.println("\nOriginal plaintext:");
								System.out.println(wholeText);
								
								System.out.println("\nDecrypted plaintext:");
								System.out.println(plainText);
				}
				
				System.out.println();
				
			}while(!choice.equals("5"));
		}
		catch(Exception e)
		{
//			e.printStackTrace();
			System.out.println("Error! " + e.getMessage());
		}
		
		
	}
	
	private static void encrypt(String wholeText) throws IOException, NoSquareException
	{
		String sMatrix = readKeyFile(keyFile);
		String[] sKeys = sMatrix.split(" ");
		int N = (int)Math.sqrt(sKeys.length);

		Matrix keyMatrix = new Matrix(N,N);
		
		int k=0;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				keyMatrix.setValueAt(i, j, Integer.parseInt(sKeys[k]));
				k++;
			}
		}
		
		String cipherText = "";
		Matrix tempMatrix = new Matrix(N,1);
		k=0;
		while(k<wholeText.length())
		{
			for(int i=0;i<N;i++)
			{
				int letterValue = 0;
				
				if(k<wholeText.length())
					letterValue = getLetterValue(wholeText.charAt(k));
				
				tempMatrix.setValueAt(i, 0, letterValue);
				k++;
			}
			Matrix resultMatrix = MatrixMathematics.multiply(keyMatrix, tempMatrix);
			resultMatrix = resultMatrix.modulusByConstant(letters.length);
			cipherText += getStringFromMatrix(resultMatrix);
		}
		
		cipherText = wholeText.length() + "\n" + cipherText;
		writeFile(cipherFile,cipherText);
	}
	
	private static void decrypt(String wholeText) throws IOException, NoSquareException
	{
		String[] temp = wholeText.split("\n");
		int totalChars = Integer.parseInt(temp[0]);
		wholeText = wholeText.substring(temp[0].length()+1,wholeText.length());
		
		String sMatrix = readKeyFile(keyFile);
		String[] sKeys = sMatrix.split(" ");
		int N = (int)Math.sqrt(sKeys.length);
		
		Matrix keyMatrix = new Matrix(N,N);
		
		int k=0;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				keyMatrix.setValueAt(i, j, Integer.parseInt(sKeys[k]));
				k++;
			}
		}
		
		keyMatrix = MatrixMathematics.inverse(keyMatrix);
	
		String plainText = "";
		Matrix tempMatrix = new Matrix(N,1);
		k=0;
		while(k<wholeText.length())
		{
			for(int i=0;i<N;i++)
			{
				int letterValue = 0;
				
				if(k<wholeText.length())
					letterValue = getLetterValue(wholeText.charAt(k));
				
				tempMatrix.setValueAt(i, 0, letterValue);		
				k++;
			}
			Matrix resultMatrix = MatrixMathematics.multiply(keyMatrix, tempMatrix);
			resultMatrix = resultMatrix.modulusByConstant(letters.length);
			
			plainText += getStringFromMatrix(resultMatrix);
		}
		
		plainText = plainText.substring(0,totalChars);
		System.out.println("\nDecrypted plaintext:");
		System.out.println(plainText);
		writeFile(plainFile,plainText);
	}
	
	private static String getStringFromMatrix(Matrix matrix)
	{
		String result = "";
		
		for(int i=0;i<matrix.getNrows();i++)
		{
			for(int j=0; j<matrix.getNcols();j++)
			{
				result += letters[matrix.getValueAt(i, j)];
			}
		}
		
		return result;
	}
	
	private static int getLetterValue(char letter)
	{
		for(int i=0;i<letters.length;i++)
		{
			if (letters[i] == letter)
				return i;
		}
		return 0;
	}
	
	private static String readKeyFile(String fileName) throws IOException
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
			}
			return sb.toString();
		}
		finally
		{
			br.close();
		}
	}
	
	private static String readFile(String fileName) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line + "\n");
				line = br.readLine();
			}
//			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
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
	
	private static int getKeySize()
	{
		System.out.print("Enter value of n key matrix dimension is n x n: ");
		int keySize = 0;
		boolean valid = false;
		do
		{
			try
			{
				keySize = Integer.parseInt(sc.nextLine());
				valid = true;
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("Error! Invalid value.");
			}	
		}while(!valid);
		
		return keySize;
	}
	
	private static void generateKey(int keySize,File file) throws IOException, NoSquareException
	{
		boolean valid = false;
		do
		{
			try
			{
				file.createNewFile();
				Matrix m = new Matrix(keySize,keySize);		
				Random random = new Random();
				
				bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				
				for(int i=0;i<keySize;i++)
				{
					for(int j=0;j<keySize;j++)
					{
						int temp = random.nextInt(letters.length);
						m.setValueAt(i, j, temp);
						bw.write(temp + " ");
					}
					bw.write("\n");
				}
				
				bw.close();
				
				MatrixMathematics.inverse(m);
				valid = true;
			}
			catch (ArithmeticException e)
			{
				valid = false;
			}
		}while(!valid);
		
	}
	
	private static boolean diff(String wholeText, String plainText)
	{
		if(wholeText.length() != plainText.length())
			return false;
		
		for(int i=0;i<plainText.length();i++)
		{
			if(wholeText.charAt(i) != plainText.charAt(i))
				return false;
		}
		
		return true;
	}

}

//Taken from http://www.codeproject.com/Articles/405128/Matrix-operations-in-Java 

class Matrix
{

	private int nrows;
	private int ncols;
	private int[][] data;

	public Matrix(int[][] dat)
	{
		this.data = dat;
		this.nrows = dat.length;
		this.ncols = dat[0].length;
	}

	public Matrix(int nrow, int ncol)
	{
		this.nrows = nrow;
		this.ncols = ncol;
		data = new int[nrow][ncol];
	}

	public int getNrows()
	{
		return nrows;
	}

	public void setNrows(int nrows)
	{
		this.nrows = nrows;
	}

	public int getNcols()
	{
		return ncols;
	}

	public void setNcols(int ncols)
	{
		this.ncols = ncols;
	}

	public int[][] getValues()
	{
		return data;
	}

	public void setValues(int[][] values)
	{
		this.data = values;
	}

	public void setValueAt(int row, int col, int value)
	{
		data[row][col] = value;
	}

	public int getValueAt(int row, int col)
	{
		return data[row][col];
	}

	public boolean isSquare()
	{
		return nrows == ncols;
	}

	public int size()
	{
		if (isSquare())
			return nrows;
		return -1;
	}

	public Matrix multiplyByConstant(int constant)
	{
		Matrix mat = new Matrix(nrows, ncols);
		for (int i = 0; i < nrows; i++)
		{
			for (int j = 0; j < ncols; j++)
			{
				mat.setValueAt(i, j, data[i][j] * constant);
			}
		}
		return mat;
	}
	
	public void printMatrix()
	{
		for (int i = 0; i < nrows; i++)
		{
			for (int j = 0; j < ncols; j++)
			{
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public int totalCount()
	{
		int count = 0;
		for (int i = 0; i < nrows; i++)
		{
			for (int j = 0; j < ncols; j++)
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Added by Garcia Anthony John Abril
	public Matrix modulusByConstant(int constant)
	{
		Matrix mat = new Matrix(nrows, ncols);
		for (int i = 0; i < nrows; i++)
		{
			for (int j = 0; j < ncols; j++)
			{
				if(data[i][j] > 0)
					mat.setValueAt(i, j, data[i][j]%constant);
				else
					mat.setValueAt(i, j, ((data[i][j]%constant)+constant)%constant);
			}
		}
		return mat;
	}
}

class MatrixMathematics
{

	/**
	 * This class a matrix utility class and cannot be instantiated.
	 */
	private MatrixMathematics()
	{
	}

	/**
	 * Transpose of a matrix - Swap the columns with rows
	 * 
	 * @param matrix
	 * @return
	 */
	public static Matrix transpose(Matrix matrix)
	{
		Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
		for (int i = 0; i < matrix.getNrows(); i++)
		{
			for (int j = 0; j < matrix.getNcols(); j++)
			{
				transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
			}
		}
		return transposedMatrix;
	}

	/**
	 * Inverse of a matrix - A-1 * A = I where I is the identity matrix A matrix
	 * that have inverse is called non-singular or invertible. If the matrix
	 * does not have inverse it is called singular. For a singular matrix the
	 * values of the inverted matrix are either NAN or Infinity Only square
	 * matrices have inverse and the following method will throw exception if
	 * the matrix is not square.
	 * 
	 * @param matrix
	 * @return
	 * @throws NoSquareException
	 */
	// Modified by Garcia Anthony John Abril
	public static Matrix inverse(Matrix matrix) throws NoSquareException
	{
		Matrix inverseMatrix = transpose(cofactor(matrix));
		inverseMatrix.modulusByConstant(Hill.letters.length);
		inverseMatrix = inverseMatrix.multiplyByConstant(modularMultiplicativeInverse(determinant(matrix),Hill.letters.length));
		inverseMatrix = inverseMatrix.modulusByConstant(Hill.letters.length);
		return inverseMatrix;
	}
	
	/**
	 * This is to calculate u where a * u = 1 mod m by using the Extended Euclidean Algorithm
	 **/
	// Added by Garcia Anthony John Abril
	public static int modularMultiplicativeInverse(int a,int m)
	{
	    int x = 0,y = 1,u = 1,v = 0; 
	    int e = m,f = a;
	    int c,d,q,r;
	    while(f != 1)
	    {
	        q = e/f;
	        r = e%f;
	        c = x-q*u;
	        d = y-q*v;
	        x = u;
	        y = v;
	        u = c;
	        v = d;
	        e = f;
	        f = r;
	    } 
	    u = (u+m)%m;
	    return u;
	}

	/**
	 * Determinant of a square matrix The following function find the
	 * determinant in a recursively.
	 * 
	 * @param matrix
	 * @return
	 * @throws NoSquareException
	 */
	public static int determinant(Matrix matrix) throws NoSquareException
	{
		if (!matrix.isSquare())
			throw new NoSquareException("matrix need to be square.");
		if (matrix.size() == 1)
		{
			return matrix.getValueAt(0, 0);
		}

		if (matrix.size() == 2)
		{
			return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1))
					- (matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
		}
		int sum = 0;
		for (int i = 0; i < matrix.getNcols(); i++)
		{
			sum += changeSign(i) * matrix.getValueAt(0, i) * determinant(createSubMatrix(matrix, 0, i));
		}
		return sum;
	}

	/**
	 * Determine the sign; i.e. even numbers have sign + and odds -
	 * 
	 * @param i
	 * @return
	 */
	private static int changeSign(int i)
	{
		if (i % 2 == 0)
			return 1;
		return -1;
	}

	/**
	 * Creates a submatrix excluding the given row and column
	 * 
	 * @param matrix
	 * @param excluding_row
	 * @param excluding_col
	 * @return
	 */
	public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col)
	{
		Matrix mat = new Matrix(matrix.getNrows() - 1, matrix.getNcols() - 1);
		int r = -1;
		for (int i = 0; i < matrix.getNrows(); i++)
		{
			if (i == excluding_row)
				continue;
			r++;
			int c = -1;
			for (int j = 0; j < matrix.getNcols(); j++)
			{
				if (j == excluding_col)
					continue;
				mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
			}
		}
		return mat;
	}

	/**
	 * The cofactor of a matrix
	 * 
	 * @param matrix
	 * @return
	 * @throws NoSquareException
	 */
	public static Matrix cofactor(Matrix matrix) throws NoSquareException
	{
		Matrix mat = new Matrix(matrix.getNrows(), matrix.getNcols());
		for (int i = 0; i < matrix.getNrows(); i++)
		{
			for (int j = 0; j < matrix.getNcols(); j++)
			{
				mat.setValueAt(i, j, changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)));
			}
		}

		return mat;
	}

	public static Matrix multiply(Matrix matrix1, Matrix matrix2)
	{
		Matrix multipliedMatrix = new Matrix(matrix1.getNrows(), matrix2.getNcols());

		for (int i = 0; i < multipliedMatrix.getNrows(); i++)
		{
			for (int j = 0; j < multipliedMatrix.getNcols(); j++)
			{
				int sum = 0;
				for (int k = 0; k < matrix1.getNcols(); k++)
				{
					sum += matrix1.getValueAt(i, k) * matrix2.getValueAt(k, j);
				}
				multipliedMatrix.setValueAt(i, j, sum);
			}
		}
		return multipliedMatrix;
	}
}

class IllegalDimensionException extends Exception
{

	public IllegalDimensionException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public IllegalDimensionException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

}









class NoSquareException extends Exception
{

	public NoSquareException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public NoSquareException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

}