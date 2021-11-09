package blackjackgame;

/**
 * 
 * Cards suit and number
 * 
 * @author DANISH MOHD
 *
 */

public class Cards {
	
	private Suits cardSuit;
	private int cardNum;
	private String[] numString = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	
	/**
	 * Cards constructor
	 */
	public Cards(Suits sType, int sNum){
		 
		this.cardSuit = sType;
		
		if(sNum >=1 && sNum <= 13)
			this.cardNum  = sNum;
		else{
			
			System.err.println(sNum+" is not a valid card number\n");
			System.exit(1);
		}
	}
	
	public int getCardNumber(){
		
		return this.cardNum;
	}
	
	public String toString(){
		
		return this.numString[this.cardNum - 1]+" of "+this.cardSuit.toString();
	}
	
	
	
}
