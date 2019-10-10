import java.util.LinkedList;
import java.io.*;
import java.util.*;
public class Hand
{
	private List<Card> Hand;  //hand as a list
	private List<String> Names = new LinkedList<String>();
	
	public int getHandTotal()
	{
		int total = 0;
		int aceElevens = 0;
		for (int i = 0; i < this.Hand.size(); i++)  //go through all cards in the hand 
		{
			Card thisCard = retrieve(i);   //take this card at i 
			
			total += thisCard.getValue(); //add value of card to total
		}
		for (int j = 0; j < this.Hand.size(); j++) //go through the deck again 
		{ 
			Card thispCard = retrieve(j); 
			if ((thispCard.getValue() == 1)) //if the total is less than or equal to 10 and we have an ace //(total <= 10)
			{
				total += 10;                               //make it valued 11
				aceElevens++;                              //increment number of aceElevens
				continue;
			}
		}
		if (total >= 22 && aceElevens > 0) //if score is greater than or equal to 11, for each ace eleven, -10
		{
			for (int i = 0; i < aceElevens; i++)
			{
				total -= 10;
			}
		}
		if (total == 21 && this.Hand.size() == 2)      //blackjack (special case)
		{
			return 31;		//31 signifies blackjack
		}
		return total;
	}
	
	public int getHandSize()     //return int size of hand (how many cards)
	{
		return this.Hand.size();
	}
	
	public boolean isEmpty() //returns true if hand is of size 0
	{
		if (this.Hand.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public void insert(Card card) //add a card to the hand
	{
		this.Hand.add(card);
	}
	
	public Hand()  //constructor 
	{
		this.Hand = new LinkedList<Card>();
	}
	
	public String HandWNames() //generate the list of cards with their names (rank and suit)
	{
		Names = new LinkedList<String>(); //new each time to avoid copies
		String thisCN = "";
		for (int i = 0; i < this.Hand.size(); i++)  //take each card
		{
			thisCN = this.Hand.get(i).getRankName() + " of " + this.Hand.get(i).getSuit(); //create a string with rank and suit
			Names.add(thisCN);                                                              //add to names list
		}
		return String.format("%s", Names.toString());  //return the list of card names in the hand as string
	}
	
	public String toString()  //return string of the hand
	{
		return String.format("%s", Hand.toString());
	}
	
	public Card deleteFirst()  //delete the first card in the hand
	{
		Card theCard = retrieve(0);;
		this.Hand.remove(0);
		return theCard;
	}
	
	private Card retrieve(int position) //return the card at a particular position in the hand
	{
		return this.Hand.get(position);
	}
	
	public void displayHand()    //display all the cards in the hand
	{
		for (int i = 0; i < this.Hand.size(); i++)
		{
			Card thisCard = retrieve(i);
			thisCard.showCard();
		}
	}
	
	public boolean bothEqual(Hand otherHand)
	{
		if (getHandTotal() == otherHand.getHandTotal())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean doubleAce()
	{
		if ((this.Hand.get(0).getValue() == 1) && (this.Hand.get(1).getValue() == 1))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
}