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
    public void testGetSettingsFromJson() throws ParseException {
//        arrange
        String settingsStr = "[{\"host\":\"127.0.0.1\",\"port\":\"23\",\"pathLogFile\":\"ClientPart/src/main/resources/log.txt\"}]";
        Settings expected = new Settings(23, "ClientPart/src/main/resources/log.txt", "127.0.0.1");
//        act
        Settings result = Settings.getSettingsFromJson(settingsStr);
//        assert
        Assertions.assertEquals(result, expected);
    }


    @ParameterizedTest
    @MethodSource("parametersForTestGetSettingsFromJsonWithIncorrectInput")
    public void testGetSettingsFromJsonWithIncorrectInput(String jsonString) {
//        assert
        Assertions.assertThrows(ParseException.class, () -> Settings.getSettingsFromJson(jsonString));
    }

    private Stream<Arguments> parametersForTestGetSettingsFromJsonWithIncorrectInput() {
        String nullValue = null;
        return Stream.of(
                Arguments.of(nullValue),
                Arguments.of("")
        );
    }

    @Test
    public void testGetPort() {
//        arrange
        int expected = 23;
        ISettings settings = new Settings(23, "ClientPart/src/main/resources/log.txt", "localhost");
//        act
        int result = settings.getPort();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testGetPathLogFile() {
//        arrange
        String expected = "ClientPart/src/main/resources/log.txt";
        ISettings settings = new Settings(23, "ClientPart/src/main/resources/log.txt", "localhost");
//        act
        String result = settings.getPathLogFile();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testGetHost() {
//        arrange
        String expected = "localhost";
        ISettings settings = new Settings(23, "ClientPart/src/main/resources/log.txt", "localhost");
//        act
        String result = settings.getHost();
//        assert
        Assertions.assertEquals(result, expected);
    }
}
