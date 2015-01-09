
// Taken from http://www.theaccents.org/ijacr/papers/current_june_2014/45.pdf
// and http://stackoverflow.com/questions/20001772/implementation-of-rc4-encryption-and-url-encoding-in-objective-c

import java.security.*;
import java.util.Arrays;

public class RC4
{
	private char[] key;
	private int[] sbox;
	private final int SBOX_LENGTH = 256;
	//private static final int KEY_MIN_LENGTH = 1;
	
	static byte[] trim(byte[] bytes)
	{
		int i = bytes.length - 1;
		while (i >= 0 && bytes[i] == 0)
		{
			--i;
		}

		return Arrays.copyOf(bytes, i + 1);
	}

	public RC4(String key) throws InvalidKeyException
	{
		setKey(key);
	}

	public RC4()
	{
	}

	public String decrypt(final char[] msg)
	{
		return (encrypt(msg));
	}

	public String encrypt(final char[] msg)
	{
		sbox = initSBox(key);
		char[] code = new char[msg.length];
		int i = 0;
		int j = 0;
		for (int n = 0; n < msg.length; n++)
		{
			i = (i + 1) % SBOX_LENGTH;
			j = (j + sbox[i]) % SBOX_LENGTH;
			swap(i, j, sbox);
			int rand = sbox[(sbox[i] + sbox[j]) % SBOX_LENGTH];
			code[n] = (char) (rand ^ (int) msg[n]);
		}
		return new String(code);
	}

	private int[] initSBox(char[] key)
	{
		int[] sbox = new int[SBOX_LENGTH];
		int j = 0;

		for (int i = 0; i < SBOX_LENGTH; i++)
		{
			sbox[i] = i;
		}

		for (int i = 0; i < SBOX_LENGTH; i++)
		{
			j = (j + sbox[i] + key[i % key.length]) % SBOX_LENGTH;
			swap(i, j, sbox);
		}
		return sbox;
	}

	private void swap(int i, int j, int[] sbox)
	{
		int temp = sbox[i];
		sbox[i] = sbox[j];
		sbox[j] = temp;
	}

	public void setKey(String key) throws InvalidKeyException
	{
		this.key = key.toCharArray();
	}

}
