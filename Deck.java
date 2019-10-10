import java.util.Arrays;
import java.util.Random;
import java.util.*;
public class Deck
{
	/*
		This program displays the cards as they are meant to be shuffled by a specified shannon entropy 
	*/
	
	private String[] theDeck;
	
	private static void third_Shuffle_Algorithm(int[] arrayToShuffle)  //method to shuffle an array (by reference) using third shuffle algorithm
	{
		Random random = new Random();
		int randomToSwap = random.nextInt(arrayToShuffle.length);     //choose random card
		for (int i = 0; i < arrayToShuffle.length; i++)
		{
			int tempVar = arrayToShuffle[i];              //store the current card temporarily
			arrayToShuffle[i] = arrayToShuffle[randomToSwap];   //swap it with the random card
			arrayToShuffle[randomToSwap] = tempVar;
			randomToSwap = random.nextInt(arrayToShuffle.length); //generate new random number, or new random card
		}
	}
	
	private static int[] riffle_Shuffle(int[] arrayToShuffle)
	{
		Random random = new Random();
		double shannonEntropy = 0;
		int splitter =  random.nextInt(10) + 20;       //to split a deck imperfectly in half, different each time
		int[] firstHalf = new int[splitter];          //split into 2
		int[] secondHalf = new int[arrayToShuffle.length - firstHalf.length];
		for (int u = 0; u < firstHalf.length; u++)  //add relevant data to first array
		{
			firstHalf[u] = arrayToShuffle[u];
		}
		for (int f = firstHalf.length; f < arrayToShuffle.length; f++) //add relevant data to second array
		{
			secondHalf[f-firstHalf.length] = arrayToShuffle[f];
		}
		int firstsTaken = 0, secondsTaken = 0, index = 0;      //to count how many taken from first half, second half and how many added
		int randomSide = random.nextInt(2);           //choose randomly from either deck
		while (index < arrayToShuffle.length)     //index only updated when an element has been added
		{
			randomSide = random.nextInt(2);            //new random number
			if ((randomSide == 0) && (firstsTaken < firstHalf.length)) //if random choice (for first half) = 0 and first half still has cards
			{
				arrayToShuffle[index] = firstHalf[firstsTaken];
				firstsTaken++;
				index++;
				continue;
			}
			else if((randomSide == 1) && (secondsTaken < secondHalf.length)) //if random choice (for second half) = 1 and second half still has cards
			{
				arrayToShuffle[index] = secondHalf[secondsTaken];
				secondsTaken++;
				index++;
				continue;
			}
			else     
			{
				randomSide = random.nextInt(2);
			}
		}
		return arrayToShuffle;
	}
	
	private static int[] copyOf(int[] original)  //method to create copy of an int array
	{
		int[] newArray = new int[original.length];
		for (int i = 0; i < newArray.length; i++)
		{
			newArray[i] = original[i];
		}
		return newArray;
	}
	
	private static boolean isIn(int theNum, int[] theArray)  //method to check if int array contains a certain int
	{
		for (int i = 0; i < theArray.length; i++)
		{
			if (theArray[i] == theNum)                     //does contain the int
			{
				return true;
			}
		}
		return false;                                //doesnt contain the int
	}
	
	private static double shannon_Entropy(int[] arrayForCalc)  //to calculate shannon entropy of shuffled array
	{
		double sum = 0, shannonEntropyResult = 0;
		int difference = 0;
		int[] differencesA = new int[51];              //to store differences between each consecutive element in arrayForCalc
		int[] differences_Counts = {};                 //to store all unqiue differences
		for (int i = 0; i < arrayForCalc.length - 1; i++)     //find differences between all consecutive elements
		{
			difference = 0;                                   
			difference = arrayForCalc[i+1] - arrayForCalc[i];
			if (difference < 0)                               //if difference is negative you add 52
			{
				difference += 52;
			}
			differencesA[i] = difference;
			
		}
		for (int i = 0; i < differencesA.length; i++)         //add all unique differences to one array
		{
			if (!(isIn(differencesA[i], differences_Counts))) //if differences_Counts contains the difference under inspection
			{
				int[] originalArray = copyOf(differences_Counts);  //create copy of original array with unqiue differences
				differences_Counts = new int[differences_Counts.length + 1]; //create a new array one longer than the original
				for (int u = 0; u < originalArray.length; u++) //add all elements of originalArray to differences_Counts
				{
					differences_Counts[u] = originalArray[u];
				}
				differences_Counts[differences_Counts.length - 1] = differencesA[i];  //add the new unique number
			}
		}
		int[] differences_Counts_HowMany = new int[differences_Counts.length];
		int occurrence = 0;
		for (int n = 0; n < differences_Counts.length; n++) //go through all unqiue differences 
		{
			for (int j = 0; j < differencesA.length; j++) //go through all differences
			{
				if (differencesA[j] == differences_Counts[n])  //if the difference equals the unique difference under inspection;
				{
					occurrence++;
				}
			}
			differences_Counts_HowMany[n] = occurrence; //the index of occurrence corresponds to the unique difference
			occurrence = 0;
		}
		double[] probabilities = new double[differences_Counts.length]; //store probabilities of each difference
		for (int i = 0; i < probabilities.length; i++) //go through each unique unqiue difference and find its probability
		{
			probabilities[i] = ((double)differences_Counts_HowMany[i])/((double)differences_Counts_HowMany.length);
		}
		for (int i = 0; i < probabilities.length; i++)  //go through each probability
		{
			if (differences_Counts_HowMany[i] != 0)  //only add to the sum if unique difference has a count greater than zero
			{
				sum += (probabilities[i]) * (Math.log(probabilities[i])); //shannon entropy is sum of probability of each bin times log of that bin
			}
		}
		shannonEntropyResult = sum * -1;                    //shannon entropy is the sum times by negative 1
		return shannonEntropyResult;
		
	}
	
