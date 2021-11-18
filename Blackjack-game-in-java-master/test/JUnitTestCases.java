import blackjackgame.Cards;
import blackjackgame.GameMain;
import blackjackgame.Players;
import blackjackgame.Suits;
import org.junit.jupiter.api.*;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

/*
Author: Justin Lesh
 */

public class JUnitTestCases {
    private static final PrintStream DEFAULT_STDOUT = System.out;
    private static final InputStream DEFAULT_STDIN = System.in;
    private ByteArrayOutputStream outContent;

    //References for input mocking.
    //https://stackoverflow.com/questions/52432876/how-to-mock-system-in
    //https://www.danvega.dev/blog/2020/12/16/testing-standard-in-out-java/

    @BeforeEach
    void setupStreams(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void rollbackChangesToStdin() {
        System.setOut(DEFAULT_STDOUT);
        System.setIn(DEFAULT_STDIN);
    }

    @Test //Test case 1
    public void BetAmountNotGreaterThanBalance(){
        String userInput = "Test\nD\n101\nE\nN";//"Dan%sVega%sdanvega@gmail.com"
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        System.setIn(bais);

        String expected = "Your bet amount is wrong, it should be a natural number and should not exceed your balance";

        GameMain.main(new String[0]);// start the game

        String[] lines = outContent.toString().split(System.lineSeparator());
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);

        //checkout output
        Assert.assertTrue(Arrays.asList(lines).contains(expected));
    }

    @Test //Test case 2
    public void BetAmountIsNaturalNumber(){
        String userInput = "Test\nD\na\n101\nE\nN";//"Dan%sVega%sdanvega@gmail.com"
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        System.setIn(bais);

        String expected = "Enter your bet in Integers (natural numbers) please:";

        GameMain.main(new String[0]);// start the game

        String[] lines = outContent.toString().split(System.lineSeparator());
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);

