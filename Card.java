public class Card
{
	private String card;
	
	public Card(String cardStr) //constructor
	{
		this.card = cardStr;
	}
	
	public String getRankName() //return the rank name for each card
	{
		String rank = "" + this.card.charAt(1);
		String theAns = "";
		if ((this.card.charAt(1) == 49)) //ace value
		{
			theAns = "Ace";
		}
		else if ((this.card.charAt(1) >= 50) && (this.card.charAt(1) <= 57)) //2 through 9
		{
			theAns = rank;
		}
		else if (rank.equals("A")) //10
		{
			theAns = "10";
		}
		else if (rank.equals("B"))
		{
			theAns = "Jack";
		}
		else if (rank.equals("D"))
		{
			theAns = "Queen";
		}
		else if (rank.equals("E"))
		{
			theAns = "King";
		}
		else
		{
			theAns = "Invalid Rank";
		}
		return theAns;
	}
		
		
		
	
	public String toString()  //string of card
	{
		return String.format("%s", this.card);
	}
	
	public int getValue()  //method to return the value of the card
	{ 
		if ((this.card.charAt(1) >= 49) && (this.card.charAt(1) <= 57))  //valued 1 to 9
		{
			return Character.getNumericValue(this.card.charAt(1));
		}
		else                             //valued 10
		{
			return 10;
		}
	}
	
	public String getSuit()  //return the suit of a card as a string
	{
		String suit = "" + this.card.charAt(0);
		if (suit.equals("A"))
		{
			return "Spades";
		}
		else if (suit.equals("B"))
		{
			return "Hearts";
		}
		else if (suit.equals("C"))
		{
			return "Diamonds";
		}
		else
		{
			return "Clubs";
		}
	}
	
	public void showCard()  //display the card (unicode display)
	{
		String theCard = "";          //for unique parts of cards
		theCard = this.card;
		int c = Integer.parseInt("DC" + theCard, 16); //get hexadecimal value
		String s = "\uD83C" + (char) c;  //unicode character
		System.out.println(s); //display
	}
}