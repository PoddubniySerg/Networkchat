import model.NetServerResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import services.IResponse;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageTest {

    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Running MessageTest");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("MessageTest complete: " + (System.nanoTime() - suiteStartTime));
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new test");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("Test complete: " + (System.nanoTime() - testStartTime));
    }

    @Test
    public void testGetTitle() {
//        arrange
        String expected = "Serg";
        IResponse message = new NetServerResponse("Serg", "Hello", "2022-06-13T14:32:10.694341200");
//        act
        String result = message.getTitle();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testGetContent() {
//        arrange
        String expected = "Hello";
        IResponse message = new NetServerResponse("Serg", "Hello", "2022-06-13T14:32:10.694341200");
//        act
        String result = message.getContent();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testGetDateTime() {
//        arrange
        String expected = "2022-06-13T14:32:10.694341200";
        IResponse message = new NetServerResponse("Serg", "Hello", "2022-06-13T14:32:10.694341200");
//        act
        String result = message.getDateTime();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testToString() {
//        arrange
        String expected = "Serg\nHello\n2022-06-13T14:32:10.694341200\n";
        IResponse message = new NetServerResponse("Serg", "Hello", "2022-06-13T14:32:10.694341200");
//        act
        String result = message.toString();
//        assert
        Assertions.assertEquals(result, expected);
    }


    @ParameterizedTest
    @MethodSource("parametersForTestEqualsMethod")
    public void testEqualsMethod(IResponse message) {
//        arrange
        IResponse expected = new NetServerResponse("Serg", "Hello", "2022-06-13T14:32:10.694341200");
//        assert
        Assertions.assertNotEquals(message, expected);
    }

    private Stream<Arguments> parametersForTestEqualsMethod() {
        return Stream.of(
                Arguments.of(new NetServerResponse("", "Hello", "2022-06-13T14:32:10.694341200")),
                Arguments.of(new NetServerResponse("Serg", "", "2022-06-13T14:32:10.694341200")),
                Arguments.of(new NetServerResponse("Serg", "Hello", "")),
                Arguments.of(new NetServerResponse("", "", ""))
        );
    }
}