        //checkout output
        Assert.assertTrue(Arrays.asList(lines).contains(expected));
    }

    @Test //Test case 3
    public void BalanceIsZeroGameEnd(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(0);
        Assert.assertTrue(gameMain.gameLoop(false));
    }

    @Test //Test case 4
    public void CheckForDoubleDownOption(){
        GameMain gameMain;
        String[] lines;
        String expected;
        do {
            String userInput = "50\nDD";
            ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
            System.setIn(bais);
            outContent.reset();
            gameMain = new GameMain();
            gameMain.setPlayerName("Test");
            gameMain.setBalance(100);

            gameMain.dealTheGame();

            expected = "Hit or Stay or Double Down? [Enter H or S or DD]";
            lines = outContent.toString().split("\n");
            lines = Arrays.stream(lines)
                    .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                    .map(c -> c.replaceAll("\t",""))
                    .map(c -> c.replaceAll("\r",""))
                    .map(c -> c.replaceAll("\n",""))
                    .toArray(String[]::new);
        }while(Arrays.asList(lines).contains("# HURRAY!!...BLACKJACK, YOU WON #"));

        Assert.assertTrue(Arrays.asList(lines).contains(expected));

        do{
            String userInput = "100\nDD";
            ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
            System.setIn(bais);

            outContent.reset();
            gameMain = new GameMain();
            gameMain.setPlayerName("Test");
            gameMain.setBalance(100);

            gameMain.dealTheGame();

            expected = "Hit or Stay? [Enter H or S(or press any letter to Stay)]";
            lines = outContent.toString().split("\n");
            lines = Arrays.stream(lines)
                    .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                    .map(c -> c.replaceAll("\t",""))
                    .map(c -> c.replaceAll("\r",""))
                    .map(c -> c.replaceAll("\n",""))
                    .toArray(String[]::new);
        }while(Arrays.asList(lines).contains("# HURRAY!!...BLACKJACK, YOU WON #"));

        Assert.assertTrue(Arrays.asList(lines).contains(expected));
    }

    @Test //Test case 5
    public void FaceCardsAreCountedAs10(){
        Players player = new Players("Test");
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 11));//jack of clubs
        Assert.assertEquals(player.getPlayersHandTotal(), 10);

        player.emptyHand();
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 12));//queen of clubs
        Assert.assertEquals(player.getPlayersHandTotal(), 10);

        player.emptyHand();
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 13));//king of clubs
        Assert.assertEquals(player.getPlayersHandTotal(), 10);
    }

    @Test //Test case 6
    public void AceIsCountedAsElevenOrOne(){
        Players player = new Players("Test");
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 1));//ace of clubs
        player.getPlayersHandTotal();
        Assert.assertEquals(player.getPlayersHandTotal(), 11);

        player.addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        Assert.assertEquals(player.getPlayersHandTotal(), 21);

        player.addCardToPlayersHand(new Cards(Suits.Clubs, 5));
        Assert.assertEquals(player.getPlayersHandTotal(), 16);
    }

    @Test //Test case 7
    public void TwoAcesOnlyOneIsEleven(){
        Players player = new Players("Test");
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 1));//ace of clubs
        player.getPlayersHandTotal();
        Assert.assertEquals(player.getPlayersHandTotal(), 11);

        player.addCardToPlayersHand(new Cards(Suits.Clubs, 1));
        Assert.assertEquals(player.getPlayersHandTotal(), 12);
    }

    @Test //Test case 8
    public void Over21CountsAceAs1(){
        Players player = new Players("Test");
        player.addCardToPlayersHand(new Cards(Suits.Clubs, 1));//ace of clubs
        player.getPlayersHandTotal();
        Assert.assertEquals(player.getPlayersHandTotal(), 11);

        player.addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        Assert.assertEquals(player.getPlayersHandTotal(), 21);

        player.addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        Assert.assertEquals(player.getPlayersHandTotal(), 21);
    }

    @Test //Test case 9
    public void CheckBustConditions(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);

        gameMain.hit();
        gameMain.hit();
        gameMain.hit();
        gameMain.hit();
        gameMain.hit();

        String expected = "# YOU BUSTED #";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));
    }

    @Test //Test case 10
    public void CheckDealerStay17HitsElse(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");

        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));
        gameMain.dealersPlay();

        String expected = "Dealer Stays ";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));

        outContent.reset();

        gameMain = new GameMain();
        gameMain.setPlayerName("Test");

        gameMain.dealersPlay();
        expected = "Dealer Hits ";
        lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));
    }

    @Test //Test case 11
    public void DecideCorrectWinner(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);
        gameMain.setBet(1);

        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));

        gameMain.decideWinner();

        String expected = "# YOU WON  #";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//20 vs 17

        outContent.reset();

        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 4));

        gameMain.decideWinner();

        expected = "# YOU LOST #";
        lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//20 vs 21
    }

    @Test //Test case 12
    public void IfOneBustOtherWins(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);
        gameMain.setBet(1);

        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 5));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));

        gameMain.decideWinner();

        String expected = "# YOU WON  #";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//15 vs 24

        outContent.reset();

        gameMain.getDealer().emptyHand();
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 7));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));

        gameMain.decideWinner();

        expected = "# YOU LOST #";
        lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//22 vs 17
    }

    @Test //Test case 13
    public void WinnerIsPush(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);
        gameMain.setBet(1);

        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 7));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 7));

        gameMain.decideWinner();

        String expected = "#   PUSH   #";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//17 vs 17

        outContent.reset();

        gameMain.getDealer().emptyHand();
        gameMain.getYou().emptyHand();
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 1));
        gameMain.getYou().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 10));
        gameMain.getDealer().addCardToPlayersHand(new Cards(Suits.Clubs, 1));

        gameMain.decideWinner();

        expected = "#   PUSH   #";
        lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        Assert.assertTrue(Arrays.asList(lines).contains(expected));//21 vs 21
    }

    @Test //Test case 14
    public void DoubleDownOnlyDraws1Card(){
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");

        Assert.assertEquals(gameMain.getYou().getNumCardsInHand(), 0);

        gameMain.doubleDown();

        Assert.assertEquals(gameMain.getYou().getNumCardsInHand(), 1);
    }
}
