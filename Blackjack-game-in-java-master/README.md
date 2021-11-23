Blackjack-game-in-java
======================

Text based Blackjack game in Java.



How To Run:
===========

Prequisite: JDK1.7


Download jar file from the link: https://www.dropbox.com/s/720k3ou22s9puj4/BlackJack.jar

use command: java -jar /path-to-jar-file/BlackJack.jar

OR import in Eclipse and Run.


Test Cases:
===========

1. Bet amount should not be greater than balance (i.e 100 or whatever (winning condition)).
2. Bet amount should be a natural number (>0).
3. If balance is 0, game should end. 
4. Check  balance>=bet,  for showing Double down option.
5. Queen, King, Jack counted as 10.
6. Ace is counted as 11 or 1.
7. if 2 Ace, one should count as 1.
8. Whenever total of a hand goes above 21 for a player, Ace is counted as one. 
9. Check Bust condition (total of hand > 21).
10. Dealer hits until < 17, stays >= 17.
11. Decide winner-- comapring totals of both the players if total of both is less than equal to 21.
12. if one player busts, other automatically wins.
13. if total of both players is equal, its a push (even if, initial hands of both totals to 21).
14. In case of Double Down choosen by The Player, draw only one card.


Note:
=====

•	A four deck shoe is used. 
•	Each chip is $1.
•	The player may double down on any initial two card hand. 

Design:
======

Suits.java -- enum (Clubs, Diamonds, Hearts, Spades)

Cards.java -- Cards class initialises a card in deck and uses Suits class for each card.

Deck.java -- This class initialises a Deck which includes many (52 or more) cards (from Cards class).

Players.java -- This class initialises a Player and deals cards from Deck etc.

GameMain.java -- main class containing main function and game mechanics.


