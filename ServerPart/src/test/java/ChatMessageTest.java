import com.google.gson.JsonSyntaxException;
import model.message.ChatMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageTest {

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
    public void getMessageFromJsonTest() {
//        arrange
        String json = "{\"null\":\"Serg\",\"content\":\"attached to chat\",\"localDateTime\":\"2022-06-19T16:25:22.877157\"}";
        String title = null;
        String content = "attached to chat";
        String dateTime = "2022-06-19T16:25:22.877157";
//        act
        ChatMessage result = ChatMessage.getMessageFromJson(json);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertEquals(result.getDateTime(), dateTime);
    }

    @ParameterizedTest
    @MethodSource("parametersForGetMessageFromJsonWithInvalidArgumentTest")
    public void getMessageFromJsonWithInvalidArgumentTest(String json) {
        Assertions.assertThrows(JsonSyntaxException.class, () -> ChatMessage.getMessageFromJson(json));
    }

    private Stream<Arguments> parametersForGetMessageFromJsonWithInvalidArgumentTest() {
        return Stream.of(
                Arguments.of("[]"),
                Arguments.of("22")
        );
    }

    @Test
    public void getMessageFromJsonWithEmptyStingTest() {
//        arrange
        String json = "";
//        act
        ChatMessage result = ChatMessage.getMessageFromJson(json);
//        assert
        Assertions.assertNull(result);
    }

    @Test
    public void getMessageFromJsonWithNullTest() {
//        arrange
        String json = null;
//        assert
        Assertions.assertNull(ChatMessage.getMessageFromJson(json));
    }

    @Test
    public void getTitleTest() {
//        arrange
        String expected = "title";
        ChatMessage message = ChatMessage.getMessageFromJson
                ("{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}");
//        act
        String result = message.getTitle();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getContentTest() {
//        arrange
        String expected = "content";
        ChatMessage message = ChatMessage.getMessageFromJson
                ("{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}");
//        act
        String result = message.getContent();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getDateTimeTest() {
//        arrange
        String expected = "localDateTime";
        ChatMessage message = ChatMessage.getMessageFromJson
                ("{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}");
//        act
        String result = message.getDateTime();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getJsonTest() {
//        arrange
        String exspected = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        ChatMessage message = ChatMessage.getMessageFromJson(exspected);
//        act
        String result = message.getJson();
//        assert
        Assertions.assertEquals(result, exspected);
    }
}
