/******
 * HashTable ADT
 * Author: Diego Holguin
 * Defines the Hash-Table ADT used for reading in a file of strings 
 * using a variety of dirrect hash table algorithms researched online and 
 * through the course's required text
 ******/

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.lang.String;

public class HashTable 
{
	private LinkedList<String>[] hashArray;
	public static String functionType; 
	private String[] distinctStringArray;
	private int numDistinctStrings = 0;
	private int hashArraySize;
	
	HashTable(String filename, int numOfWords, String typeOfFunction)
	{	
		// Initalzie the Hash Table
		initializeHashTable(filename, numOfWords);
		
		// Set the function type of the Hash Table
		functionType = typeOfFunction;
		
		// Update the variable to hold the number of distinct words in the file
		numDistinctStrings = getNumberOfDistinctStrings();
				
		// Make the open chaining hash table be roughly 50 percent long as the 
		// number of distinctStrings there are within the file
		hashArraySize = numDistinctStrings / 2;
						
		// Creates the hash Array
		createHashArray();
		
		// Print out the contents of each linkedList within the hashArray
		printHashTable();
	}
	
	void createHashArray()
	{
		/* Create an array that will hold half the size of the number of distinct characters
		 * found in the file, which will make the hashTable spread more evenly */
		hashArray = new LinkedList[hashArraySize];
		
		// Initialize each index of the array and instantiate a linedList at that location
		for(int i = 0; i < hashArray.length; i++)
		{
			hashArray[i] = new LinkedList<String>();
		}
		
		// A variable that will hold the value of an entire word 
		int totalHashValue = 0;
		
		// A variable that will hold the average amount of time 
		double averageTime = 0;
		
		int a;
		
		// Calculate each word's hash value, and insert it into the open chaining hash table
		for(a = 0; a < distinctStringArray.length; a ++)
		{
			// The starting time, before the calculation and insertion
			long startTime = System.nanoTime();
			
			// Set the variable to hold the specific word's hash value 
			int hashedOutValue = computeHashValue(distinctStringArray[a],a);
			
			// Add the word to the specific index's linkedList
			hashArray[hashedOutValue].addFirst(distinctStringArray[a]);
			
			// The estimated amount of time it took to perform the calculation and insertion 
			long estimateTime = System.nanoTime() - startTime;
			
			averageTime = ((averageTime*distinctStringArray.length + estimateTime) / distinctStringArray.length);
		}
		
		System.out.println((a) + " , " + averageTime);
	}
	
	/* Parameters: None
	 * Returns: Void 
	 * Purpose : To print out the contents of the Hash Table
	 */ 
	void printHashTable()
	{
		// A variable that simply holds the largest LinkedList size with the hashArray
		int largestOpenedChained = 0;
		
		// A variable that will tally up the total number of LinkedList that aren't empty (active)
		int numberOfActiveLinkedList = 0;
		
		// A variable that dynamically determines the index within a specific instance of 
		// a LinkedList
		int currentLinkedListIndex;
		
		// A variable that simply holds the lowest LinkedList size with the hashArray
		int lowestOpenedChained = 0;
				
		// For each LinkedList in the hashArray..
		for(int c = 0; c < hashArray.length; c++)
		{
			// Updates the variable to start at index 0 for each LinkedList being evaluated
			currentLinkedListIndex = 0;
			
			// If the linkedList at the specific index isn't empty
			if(!hashArray[c].isEmpty())
			{
				
				// Since this LinkedList isn't empty, Increment the variable
				numberOfActiveLinkedList++;
				
				// A dynamic variable will hold the number of elements within a LinkedList
				int currentNumberOfElements = hashArray[c].size();
				
				// On the first pass where 0 will be the lowest no matter what.. 
				if(lowestOpenedChained == 0)
				{
					// Set it to equal the first linkedList size so it can dynamically begin to check
					// throughout the process
					lowestOpenedChained = currentNumberOfElements;
				}
				// If the current number of elements are less then the lowest recorded number of elements.. 
				else if (lowestOpenedChained > currentNumberOfElements)
				{
					// Update the variable to reflect the "now" lowest recorded number of elements
					lowestOpenedChained = currentNumberOfElements;
				}
								
				// Then get each String within the specific index's LinkedList structure
				for(int x = 0; x < hashArray[c].size(); x++)
				{
					// Updates the variable to represent the index location of the specific element
					// dynamically within the LinkedList
					currentLinkedListIndex++;
					
					// If this specific linkedList index value is larger then any previously
					// recorded linked list value
					if(currentLinkedListIndex > largestOpenedChained)
					{
						// Update the variable to equal larger value
						largestOpenedChained = currentLinkedListIndex;
					}
					
					// A variable that hold's the currentString being evaluated
					String currentString = hashArray[c].get(x);
			
					// Show HashTable Related Data
					// System.out.println("Hash Array Index : " + c + " --- Element Location : " + currentLinkedListIndex);
					// System.out.println("Has the character : " + hashArray[c].get(x));
				}
			}		
		}
		
		// A variable which holds the average size of each LinkedList within the HashTable
		int averageLinkedListSize = (int) numDistinctStrings / numberOfActiveLinkedList;
		
		/* Print out to the console the largest LinkedList Size within the hashTable
		System.out.println("Largest Chain Size Within The HashTable Is " + largestOpenedChained);
		System.out.println("Number of LinkedList Is " + numberOfActiveLinkedList);
		System.out.println("Lowest Chain Size Within The HashTable Is " + lowestOpenedChained);
		System.out.println("Average Size of Each Open Chained LinkedList Is " + averageLinkedListSize);
		System.out.println("Percent of LinkedList Created based upon it's size : " + (double)numberOfActiveLinkedList / (double)hashArraySize);
		*/
	}
	
