import java.util.Scanner;
import java.util.InputMismatchException;
import java.nio.file.Paths;     
import java.nio.file.Path;
import java.nio.file.Files;           //import all packages to read and write from/to files
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;

public class Blackjack
{
	public static String options(Hand playerHand, int funds, int score)  //provides options when split or doubling down is possible
	{
		Scanner optionChoice = new Scanner(System.in);
		String ans = "";
		while (!(ans.equals("s") || ans.equals("h") || ans.equals("w") || ans.equals("d"))) //user must enter one of these
		{
			if ((playerHand.getHandTotal() == 31)) //blackjack
			{
				return "b";
			}
			/*
				can perform a split:
			*/
			else if (((funds*2) <= score) && (((playerHand.getHandSize() == 2) && (playerHand.getHandTotal() == 20)) || ((playerHand.getHandSize() == 2) && (playerHand.doubleAce())))) //can split
			{
				
				System.out.println("Options: ");
				System.out.println("Press S for split");
				System.out.println("press H for hit");
				System.out.println("press W for stand");
				ans = optionChoice.nextLine().toLowerCase(); 
				System.out.println();
			}
			/*
				can perform a double down:
			*/
			else if (((funds*2) <= score) && (((playerHand.getHandSize() == 2)) && ((playerHand.getHandTotal() >= 9) && (playerHand.getHandTotal() <= 11)))) 
			{
				System.out.println("Options: ");
				System.out.println("Press D for double down");
				System.out.println("press H for hit");
				System.out.println("press W for stand");
				ans = optionChoice.nextLine().toLowerCase();
				System.out.println();
			}
			/*
				can only hit or stand
			*/
			else
			{
				System.out.println("Options: ");
				System.out.println("press H for hit");
				System.out.println("press W for stand");
				ans = optionChoice.nextLine().toLowerCase();
				System.out.println();
			}
		}
		return ans; //return their choice
	}
	
	public static String options2(Hand playerHand, String handNo) //provides options for individual split decks where split and double down arent possible
	{
		Scanner optionChoice = new Scanner(System.in);
		String ans = "";
		while (!(ans.equals("h") || ans.equals("w"))) //have to hit or stand
		{
			if ((playerHand.getHandTotal() == 31)) //blackjack
			{
				return "b";
			}
			else
			{
				System.out.printf("Options for %s:\n", handNo);
				System.out.println("press H for hit");
				System.out.println("press W for stand");
				ans = optionChoice.nextLine().toLowerCase();
			}
		}
		return ans;  //return their choice
	}
	
	public static Hand initialDeckHand() //create the initial deck (52 cards)
	{
		Hand deckHand = new Hand();   //deck as a hand
		Deck theDeck = new Deck();   //create deck
		theDeck.shuffle();           //shuffle the deck
		for (int i = 0; i < theDeck.length(); i++)
		{
			Card theCard = theDeck.get(i); //turn each element of the deck into a card
			deckHand.insert(theCard); //add the card to the deck
		}
		return deckHand;    //return the deck as a hand of cards
	}
	
	public static void showHands (Hand player, Hand dealer)  //method to show each hand (dealer and player's)
	{
		player.displayHand();
		dealer.displayHand();
	}
	
	public static void initialDraw(Hand firstDeck, Hand player, Hand dealer) //the initial drawing of cards from the 52 card deck, giving 2 to player and 1 to dealer
	{
		player.insert(firstDeck.deleteFirst()); //insert cards from deck into players hand
		player.insert(firstDeck.deleteFirst()); //
		dealer.insert(firstDeck.deleteFirst()); //insert a card from deck into players hand
	}
	
	public static void hit(Hand firstDeck, Hand player) //method to give a hand another card
	{
		player.insert(firstDeck.deleteFirst());  
	}
	
	public static Hand split(Hand firstDeck, Hand player) //method to split a hand into two
	{
		Hand otherHand = new Hand();                   //create a new hand
		otherHand.insert(player.deleteFirst());        //take a card from the initial deck and insert into the new hand
		otherHand.insert(firstDeck.deleteFirst());     //add another card from the original 52 card deck into the new hand
		player.insert(firstDeck.deleteFirst());        //give the original player hand another card from the original 52 card deck
		return otherHand;                              //return the new hand
	}
	
