import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable4<K, V> implements MapInterface<K, V> {
	protected GenericArray<MapEntry<K, V>> map;
	protected int TABLE_SIZE;
	protected int size;
	protected boolean useNaiveHashCode; // this method of determining hash code function is from chatGPT
	
	
	
	// Constructor
	public HashTable4(int initSize, boolean useNaiveHashCode) {
		size = 0;
		TABLE_SIZE = initSize;
		this.useNaiveHashCode = useNaiveHashCode;
		map = new GenericArray<MapEntry<K, V>>(initSize);
		
		for (int i = 0; i < TABLE_SIZE; i++) {
            map.set(i, null);
        }
	}	
	
	
	/**
	 * get method before I realized I didn't incorporate
	 * the iterator.
	 */
	@Override
	public V get(K k) {	
		// throw exception if try to get null key
		if (k == null) {
			throw new IllegalArgumentException("No null keys");
		}
		
		// get hash corresponding to key
		int hash = calculateHashCode(k);
		
		System.out.println("(Getting value corresponding to key " + k + ", which is in bucket #" + hash + "...)");

		// if map at index hash is empty, return null
		if (map.get(hash) == null) {
			return null;
		
		// otherwise, get entry at index hash
		} else {
			MapEntry<K, V> entry = map.get(hash);
			// if/while the key is not the key that is requested, search
			// the next entry in the chain that is at that index
			while (entry != null && !entry.key.equals(k)) {
				entry = entry.next;
			}
			// return null if no such key exists, or the value of the entry with that key
			if (entry == null) 
				return null; 
			else
				return entry.value;

		}
	}


	
	/*
	 * get bucket at specified index
	 */
	public MapEntry<K, V> getBucket(int bucketIndex) {
		return map.get(bucketIndex);
	}
	
	
	
	/**
	 * Method that puts an entry with a key and value into the map
	 */
	@Override
	public V put(K k, V v) {
		// throw exception attempt to put entry with null key
		if (k == null) {
			throw new IllegalArgumentException("This table does not allow null values");
		}
		
		// get hash corresponding to key
		int hash = calculateHashCode(k);
		
		System.out.println("Putting entry with key " + k + " and value " + v + " into map at index " + hash);
		
		size++;
		
		// get slot in map with index hash
		MapEntry<K, V> entry = map.get(hash);

		// if bucket is empty, set entry to that slot
		if (map.get(hash) == null) {
			map.set(hash, new MapEntry<K, V>(k, v));
		
		// otherwise, iterate through the linked list at the bucket
		// until you arrive at the end of the list, where you set the 
		// next pointer to the new entry
		} else {
			System.out.println("Entry/entries exists at index " + hash + ". Iterating through chain and inserting new entry at end.");
			while (entry.next != null && !entry.key.equals(k)) {
				entry = entry.next;
			}
			
			// if an entry with the same key exists, replace old value
			if (entry.key.equals(k)) {
				V oldValue = entry.value;
				entry.value = v;
				return oldValue;
				
			} else {
				entry.next = new MapEntry<K, V>(k, v);
			}
		}
		
		return null;
	}
		
	
	
	public int calculateHashCode(K k) {
		return useNaiveHashCode ? naiveHashCode(k) : moreComplicatedHashCode(k);
	}
	// SOURCE: debugcode.ai
	private int naiveHashCode(K key) {
		 int hash = 0;
		 String keyString = key.toString();
		 
		 for (int i = 0; i < keyString.length(); i++) {
			 hash += keyString.charAt(i);
		 }
		 return hash % TABLE_SIZE;
	}
	
	
	
	
	// SOURCE: debugcode.ai
	private int moreComplicatedHashCode(K key) {
		int hash = 0;
		String keyString = key.toString();
		
		for (int i =0; i < keyString.length(); i++) {
			hash = (hash << 5) + keyString.charAt(i);
		}
		
		return Math.abs(hash) % TABLE_SIZE;
	}
	
	
	
	/**
	 * method to remove an entry based on its key
	 */
	@Override
	public V remove(K k) {
		// if key is null, throw exception
		if (k == null) {
			throw new IllegalArgumentException("No null keys");
		}
		
		// get hash for the key
		int hash = calculateHashCode(k);
		
		System.out.println("Removing entry with key " + k + " and value " + map.get(hash) + "from map at index " + hash);
		
		// if map at index hash-value is not null...
		if (map.get(hash) != null) {
			// create pointer for map[hash] and a previous pointer
			// initiated to null
			MapEntry<K, V> prevEntry = null;
			MapEntry<K, V> current = map.get(hash);
			
			// while the current pointer is not pointing to the last link
			// in the chain AND the current key is different from the key of
			// the entry to be removed, iterate both pointers up
			while(current.next != null && !current.key.equals(k)) {
				prevEntry = current;
				current = current.next;
			}
		
			// if previous pointer never moved (meaning the first link in the list
			// is the link to remove, set the slot in the array to the next link (so
			// now there is no pointer to that first link, it is now unaccessible)
			if (prevEntry == null) {
				map.set(hash, current.next);
				
			// otherwise, point the previous pointer to the entry that the entry-to-
			// be-removed's next pointer is pointing to
			} else {
				prevEntry.next = current.next;
			}
			
			size--;
			// return removed value
			return current.value;
		}
		
		// if key not in map, remove none
		return null;
	}
	
	
	
	/**
	 * method that checks if a key is contained in the map
	 */
	@Override
	public boolean contains(K k) {
		// if key is null, throw exception
		if (k == null) {
			throw new IllegalArgumentException("No null keys");
		}
		
		// get hash for the key
		int hash = calculateHashCode(k);

		if (map.get(hash) == null) {
			return false; 
			
		} else {
			MapEntry<K, V> temp = map.get(hash);

			while (temp != null && !temp.key.equals(k)) {
				temp = temp.next;
			}
			
			return temp != null; // I got this line from debugcode.ai
		}
	}
	
	
	
	/**
	 * Method that returns whether map is empty 
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	
	
	/**
	 * Method that returns whether maps if full (never)
	 */
	@Override
	public boolean isFull() {
		return false;
	}
	
	
	
	/**
	 * Method that returns size of map
	 */
	@Override
	public int size() {
		return size;
	}
		
	
	
	/**
	 * Method that prints the map
	 */
	public void printMap() {
		for (int i = 0; i < map.size(); i++) {
			
			MapEntry<K, V> entry = map.get(i);
			
			if(entry != null) {
				System.out.print("\n" + (i+1) + ": ");
			
				while (entry != null) {
					System.out.print(entry.key + " - " + entry.value + ", ");
					entry = entry.next;	
				}
			}
		}
		System.out.println();
	}
		
	
	
	/*
	 * Method that reports on number of entries, size of array, and 
	 * number of empty slots in the array
	 */
	public void hashMapReport() {
		System.out.println("Entries in map: " + size);
		System.out.println("Size of array: " + TABLE_SIZE);
		
		// length of linked list for each hash code
		
		// empty
		int emptyCount = 0;
		for (int i = 0; i < TABLE_SIZE; i++) {
			if (map.get(i) == null) {
				emptyCount++;
			}
		}
		System.out.println("Empty Slots: " + emptyCount);

	}
		
	
	/**
	 * Iterator class for the HashTable (takes into account the buckets with chaining)
	 */
	protected class MapIterator implements Iterator<MapEntry<K, V>> {
	    private int currentIndex = -1; // Current index in the map array
	    private MapEntry<K, V> currentEntry = null; // Current entry in the linked list
	    
	    /**
	     * MapIterator constructor
	     */
	    public MapIterator() {
	        // Initialize currentIndex and currentEntry to the first valid entry
	        for (int i = 0; i < TABLE_SIZE; i++) {
	            if (map.get(i) != null) {
	                currentIndex = i;
	                break;
	            }
	        }
	    }
	    
	    
	    /**
	     * method that determines if an entry has a next entry in the hash table
	     */
	    public boolean hasNext() {
	    	// if current entry's next pointer is not null - has next
		    // if current entry's next pointer is null but the currentIndex is not the last index - has next
		    // if current entry's next pointer is null and the currentIndex is the last index - no next
	        return currentEntry != null || currentIndex < TABLE_SIZE;
	    }
	    
	    
	    /**
	     * Method that determines the next entry in the hash table
	     */
	    public MapEntry<K, V> next() {
	    	// if there is no next element, throw exception
	        if (!hasNext()) {
	            throw new NoSuchElementException();
	        }
	        
	        // if currentEntry is not null and if there is a next entry in the linked list
	        if (currentEntry != null && currentEntry.next != null) {
	        	// set currentEntry to the next entry
	            currentEntry = currentEntry.next;
	            
	        // otherwise
	        } else {
	        	// increment the currentIndex
	            currentIndex++;
	            // If the current linked list ends, move to the next non-null entry in the map array
	            while (currentIndex < TABLE_SIZE && map.get(currentIndex) == null) {
	                currentIndex++;
	            }
	            
	            // set currentEntry to currentIndex's value in map, or null if at the end of table
	            currentEntry = currentIndex < TABLE_SIZE ? map.get(currentIndex) : null;
	        }
	        return currentEntry;
	    }
	}

	
	/**
	 * Method that returns custom iterator
	 */
	public Iterator<MapEntry<K, V>> iterator() {
		return new MapIterator();
	}
}