	/* Parameters: String (which is the name of the local filename) , Int (number of words in file)
	 * Returns : Void
	 * Purpose: To initalize the hashTable process by properly taking in the contents of the file
	 * and proprerly create a open hashing hash table that represents all of the unique characters within 
	 * within the file
	 */	
	void initializeHashTable(String filename, int numOfWords)
	{	
		// Initialize tha array to hold a specific number of words
		distinctStringArray = new String[numOfWords];
		
		// A variable that will be used to add to the array in a fashion that is dynamic
		int arrayIncrementer = 0;
		
		// Let's first try and see if we can successfully open the passed in file
		try
		{
			// Create a File instance of the filename
			File file = new File(filename); 
			
			// Create a scanner instance to scan the contents of the file
			Scanner scanner = new Scanner(file);
			
			// While scanning through the contents of the file.. 
			while(scanner.hasNext())
			{
				// String variable that will hold all of the characters that were skipped using nextLine()
				String tempString = scanner.nextLine();
				
				// As long as the character being tested isn't a space and if it is unique..
				if(checkIfDinstinct(tempString, distinctStringArray))
				{
					// Then add it to the array
					distinctStringArray[arrayIncrementer] = tempString.toUpperCase();
					
					// Increment variable so that it add's the next instance to the correct locaiton
					arrayIncrementer++;
				}
			}
			
			// Close the scanner
			scanner.close();
		}
		// In the case that the file passed in isn't within the local directory
		catch (IOException e)
		{
			// Print this error out on the console
			System.out.println(e.getMessage());
		}
	}
	
	/* Parameters: char (the character that will be tested) and char[] (the char[] holder)
	 * Returns : boolean (If false the char is not distinct, else the char is distinct return true)
	 * Purpose: Boolean variable that will determine if the passed in character
	 * is a distinct character ( aka unique ) and if so return true, else returns false */
	boolean checkIfDinstinct(String temp, String[] distinctCharArray2)
	{
		// Variable that will hold if the String is distinct or not
		boolean isDistinct = true;
		
		// Variable that will be used to short-circut the process of checking 
		// each location within the array once the isNotDistinct variable is true
		boolean keepRunning = true;
		
		// Check each character location within the distinctHolder... 
		for(int d = 0; keepRunning && d < distinctCharArray2.length; d++)
		{
			// If any of those locations match the String being tested
			if(distinctCharArray2[d] == temp)
			{
				// Update the variable
				isDistinct = false;
				
				// Update variable to short-cut the process and
				// save us some time and unneeded comparisons
				keepRunning = false;
			}
		}
		
		// Return if the passed in String is distinct or not
		return isDistinct;
	}
	
	/* Parameters: None
	 * Returns: Int 
	 * Purpose : To return the number of unique characters within the distinctCharArray holder
	 */ 
	int getNumberOfDistinctStrings()
	{	
		// Return the number of unique Strings within the distinctCharArray holder
		return distinctStringArray.length;
	}
	
	/* Parameters: None
	 * Returns: Void 
	 * Purpose : To print the number of distinct characters within the distintStringArray
	 */ 
	public void printDistinctCharacters()
	{
		// Check each character within the character array..
		for(int k = 0; k < distinctStringArray.length; k++)
		{
			if(distinctStringArray[k] != null)
			{
				// Print out the specifc index value and the contents that it represents
				System.out.println("Index " + k + " Represents the String : " + distinctStringArray[k]);
		
			}
		}
	}
	