	public static void dealerDraw(Hand firstDeck, Hand dealer, Hand player) //method for the dealer to draw cards
	{
		/*
			dealer will hit until their hand total is higher than the players, but stop drawing if their score is 17, 18, 19, 20, 21, blackjack, or they are bust
		*/
		while ((dealer.getHandTotal() <= player.getHandTotal()) && (dealer.getHandTotal() < 17) && (dealer.getHandTotal() != 21) && !(dealer.getHandTotal() > 21))
		{
			hit(firstDeck, dealer);
		}
	}
	
	public static boolean compareHands(Hand player, Hand dealer) //method to compare hand totals
	{
		if (player.getHandTotal() > dealer.getHandTotal()) //if the first hand is greater than the second value, return true
		{
			return true;
		}
		else //equal to or less than
		{
			return false;
		}
	}
	
	public static void doubleDown(Hand firstDeck, Hand player) //method for player to double down (one more is added)
	{
		player.insert(firstDeck.deleteFirst());
	}
	
	public static void createFile(String info, String addressing) //for storing high scores
	{
		try (PrintWriter w = new PrintWriter(new FileWriter(addressing, true))) //if the file does not exist, create it and add data accordingly or if does exist just add on
		{
			w.println(info);   //add the data to the line, next set of data to be added will be on a new line
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String[] ReadFileLines(String addressing)        //method to read all lines of data in the file
	{
		String[] allLines = null;                //initialise an array to store each line as separate elements
		Path filePath = Paths.get(addressing);  
		try
		{
			allLines = Files.readAllLines(filePath).toArray(new String[0]);  //store each line of data in the file as a separate element in the array allLines
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return allLines;      //return the array so that it can be used for reading
	}
	
	public static void createFileSorted(String[] alls, String addressing)  //for overwriting the file with sorting scores
	{
		try (PrintWriter w = new PrintWriter(new FileWriter(addressing))) //if the file does not exist, create it and add data accordingly or if it does, overwrite
		{
			for (int i = 0; i < alls.length; i++)
			{
				w.println(alls[i]);   //add the data to the line, next set of data to be added will be on a new line
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sortScores(String[] info)  //method to sort scores in in decreasing order
	{
		int previousScore = 0;
		for (int h = 0; h < info.length; h++)
		{
			for (int i = 0; i < info.length; i++)
			{
				String[] thisLine = info[i].split(": "); //take the data on a line
				int score = Integer.parseInt(thisLine[1]);
				if (i == 0)
				{
					previousScore = score;
				}
				if (i > 0)
				{
					if (previousScore < score)
					{
						String tempVar = info[i-1];  //swap
						info[i-1] = info[i];
						info[i] = tempVar;
						previousScore = score;
					}
					else if (previousScore >= score)
					{
						previousScore = score;
					}
				}
			}
		}
	}
		
	public static void main(String[] args)
	{
		Hand playerHand;
		Hand dealerHand;
		Hand firstDeckShuffled;
		int score = 1000;                //player starts with a score (or money) of 1000
		while (score > 4)                //can keep playing 
		{
			boolean dealerBust = false;   //dealer hit bust
			boolean deck1Bust = false;    //two decks for split
			boolean deck2Bust = false;
			Scanner in = new Scanner(System.in);
			System.out.printf("Money: $%d\n\n", score);   //display Money/Score
			System.out.print("Enter Q if you wish to walk away, it can only be done now, otherwise any other key to continue: "); //Provide option to walk away and keep score
			String walk = in.nextLine().toLowerCase();
			if (walk.equals("q"))
			{
				System.out.print("Enter Q again to confirm walking away and quitting, otherwise any other key to continue playing: "); //confirm walk away first
				walk = in.nextLine().toLowerCase();
				if (walk.equals("q"))
				{
					break;  						//stop this playing
				}
			}
			System.out.println();
			int bet = 0;    						//bet instantiated to 0
			do 										//take bet
			{
				try 
				{
					System.out.printf("Place your bet (Minimum = $5, Maximum = $%d): ", score);
					bet = in.nextInt();
				} 
				catch (InputMismatchException e) //they did not enter int
				{
					System.out.println();
					System.out.printf("Poor bet, please place a bet that is an integer value and greater than 5 or smaller than %d\n", score);
					System.out.println();
				}
				in.nextLine();                 
			}
			while ((bet < 5) || (bet > score));  //bet must be higher than 5 and less than or equal to score(funds)
			System.out.println();
			playerHand = new Hand();              //create new player hand
			firstDeckShuffled = new Hand();      //create new shuffled deck as hand
			dealerHand = new Hand();             //create new dealer hand
			firstDeckShuffled = initialDeckHand();  //shuffleDeck
			initialDraw(firstDeckShuffled, playerHand, dealerHand);  //give player 2 cards from deck and dealer one card

			System.out.printf("Your hand: %s\n", playerHand.HandWNames());      //
			System.out.printf("Your current hand total: %d\n\n",playerHand.getHandTotal());
			System.out.printf("Dealer hand: %s\n", dealerHand.HandWNames());
			System.out.printf("Dealer current hand total: %d\n\n", dealerHand.getHandTotal());
			
			Hand otherPlayerHand = new Hand();  //for if split is done
			boolean splitDone = false;          //set to true if player given choice to split and they confirm
			String ans = options(playerHand, bet, score); //give them their options
			while (((playerHand.getHandTotal() <= 21) || (playerHand.getHandTotal() == 31)) && !((ans.equals("w")))) //w for stand, 31 for blackjack
			{
				if (ans.equals("b")) //blackjack
				{
					System.out.printf("Your hand: %s\n", playerHand.HandWNames());
					System.out.println("BLACKJACK!");
					System.out.println();
					break;
				}
				else if (ans.equals("h"))  //hit
				{
					hit(firstDeckShuffled, playerHand); //give another card to playerHand from original deck
				}
				else if (ans.equals("s")) //split
				{
					otherPlayerHand = split(firstDeckShuffled, playerHand); //playerHand and otherPlayerHand becomes two new hands with cards from playerHand before
					splitDone = true;                                     //split has been done
					bet = bet * 2; //bet mulitplied by 2
					break;
				}
				else if (ans.equals("d"))  //double down
				{
					doubleDown(firstDeckShuffled, playerHand); //add one more card to playerHand
					bet = bet * 2;  //double bet
					break;
				}
				System.out.printf("Your hand: %s\n", playerHand.HandWNames());
				System.out.printf("Your current hand total: %d\n", playerHand.getHandTotal()); //print score
				System.out.println();
				if (playerHand.getHandTotal() <= 21) //give them options again
				{
					ans = options(playerHand, bet, score);
				}
			}
			
			if (splitDone == false) //split was not done
			{
				if ((playerHand.getHandTotal() < 31) && (playerHand.getHandTotal() > 21))  //bust
				{
					score -= bet;                                //player loses instantly
					System.out.println("BUST");
					System.out.printf("Your final hand: %s\n", playerHand.HandWNames());
					System.out.printf("Your final hand total: %d\n", playerHand.getHandTotal());
					System.out.println();
					continue;                                  //next game
				}
			}
		
			if (splitDone == true)     //split was done
			{
				System.out.printf("First hand: %s\n", playerHand.HandWNames());
				System.out.printf("First hand current total: %d\n", playerHand.getHandTotal());  //give their first hand total
				System.out.println();
				String newAns1 = options2(playerHand, "Hand 1"); //new set of different options
				while (((playerHand.getHandTotal() <= 21) || (playerHand.getHandTotal() == 31)) && !(newAns1.equals("w"))) //total less than or equal to 21, or blackjack, has not called stand
				{
					if (newAns1.equals("b")) //blackjack
					{
						System.out.printf("First hand: %s\n", playerHand.HandWNames());
						System.out.println("BLACKJACK ON FIRST HAND!");
						System.out.println();
						break;
					}
					else if (newAns1.equals("h")) //hit
					{
						hit(firstDeckShuffled, playerHand);
					}
					System.out.printf("First hand: %s\n", playerHand.HandWNames());
					System.out.printf("First hand current total: %d\n", playerHand.getHandTotal());
					System.out.println();
					if (playerHand.getHandTotal() <= 21)
					{
						newAns1 = options2(playerHand, "Hand 1");
					}
				}
				System.out.printf("First hand final: %s\n", playerHand.HandWNames());
				if (playerHand.getHandTotal() != 31)
				{
					System.out.printf("First hand total: %d\n", playerHand.getHandTotal());
				}
				else
				{
					System.out.println("First hand total: 21 (BLACKJACK)");
				}
				System.out.println();
				/*
					Again for the next hand
				*/
				System.out.printf("Second hand: %s\n", otherPlayerHand.HandWNames());
				System.out.printf("Second hand current total: %d\n", otherPlayerHand.getHandTotal()); 
				System.out.println();
				String newAns2 = options2(otherPlayerHand, "Hand 2");
				while (((otherPlayerHand.getHandTotal() <= 21) || (otherPlayerHand.getHandTotal() == 31)) && !(newAns2.equals("w")))
				{
					if (newAns2.equals("b"))
					{
						System.out.printf("Second hand: %s\n", otherPlayerHand.HandWNames());
						System.out.println("BLACKJACK ON SECOND HAND!");
						System.out.println();
						break;
					}
					else if (newAns2.equals("h"))
					{
						hit(firstDeckShuffled, otherPlayerHand);
					}
					System.out.printf("Second hand: %s\n", otherPlayerHand.HandWNames());
					System.out.printf("Second hand current total: %d\n", otherPlayerHand.getHandTotal());
					System.out.println();
					if (otherPlayerHand.getHandTotal() <= 21)
					{
						newAns2 = options2(otherPlayerHand, "Hand 2");
					}
				}
				System.out.printf("Second hand final: %s\n", otherPlayerHand.HandWNames());
				if (otherPlayerHand.getHandTotal() != 31)
				{
					System.out.printf("Second hand total: %d\n", otherPlayerHand.getHandTotal());
				}
				else
				{
					System.out.println("Second hand total: 21 (BLACKJACK)");
				}
				System.out.println();
			}
			
			if (splitDone == true)        //split was done
			{
				if ((playerHand.getHandTotal() < 31) && (playerHand.getHandTotal() > 21)) //first hand hit bust values
				{
					System.out.printf("First hand: %s\n", playerHand.HandWNames());
					System.out.println("HAND 1 BUST");
					System.out.printf("Its final total: %d\n\n", playerHand.getHandTotal());
					System.out.println();
					deck1Bust = true;
				}
				if ((otherPlayerHand.getHandTotal() < 31) && (otherPlayerHand.getHandTotal() > 21)) //second hand hit bust values
				{
					System.out.printf("Second hand: %s\n", otherPlayerHand.HandWNames());
					System.out.println("HAND 2 BUST");
					System.out.printf("Its final total: %d\n\n", otherPlayerHand.getHandTotal());
					System.out.println();
					deck2Bust = true;
				}
				if (deck1Bust == true && deck2Bust == true) //both hands hit but, instant lose
				{
					score -= bet;                             //lose whole bet
					System.out.println("BOTH HANDS BUST");
					continue;                                  //next game
				}
			}
			
			/*
				Display player total
			*/
			if (splitDone == false)  //no split was done
			{
				if (playerHand.getHandTotal() == 31)       //player got blackjack, display it
				{
					System.out.printf("Your final hand: %s\n", playerHand.HandWNames());
					System.out.println("Your final total: 21 (BLACKJACK)");  //special case
					System.out.println();
				}
				else                    //didnt get blackjack
				{
					System.out.printf("Your final hand: %s\n", playerHand.HandWNames());
					System.out.printf("Your final total: %d\n", playerHand.getHandTotal()); //display total
					System.out.println();
				}
			}
			
			/*
				dealer turn
			*/
			if (splitDone == true)        //split was done, dealer must decide which deck to try and beat
			{
				if (compareHands(playerHand, otherPlayerHand))  //first deck was bigger score
				{
					dealerDraw(firstDeckShuffled, dealerHand, playerHand);  //try beat first deck
				}
				else                                                     //second deck was bigger score or equal score
				{
					dealerDraw(firstDeckShuffled, dealerHand, otherPlayerHand); //try beat second deck
				}
			}
			else 															//no split was done
			{
				dealerDraw(firstDeckShuffled, dealerHand, playerHand);    // dealer try to beat first and only deck
			}
			
			/*
				display dealer hand
			*/
			if (dealerHand.getHandTotal() == 31)                        //if dealer gets blackjack
			{
				System.out.printf("Dealer final hand: %s\n", dealerHand.HandWNames());
				System.out.println("Dealer final total: 21 (BLACKJACK)"); //show they got blackjack
				System.out.println();
			}
			else                                                         //they didnt get blackjack
			{
				System.out.printf("Dealer final hand: %s\n", dealerHand.HandWNames());
				System.out.printf("Dealer final total: %d\n", dealerHand.getHandTotal()); //show their total
				System.out.println();
			}
			
			/*
			find dealer score
			*/
			if ((dealerHand.getHandTotal() < 31) && (dealerHand.getHandTotal() > 21)) //dealer hit bust values
			{
				dealerBust = true;                                           //dealer hand is bust
				System.out.println("DEALER BUST");
				System.out.println();
				System.out.println("WIN!");
				System.out.println();
				if (splitDone == true)                                   //a split was done
				{
					if ((deck1Bust == false) && (deck2Bust == false))    //neither player hands were bust
					{
						score += (bet*2);     //four times original bet win
						continue;
					}
				}
				else            //player did not bust but dealer did
				{
					score += bet;
					continue;
				}
			}
			
			/*
				handle the winnings
			*/
			if (splitDone == true)             //winnings/losses different for when they split (bet doubled)
			{
				int wins = 0;                //count how many decks beat the dealer
				int pushes = 0;              //how many had a push with the dealer
				if (dealerBust == true)         //dealer did a bust, but a player hand bust as well
				{
					if (((deck1Bust == true) && (deck2Bust == false)) || ((deck2Bust == true) && (deck1Bust == false)))
					{
						continue;                  //no money earned or lost
					}
				}
				else //dealer did not bust
				{
					if ((playerHand.getHandTotal() == dealerHand.getHandTotal())) //push
					{
						System.out.println("Push on 1st hand.");
						pushes++;
					}
					else if ((compareHands(playerHand, dealerHand)) && (deck1Bust == false)) //player hand 1 is not bust and higher score
					{
						System.out.println("First hand WIN");
						wins++;
					}
					if (otherPlayerHand.getHandTotal() == dealerHand.getHandTotal()) //push on 2nd hand
					{
						System.out.println("Push on 2nd hand.");
						pushes++;
					}
					else if ((compareHands(otherPlayerHand, dealerHand)) && (deck2Bust == false)) //player hand 2 did not bust and has higher score
					{
						System.out.println("Second hand WIN");
						wins++;
					}
					if (wins == 1 && pushes == 1) //1 win and a push
					{
						score += (int)(((double) bet)/((double)(2.0))); //twice the original bet
					}
					else if (wins == 2) //2 wins
					{
						System.out.println();
						System.out.println("FOUR TIMES PAYOUT");
						score += (bet*2);                  				//four times original bet
					}
					else if (wins == 0 && pushes == 1)  			 	//push and a lose
					{
						score -= (int)(((double) bet)/((double)(2.0))); //lose original bet
					}
					else if (wins == 0 && pushes == 0)  				//2 losses
					{
						score -= bet;  									//lose bet multiplied by 2 at split
					}
				}
			}
			else //no split done and no busts
			{
				if (playerHand.getHandTotal() == 31 && (dealerHand.getHandTotal() != 31)) //if player gets blackjack and dealer does not, they get a ratio of 3:2 of their original bet
				{
					score += (int) (((double)(bet*3))/((double)(2)));
					continue;     //next game
				}
				if (playerHand.getHandTotal() == dealerHand.getHandTotal()) //push
				{
					System.out.println("PUSH!");
					continue;
				}
				else if (compareHands(playerHand, dealerHand) == true)  //not blackjack but player hand is better
				{
					System.out.println("WIN!");
					score += bet;
					System.out.println();
				}
				else if (compareHands(playerHand, dealerHand) == false) //dealer hand is better
				{
					System.out.println("LOSE!");
					score -= bet;
					System.out.println();
				}
			}
		}
		System.out.println("Final Score: " + score); //they walked away or ran out of money, print their score
		System.out.println();
		Scanner namE = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String name = namE.nextLine();
		System.out.println();
		while ((name.charAt(name.length()-1) == 58) || (name.equals("")))
		{
			System.out.print("Invalid name, it must not end with ':' or be blank, enter a different one: ");
			name = namE.nextLine();
			System.out.println();
		}
		name = name + ": " + score;
		String addressing = "Leaderboard.csv";
		createFile(name, addressing);
		String[] leaderBoard = ReadFileLines(addressing);
		sortScores(leaderBoard);
		createFileSorted(leaderBoard, addressing);
		System.out.println("LEADERBOARD: ");
		for (int i = 0; i < 10; i++)
		{
			try
			{
			System.out.println((i+1) + ". " + leaderBoard[i]);
			}
			catch(IndexOutOfBoundsException e)
			{
				break;
			}
		}
		System.exit(0);
	}
}