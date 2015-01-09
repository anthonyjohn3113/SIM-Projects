// Name: Garcia Anthony John Abril
// ID: 4321819

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Peer
{
	private Long peerNum;
	private Long ftSize;
	private Long predecessor;
	private Long N;
	private HashMap<String,Long> dataTable;
	private TreeMap <Long,Long> fingerTable;

	Peer(Long num,Long size)
	{
		peerNum = num;
		ftSize = size;
		fingerTable = new TreeMap<Long,Long>();
		dataTable = new HashMap<String,Long>();
	}
	
	Long succ(Long key,TreeMap <Long,Peer> Chord)
	{
		if(Chord.containsKey(key))
			return key;
		else if(Chord.higherKey(key) == null)
			return Chord.firstKey();
		else
			return Chord.higherKey(key);
	}
	
	void updateFT(TreeMap<Long, Peer> Chord, Long N)
	{
		this.N = N;
		for(int i=0; i<ftSize; i++)
		{
			Long key = peerNum + powertwo(i);	
			if(key >= N)
				key = (Long) (key & (N - 1));	
			
			Long value = succ(key,Chord);
			fingerTable.put(key, value);
		}
	}

	int powertwo(int x) 
	{
		return (1 << x);
	}

	void printFT(BufferedWriter out) throws IOException
	{
		out.write("FT for Peer: " + peerNum + "\n");
		Iterator<Entry<Long, Long>> it = fingerTable.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Entry<Long, Long> pairs = (Entry<Long, Long>)it.next();
	        out.write("Key: " + pairs.getKey()  + "\tValue: " + pairs.getValue()  + "\n");
	        it.remove(); 
	    }
		out.write("Predecessor for Peer" + peerNum + ": " + predecessor + "\n");
		out.flush();
	}
	
	void insertDT(Long x, String data) throws IOException
	{
		dataTable.put(data,x);
	}
	
	void deleteDT(String data)
	{
		dataTable.remove(data);
	}

	Long findInFT(Long id, BufferedWriter out) throws IOException 
	{
		Long temp =  fingerTable.lastEntry().getValue();
		if(id == peerNum)
			return peerNum;
		if (id < peerNum)
		{
			if (id > predecessor)
				return peerNum;
			else if (id == predecessor)
				return predecessor;
		}
		Iterator<Entry<Long, Long>> it = fingerTable.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Entry<Long, Long> pairs = (Entry<Long, Long>)it.next();
	        
	        if(pairs.getKey()  == id)
	        	return pairs.getValue();
	        
	        else if(pairs.getValue() == id)
	        	return pairs.getValue();
	        
	        else if(pairs.getKey() > id)
	        	return temp;
	        
	        temp = pairs.getValue();
	        
	        it.remove(); 
	    }
	    
		return temp;
	}
	
	boolean isBetween(long key, Peer nextPeer)
	{
		Long nextPeerNum = nextPeer.getPeerNum();
		if (nextPeerNum < peerNum)
			nextPeerNum += N;

		if (peerNum < key && key <= nextPeerNum)
			return true;
		else
			return false;		
	}
	void printData(BufferedWriter out) throws IOException
	{
		out.write("DATA AT INDEX NODE " +  peerNum + ": \n");
		Iterator<Entry<String, Long>> it = dataTable.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Entry<String, Long> pairs = (Entry<String, Long>)it.next();
	        out.write("Key: " + pairs.getValue() + "\t Data: " + pairs.getKey() + "\n");
//	        it.remove();
	    }
	    out.flush();
	}
	void printFingerTable(BufferedWriter out) throws IOException
	{
		out.write("FINGER TABLE OF NODE " +  peerNum + ": \n");
		Iterator<Entry<Long, Long>> it = fingerTable.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Entry<Long, Long> pairs = (Entry<Long, Long>)it.next();
	        out.write(pairs.getValue() + " ");
//	        it.remove(); 
	    }	
		out.write("\n");
		out.flush();
	}
	
	Long getPeerNum()
	{
		return peerNum;
	}
	
	HashMap<String,Long> getData()
	{
		return this.dataTable;
	}

	void setpredecessor(Long x)
	{
		predecessor = x;
	}
	
	Long getpredecessor()
	{
		return predecessor;
	}
}