	/* Parameters: String and Int
	 * Returns: Int
	 * Purpose : To compute the hash value with 5 different possible hash functions. The string index
	 * is used to obtain the specific word being evaluated. 
	 */ 
	int computeHashValue(String passedInString, int StringIndex)
	{
		// A variable that will hold the entire hashValue for a given word
		int wordHashValue = 0;

		// If it's set to bad 1... 
		if(functionType.equals("Bad 1"))
		{			
			// Set a temp variable to hold the hashValue of this type of hash function
			int tempHashValue = 0;

			// For each character of each word
			for(int f = 0; f < distinctStringArray[StringIndex].length(); f++)
			{
				// A variable that holds each indivudal character of the word
				char character = distinctStringArray[StringIndex].charAt(f); 
					
				// Updates the tempHashValue to hold the ASCII value of each character within
				// each word
				tempHashValue += (int) character; 
			}
			// Update the wordHashValue to equal the accurate hash value
			wordHashValue = tempHashValue;
		}
		else if (functionType == "Bad 2")
		{
			// Set a temp variable to hold the hashValue of this type of hash function
			int tempHashValue = 1;
			
			// For each word within the distinctStringArray that isn't null... 
			for(int j = 0; j < distinctStringArray[StringIndex].length(); j ++)
			{	
				// A variable that holds each indivudal character of the word
				char character = distinctStringArray[StringIndex].charAt(j); 
					
				// Updates the tempHashValue to hold the ASCII value of each character within
				// each word
				tempHashValue = tempHashValue * (int) character; 
			}
			
			// Update the wordHashValue to equal the accurate hash value
			wordHashValue = Math.abs(tempHashValue);
		}
		else if (functionType == "Good 1")
		{
			int tempHashValue;
			int intLength = passedInString.length() / 4;
			long sum = 0;
		    for (int j = 0; j < intLength; j++) 
		    {
		    	char c[] = passedInString.substring(j*4,(j*4)+4).toCharArray();
		    	long mult = 1;
		    	
		    	for (int k = 0; k < c.length; k++) 
		    	{
		    		 sum += c[k] * mult;
		    		 mult *= 256;
		    	}
		    }
		    char c[] = passedInString.substring(intLength * 4).toCharArray();
		    long mult = 1;
		    for (int k = 0; k < c.length; k++)
		    {
		    	sum += c[k] * mult;
		    	mult *= 256;
		    }
		    
		    // A variable that will hold the typecasted value of sum
		    tempHashValue = (int) sum;
		    
			// Update the wordHashValue to equal the accurate hash value
		    wordHashValue = Math.abs(tempHashValue);
		}
		// Shift-Add-XOR hash : http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
		else if (functionType == "Research Topic 1")
		{
			// Set a temp variable to hold the hashValue of this type of hash function
			 int tempHashValue= 0;

			 // For each character within the word.. 
			 for(int n = 0; n < passedInString.length(); n++)
			 {
				 // Update the tempHashValue by adding the bitwise operations of << 5 and >> 2 
				 // of tempHashValue + the ASCII value of the current character
				 tempHashValue ^= (tempHashValue << 5) + (tempHashValue >> 2) + (int)passedInString.charAt(n);
			 }
				
			 // Update the wordHashValue to equal the accurate hash value
			 wordHashValue = Math.abs(tempHashValue);
		}
		// ELF Hash : http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
		else if (functionType == "Research Topic 2")
		{
			// Set a temp variable to hold the hashValue of this type of hash function
			int tempHashValue = 0;
			
			// A variable that will hold the AND value of each character within the word
			long hexidecimalAnd = 0;

			// For each character within the word
			for(int s = 0; s < passedInString.length(); s++)
			{
				// Perform some calculatations and update it's variables, respectively
				tempHashValue = (tempHashValue << 4) + (int)passedInString.charAt(s);
				hexidecimalAnd = (tempHashValue & 0xf0000000L);
				
				// As long as the hexidecimal isn't equal to 0...
				if(hexidecimalAnd != 0)
				{
					// Perform an additional calculation
					tempHashValue ^= (int) hexidecimalAnd >> 24;
				}
				
				// And the tempHashValue with the bitewise not of hexidecimalAnd and then equal 
				// it to tempHashValue
				tempHashValue &= ~hexidecimalAnd;
			}
			
			// Update the wordHashValue to equal the accurate hash value
			wordHashValue = Math.abs(tempHashValue);
		}

		// Return the hashValue of the specific string (word)
		return wordHashValue % (hashArraySize);
		
	}
	
}
