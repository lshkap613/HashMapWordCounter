import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// initialize text string
		String text = "";

		// try to scan file
		try {
			File file = new File("C:\\Users\\liels\\eclipse-workspace\\MapWordCounter\\src\\littleWomenSnippet.txt");
			
			Scanner scan2 = new Scanner(file);
			
			System.out.println("\nScanning book... (this may take a few seconds)");
			
			// scan entire file
			while(scan2.hasNext()) {
				text += scan2.nextLine() + " "; // without this space, the last word of every line was being concatenated with the first word of the next line
			}
			scan2.close();
			
		// catch filenotfound exception
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		Scanner scan = new Scanner(System.in);

		// determine whether user would like to choose naive hash method or more complicated hash method
		boolean useNaiveHashCode = chooseHashMethod(scan);
		
		// split text string into an array of words
		String[] words = text.split("\\s+");
		
		// Create HashTable
		HashTable4<String, Integer> ht = new HashTable4<String, Integer>(200, useNaiveHashCode);
		
		// place words from words array into map
		placeWordsInMap(ht, words);
		
		// allow user to search for specific words in map
		searchForWords(ht, scan);
		
		// allow user to explore particular buckets
		exploreBuckets(ht, scan);
		
		// report on hash map
		ht.hashMapReport();

		// print map
		ht.printMap();
		
		System.out.println("\nNow, printing words in descending order or frequency. Press enter to start");
		scan.nextLine();
		
		// print words in descending order of frequency
		printMapDescendingOrder(ht);
	}
	
	/**
	 * Method that allows user to choose a hash method
	 * @param scan Scanner
	 * @return returns true if user chose naive hash method, false if user chose more complicated hash method 
	 */
	public static boolean chooseHashMethod(Scanner scan) {
		System.out.println("Enter 1 if you would like to use the naive hash code method, or 2 if you would like to use the more complicated hash code method");
		int userInput = scan.nextInt();
		
		// validation: only allow values 1 and 2
		while(userInput != 1 && userInput != 2) {
			System.out.println("Please enter 1 or 2");
			userInput = scan.nextInt();
		}
		scan.nextLine(); // clear buffer
		
		// determine return
		if (userInput == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * Method to place words in map
	 * @param ht hash map
	 * @param words array of words to put in map
	 */
	public static void placeWordsInMap(HashTable4<String, Integer> ht, String[] words) {
		for (String word : words) {
			word = word.replaceAll("[^\\w]", "");
			
			Integer putValue = ht.put(word, 1);

			if (putValue != null) {
				ht.put(word, putValue+1);
			}
			
		}
	}
	
	
	/**
	 * method to search for words in map
	 * @param ht hash map
	 * @param scan Scanner
	 */
	private static void searchForWords(HashTable4<String, Integer> ht, Scanner scan) {
		System.out.println("\nIf you would like to search for a word to see how many times if appears in the book, enter the word."
				+ "\nOtherwise, enter \"x\" to skip.");
		String word = scan.nextLine();
		int hashIndex;
		MapEntry<String, Integer> chainEntry;
		int chainCount;
		
		while (!word.toLowerCase().equals("x")) {
			Integer value = ht.get(word);
			
			if (value == null) {
				System.out.println("\"" + word + "\"" + " does not appear in the book");
			} else {
				System.out.print("\"" + word + "\"" + " appears " + value + " times, ");
				hashIndex = ht.calculateHashCode(word);
				chainEntry = ht.getBucket(hashIndex);
				chainCount = 1;

				while(chainEntry.next != null) {
					chainEntry = chainEntry.getNext();
					chainCount++;
				}
				System.out.println("and it is in a chain of " + chainCount + " entries at index " + hashIndex + "\n");
			
			}
		
			System.out.println("\nEnter another word to search for or enter \"x\" to continue.");
			word = scan.nextLine();
	    }
	}
	
	
	
	/**
	 * Method that prints number of words in specified bucket
	 * @param ht hash map
	 * @param scan Scanner
	 */
	private static void exploreBuckets(HashTable4<String, Integer> ht, Scanner scan) {
		System.out.println("\nEnter number between 0 and " + ht.TABLE_SIZE + " to check number of words in that (hashmap) bucket"
				+ "\n or enter \"-1\" to skip.");
		int bucket = scan.nextInt();
		
		while(bucket < -1 || bucket > ht.TABLE_SIZE) {
		System.out.println("Invalid input. Please enter a number 0 and " + ht.TABLE_SIZE + " or -1 to continue.");
		bucket = scan.nextInt();
		}
		
		while(bucket != -1) {
			int wordsInBucketCount = 0;
			
			MapEntry<String, Integer> entryInBucket = ht.getBucket(bucket);
			if (entryInBucket != null) {
				wordsInBucketCount++;
				
				while(entryInBucket.next != null) {
					entryInBucket = entryInBucket.next;
					wordsInBucketCount++;
				}
				System.out.println("There are " + wordsInBucketCount + " words in bucket #" + bucket);
			} else {
				System.out.println("There are 0 words in bucket #" + bucket);
			
			}
			
			System.out.println("Enter bucket number or enter \"-1\" to continue.");
			bucket = scan.nextInt();
		}
		scan.nextLine(); // clear buffer
	}
	
	private static void printMapDescendingOrder(HashTable4<String, Integer> ht) {
	    Iterator<MapEntry<String, Integer>> iterator = ht.iterator();
	    ArrayList<MapEntry<String, Integer>> entries = new ArrayList<MapEntry<String, Integer>>();
	    
	    // enter all entries into an ArrayList
	    while(iterator.hasNext()) {
	        MapEntry<String, Integer> entry = iterator.next();
	        if (entry != null)
	        	entries.add(entry);
	    }
	    
	    Collections.sort(entries, new Comparator<MapEntry<String, Integer>>() {
	    	@Override
	        public int compare(MapEntry<String, Integer> o1, MapEntry<String, Integer> o2) {
	            return o2.value.compareTo(o1.value);
	        }
	    });
	    
	    for (MapEntry<String, Integer> entry : entries) {
	    	System.out.println(entry);
	    }
	     
	}

}
