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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String actual = lines[12];

        //checkout output
        Assert.assertEquals(actual, expected);
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
        String actual = lines[12];

        //checkout output
        Assert.assertEquals(actual, expected);
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
        String userInput = "50\nDD";
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);

        gameMain.dealTheGame();

        String expected = "Hit or Stay or Double Down? [Enter H or S or DD]";
        String[] lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        String actual = lines[10];
        Assert.assertEquals(actual, expected);


        userInput = "100\nDD";
        bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);

        outContent.reset();

        gameMain.dealTheGame();

        expected = "Hit or Stay? [Enter H or S(or press any letter to Stay)]";
        lines = outContent.toString().split("\n");
        lines = Arrays.stream(lines)
                .filter(value -> value != null && !value.equals("\r") && value.length() > 0)
                .map(c -> c.replaceAll("\t",""))
                .map(c -> c.replaceAll("\r",""))
                .map(c -> c.replaceAll("\n",""))
                .toArray(String[]::new);
        actual = lines[10];
        Assert.assertEquals(actual, expected);
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

    /*
    public static void main(String[] args) {
        GameMain gameMain = new GameMain();
        gameMain.setPlayerName("Test");
        gameMain.setBalance(100);

        gameMain.dealTheGame();
    }*/
}
