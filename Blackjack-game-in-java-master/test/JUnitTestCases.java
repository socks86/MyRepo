import blackjackgame.GameMain;
import org.junit.jupiter.api.*;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class JUnitTestCases {
    private static final PrintStream DEFAULT_STDOUT = System.out;
    private static final InputStream DEFAULT_STDIN = System.in;
    private static ByteArrayOutputStream outContent;

    //References for input mocking.
    //https://stackoverflow.com/questions/52432876/how-to-mock-system-in
    //https://www.danvega.dev/blog/2020/12/16/testing-standard-in-out-java/

    @BeforeEach
    static void setupStreams(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    static void rollbackChangesToStdin() {
        System.setOut(DEFAULT_STDOUT);
        System.setIn(DEFAULT_STDIN);
    }

    @Test
    public void BetAmountNotGreaterThanBalance(){
        String userInput = "Test\nD\n101\nE\nN";//"Dan%sVega%sdanvega@gmail.com"
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        System.setIn(bais);

        String expected = "Your bet amount is wrong, it should be a natural number and should not exceed your balance";

        GameMain.main(new String[0]);// start the game

        String[] lines = outContent.toString().split(System.lineSeparator());
        String actual = lines[12];

        //checkout output
        Assert.assertEquals(expected,actual);
    }
}