	private static void fisher_Yates_Shuffle(int[] arrayToShuffle)
	{
		Random random = new Random();                         //to create a random variable
		for (int s = arrayToShuffle.length - 1; s > 0; s--)  //loop 51 times, with s starting at 51 and decreasing to 1
		{
			int randomIndex = random.nextInt(s + 1);       //create a random variable between 0 and s (inclusive)
			int tempVar = arrayToShuffle[randomIndex];     //store the value 
			arrayToShuffle[randomIndex] = arrayToShuffle[s];  //swap
			arrayToShuffle[s] = tempVar;
		}
	}
	
	private static String[] createDeckofCards(int howMany)  //method that creates standard 52 card deck with each unique part of unicode card value
	{
		String[] arrayToReturn = new String[howMany];
		String[] suits = {"A", "B", "C", "D"}; //suits
		String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "D", "E"};  //numbers, jack, queen and king
		int index = 0;
		String result = "";
		for (int i = 0; i < suits.length; i++)
		{
			for (int g = 0; g < numbers.length; g++)
			{
				result = suits[i] + numbers[g];   //string the current suit to current number
				arrayToReturn[index] = result;
				index++;
			}
		}
		return arrayToReturn;  //return this array
	}
	
	private static int[] createDeck(int length) //method to create initial deck of specified length
	{
		int[] arrayToReturn = new int[length];
		for (int i = 0; i < length; i++)
		{
			arrayToReturn[i] = i;
		}
		return arrayToReturn;
	}
	
	private static void display(String[] theDeck) //method to display deck of cards using unicode values
	{
		String theCard = "";          //for unique parts of cards
		for (int f = 0; f < theDeck.length; f++)
		{
			theCard = theDeck[f];
			int c = Integer.parseInt("DC" + theCard, 16); //get hexadecimal value
			String s = "\uD83C" + (char) c;  //unicode character
			System.out.println(s); //display
		}
	}
	
	private static String[] correspondShuffle(int[] theInts, String[] cardsDeck) //to make the deck of cards to be displayed correspond to the way it was shuffled
	{
		String[] deckToBe = new String[cardsDeck.length];
		for (int i = 0; i < deckToBe.length; i++)
		{
			deckToBe[i] = cardsDeck[theInts[i]]; 
		}
		return deckToBe;
	}
	
	public void shuffle()
	{
		int[] deckInts = createDeck(52); //create int array with 52 positions and each the value of their index
		double shannonEntropy = 0;
		double specified_Shannon_Entropy = 4.9;      //specified entropy that must be reached
		int times = 0;                           //to count number of shuffles
		while (shannonEntropy < specified_Shannon_Entropy)
		{
			third_Shuffle_Algorithm(deckInts);           //shuffle the deck by reference
			shannonEntropy = shannon_Entropy(deckInts);
			times += 1;
			if (shannonEntropy >= specified_Shannon_Entropy)
			{
				break;
			}
			fisher_Yates_Shuffle(deckInts);           //shuffle the deck by reference
			shannonEntropy = shannon_Entropy(deckInts);
			times += 1;
			if (shannonEntropy >= specified_Shannon_Entropy)
			{
				break;
			}
			for (int i = 0; i < 8; i++)
			{
				deckInts = riffle_Shuffle(deckInts);                 //riffle shuffle the deck
				times += 1;
				shannonEntropy = shannon_Entropy(deckInts);
				if (shannonEntropy >= specified_Shannon_Entropy)
				{
					break;
				}
			}
		}
		this.theDeck = correspondShuffle(deckInts, this.theDeck);
	}
	
	public String toString()
	{
		String theArray = "";
		theArray += "[";
		for (int i = 0; i < this.theDeck.length; i++)
		{
			theArray += this.theDeck[i] + ",";
		}
		theArray = theArray.substring(0, theArray.length()-1);
		theArray += "]";
		return String.format("%s", theArray);
			
	}
	
	public String top()
	{ 
		return this.theDeck[0];
	}
	
	public int length()
	{
		int len = 0;
		for (int i = 0; i < this.theDeck.length; i++)
		{
			len++;
		}
		return len;
	}

	public Deck()
	{
		this.theDeck = createDeckofCards(52);
	}
	
	public boolean isEmpty()
	{
		if (this.theDeck.length == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Card get(int position)
	{
		Card theCard = new Card(this.theDeck[position]);
		return theCard;
	}
}