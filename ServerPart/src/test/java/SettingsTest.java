import model.Settings;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import services.ISettings;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SettingsTest {

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
    public void getSettingsFromJsonTest() throws ParseException {
//        arrange
        String json = "[{" +
                "\"port\":\"23\"," +
                "\"pathLogFile\":\"ServerPart/src/main/resources/log.txt\"," +
                "\"pathUsersFile\":\"ServerPart/src/main/resources/users.json\"," +
                "\"pathUsersMessages\":\"ServerPart/src/main/resources/messages/\"" +
                "}]";
        int port = 23;
        String pathLogFile = "ServerPart/src/main/resources/log.txt";
        String pathUsersFile = "ServerPart/src/main/resources/users.json";
        String pathUsersMessages = "ServerPart/src/main/resources/messages/";
//        act
        ISettings result = new Settings(json);
//        assert
        Assertions.assertEquals(result.getPort(), port);
        Assertions.assertEquals(result.getPathLogFile(), pathLogFile);
        Assertions.assertEquals(result.getPathUsersFile(), pathUsersFile);
        Assertions.assertEquals(result.getPathUsersMessages(), pathUsersMessages);
    }

    @ParameterizedTest
    @MethodSource("parametersForGetSettingsFromJsonWithInvalidArgumentTest")
    public void getSettingsFromJsonWithInvalidArgumentTest(String json) {
        Assertions.assertThrows(ClassCastException.class, () -> new Settings(json));
    }

    private Stream<Arguments> parametersForGetSettingsFromJsonWithInvalidArgumentTest() {
        return Stream.of(
                Arguments.of("{}"),
                Arguments.of("22")
        );
    }

    @Test
    public void getSettingsFromJsonWithEmptyStingTest() {
//        arrange
        String json = "";
//        assert
        Assertions.assertThrows(ParseException.class, () -> new Settings(json));
    }

    @Test
    public void getSettingsFromJsonWithNullTest() {
//        arrange
        String json = null;
//        assert
        Assertions.assertThrows(NullPointerException.class, () -> new Settings(json));
    }

    @Test
    public void getPortTest() throws ParseException {
//        arrange
        String json = "[{" +
                "\"port\":\"23\"," +
                "\"pathLogFile\":\"ServerPart/src/main/resources/log.txt\"," +
                "\"pathUsersFile\":\"ServerPart/src/main/resources/users.json\"," +
                "\"pathUsersMessages\":\"ServerPart/src/main/resources/messages/\"" +
                "}]";
        int expected = 23;
//        act
        ISettings result = new Settings(json);
//        assert
        Assertions.assertEquals(result.getPort(), expected);
    }

    @Test
    public void getPathLogFileTest() throws ParseException {
//        arrange
        String json = "[{" +
                "\"port\":\"23\"," +
                "\"pathLogFile\":\"ServerPart/src/main/resources/log.txt\"," +
                "\"pathUsersFile\":\"ServerPart/src/main/resources/users.json\"," +
                "\"pathUsersMessages\":\"ServerPart/src/main/resources/messages/\"" +
                "}]";
        String expected = "ServerPart/src/main/resources/log.txt";
//        act
        ISettings result = new Settings(json);
//        assert
        Assertions.assertEquals(result.getPathLogFile(), expected);
    }

    @Test
    public void getPathUsersFileTest() throws ParseException {
//        arrange
        String json = "[{" +
                "\"port\":\"23\"," +
                "\"pathLogFile\":\"ServerPart/src/main/resources/log.txt\"," +
                "\"pathUsersFile\":\"ServerPart/src/main/resources/users.json\"," +
                "\"pathUsersMessages\":\"ServerPart/src/main/resources/messages/\"" +
                "}]";
        String expected = "ServerPart/src/main/resources/users.json";
//        act
        ISettings result = new Settings(json);
//        assert
        Assertions.assertEquals(result.getPathUsersFile(), expected);
    }

    @Test
    public void getPathUsersMessagesTest() throws ParseException {
//        arrange
        String json = "[{" +
                "\"port\":\"23\"," +
                "\"pathLogFile\":\"ServerPart/src/main/resources/log.txt\"," +
                "\"pathUsersFile\":\"ServerPart/src/main/resources/users.json\"," +
                "\"pathUsersMessages\":\"ServerPart/src/main/resources/messages/\"" +
                "}]";
        String expected = "ServerPart/src/main/resources/messages/";
//        act
        ISettings result = new Settings(json);
//        assert
        Assertions.assertEquals(result.getPathUsersMessages(), expected);
    }
}
