// Name: Garcia Anthony John Abril
// ID: 4321819

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Chord
{
	private static TreeMap <Long,Peer> Chord;
	private static long N;
	private static long zero = 0L;
	private static Long ftSize;
	private static BufferedWriter out;
	private static boolean NICE_FINGER_TABLE_VIEW = false;
	
	public static void main(String[] args)
	{
		try
		{
			Long startTime = System.currentTimeMillis();
			String filename = "";
			if(args.length == 1)
			{
				filename = args[0];
			}
			else
			{
				System.out.println("*******************************************************************");
				System.out.println("*                       Invalid Arguments                         *");
				System.out.println("*              Arguments example : Chord data.txt                 *");
				System.out.println("*******************************************************************");
			}

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), "ASCII"), 512);
			Read(filename);
			Long endTime = System.currentTimeMillis();
			Long totalTime = endTime - startTime;
			System.out.println("\n" + totalTime + "ms");
		}
		catch (Exception e)
		{
		}
		
	}

	private static void Init(long n) throws IOException
	{
		Chord = new TreeMap <Long,Peer>();
		out.write("Name: Garcia Anthony John Abril" + "\n");
		out.write("ID: 4321819" + "\n");
	
		ftSize = getFingerTableSize(n);
		N = n;
		Peer temp = new Peer(zero,ftSize);
		temp.setpredecessor(zero);
		Chord.put(zero, temp);
	}
	
	private static Long getFingerTableSize(long n)
	{
		// maxsize = 2^31
		Long maxSize = 2147483648L;
		
		for (Long s = zero; ; s++)
		{
			Long powertwo = 1L<<s;
			
			if (powertwo >= n)
				return s;
			else if(powertwo >= maxSize)
				return powertwo;
		}
	}
	
	private static void AddPeer(Long ID) throws IOException
	{
		if(Chord.containsKey(ID))
			return;
		Peer temp = new Peer(ID,ftSize);
		Chord.put(ID, temp);
		
		if(Chord.lowerEntry(ID).getValue() != null && Chord.higherEntry(ID) != null)
		{
			HashMap<String,Long> dataTable;
			Peer predecessor = Chord.lowerEntry(ID).getValue();
			
			Peer nextPeer = Chord.higherEntry(ID).getValue();			
			dataTable = nextPeer.getData();

			
			if(!dataTable.isEmpty() && !Chord.isEmpty() && nextPeer!=Chord.get(ID))
			{
				UpdateFTs();
				LinkedList <String> temp1 = new LinkedList <String>();
				for (Map.Entry<String, Long> entry : dataTable.entrySet())
			    {		      
			        if(predecessor.isBetween(entry.getValue(), Chord.get(ID)))
			        {
			        	Insert(ID, entry.getKey(), false);
//			        	Chord.get(ID).insertDT(entry.getValue(), entry.getKey());
			        	temp1.add(entry.getKey());
			        }
				}
				
				for(int i=0; i< temp1.size(); i++)
					Chord.get(nextPeer.getPeerNum()).deleteDT(temp1.get(i));
			}
		}
		out.write("PEER " + ID + " inserted" + "\n");
	}
	
	private static void UpdateFTs() throws IOException
	{
		if(Chord.size() != 0)
		{
			SortedSet<Long> keys = new TreeSet<Long>(Chord.keySet());
			for (Long key : keys)
			{
				out.flush();
				Chord.get(key).setpredecessor(Chord.lowerKey(key));
				Chord.get(key).updateFT(Chord,N);
				
				if (NICE_FINGER_TABLE_VIEW)
				{
					Chord.get(key).printFT(out);
					out.flush();
				}
			}
		}
	}

	
	private static void RemovePeer(Long ID) throws IOException
	{
		if(!Chord.containsKey(ID))
			return;
		HashMap<String,Long> dataTable;
		dataTable = Chord.get(ID).getData();
		
		Chord.remove(ID);
			
		if(dataTable.size() != 0 && Chord.size() != 0)
		{
			UpdateFTs();
			Iterator<Entry<String, Long>> it = dataTable.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		  
		        Entry<String, Long> pairs = (Entry<String, Long>)it.next();
		        if(Chord.higherKey(ID) == null)
		        	Chord.firstEntry().getValue().insertDT(pairs.getValue(), pairs.getKey());
		        else
		        	Chord.get(Chord.higherKey(ID)).insertDT(pairs.getValue(), pairs.getKey());
		        it.remove(); // avoids a ConcurrentModificationException
		    }			
		}
		
	
		if(Chord.size() == 0)
			out.write("The CHORD is now empty\n");
		else
			out.write("PEER " + ID + " removed" + "\n");
	}
	
	private static Long Find(Long ID, Long key, boolean print) throws IOException
	{
		if(!Chord.containsKey(ID))
			ID = zero;
		if (print)
		{
			out.write("Path: " + ID);
			out.flush();
		}
		if(ID == key)
		{	
			return ID;
		}
		else
		{
			Long nextPeerNum;
			Peer tempPeer = Chord.get(ID);
			boolean found = false;
			nextPeerNum = tempPeer.findInFT(key,out);
			do
			{
				if(key == nextPeerNum)
				{
					if(print)
					{
						out.write(">" + nextPeerNum);
						out.flush();
					}
					return nextPeerNum;
				}
				if (tempPeer.getPeerNum() == nextPeerNum)
				{
					if(print)
					{
						out.write(">" + tempPeer.getPeerNum());
						out.flush();
					}
					return tempPeer.getPeerNum();
				}
				if(print)
				{
					out.write(">" + nextPeerNum);
					out.flush();
				}
				if (tempPeer.isBetween(key, Chord.get(nextPeerNum)))
					return nextPeerNum;
				
				tempPeer = Chord.get(nextPeerNum);
				nextPeerNum = tempPeer.findInFT(key,out);
				
			}while(!found);
			
			return tempPeer.getPeerNum();
		}	
	}
	
	
	private static Long Hash(String s)
	{
		long key = 0;
		for(int i=0; i<s.length(); i++)
		{
			key = ((key << 5) + key) ^ s.charAt(i);
		}
		return key;
	}
	
	private static void Insert(Long ID, String s, boolean print) throws IOException
	{
		if(Chord.size() != 0)
		{
				Long x = (Hash(s) & (N-1));
				if(print)
				{
					out.write("INSERTING " + s + " AT " + x + "\n");
					out.flush();
				}
				Long y = Find(ID,x,print);
				if(print)
					out.write("\n");
				Chord.get(y).insertDT(x, s);
		}
	}
	
	private static void Delete(Long ID, String s, boolean print) throws IOException
	{
		Long x = (Hash(s) & (N-1));
		if(print)
		{
			out.write("REMOVED " + s + " FROM " + x + "\n");
			out.flush();
		}
		Long y = Find(ID,x,true);
		if(print)
			out.write("\n");
		Chord.get(y).deleteDT(s);
	}
	
	private static void Print(Long key) throws IOException
	{
		Chord.get(key).printData(out);
		Chord.get(key).printFingerTable(out);
	}
	
	private static void Read(String filename)
	{
		try
		{
			BufferedReader readfile = new BufferedReader(new FileReader(new File(filename)));
			
			String temp = readfile.readLine();
			String [] data;
			
			while(temp!=null)
			{
				data = temp.split(" ");
				
				switch(data[0])
				{
					
					case "init": 
								if(data.length > 1)
									Init(Long.parseLong(data[1]));
								else
								{
									long temp2 = N;
									Init(temp2);
								}
								break;
								
					case "addpeer": 
								AddPeer(Long.parseLong(data[1]));
									
								break;
								
					case "removepeer": 
								RemovePeer(Long.parseLong(data[1]));
								break;
								
					case "insert":
								UpdateFTs();
								String s = "";
								for(int i=2; i<(data.length - 1); i++)
								{
									s = s + data[i] + " ";
								}
								s = s + data[data.length-1];
								Insert(Long.parseLong(data[1]),s, true);
								break;
								
					case "delete":
								UpdateFTs();
								s = "";
								for(int i=2; i<(data.length - 1); i++)
								{
									s = s + data[i] + " ";
								}
								s = s + data[data.length-1];
								Delete(Long.parseLong(data[1]),s,true);
								break;
								
					case "print":
								UpdateFTs();
								out.write("\n");
								Print(Long.parseLong(data[1]));
								out.write("\n");
								break;
					default: 
				}
				
				out.flush();
				
				temp = readfile.readLine();
				
			}
			readfile.close();
		}
		catch(Exception e)
		{
		
		}
	}
}
