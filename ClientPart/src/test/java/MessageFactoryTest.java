import model.Message;
import model.MessageFactory;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import services.IMessage;
import services.IMessageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageFactoryTest {
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
    public void testGetMessagesFromJson() throws ParseException {
//        arrange
        String jsonMessage = "[" +
                "{\"title\":\"Serg\",\"content\":\"is attached to chat\",\"localDateTime\":\"2022-06-13T14:31:54.178941900\"}," +
                "{\"title\":\"Serg\",\"content\":\"Hello\",\"localDateTime\":\"2022-06-13T14:32:10.694341200\"}," +
                "{\"title\":\"Serg\",\"content\":\"leave chat\",\"localDateTime\":\"2022-06-13T14:32:18.769383\"}]";
        List<Message> expected = new ArrayList<>();
        expected.add(new Message("Serg", "is attached to chat", "2022-06-13T14:31:54.178941900"));
        expected.add(new Message("Serg", "Hello", "2022-06-13T14:32:10.694341200"));
        expected.add(new Message("Serg", "leave chat", "2022-06-13T14:32:18.769383"));
//        act
        List<IMessage> result = new MessageFactory().getMessagesFromJson(jsonMessage);
//        assert
        for (int i = 0; i < result.size(); i++) {
            Assertions.assertEquals(result.get(i), expected.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("parametersForTestGetMessagesFromJson")
    public void testGetMessagesFromJsonWithNullAndEmptyList(String jsonStr) throws ParseException {
//        act
        List<IMessage> result = new MessageFactory().getMessagesFromJson(jsonStr);
//        assert
        Assertions.assertTrue(result.isEmpty());
    }

    private Stream<Arguments> parametersForTestGetMessagesFromJson() {
        String nullValue = null;
        return Stream.of(
                Arguments.of(nullValue),
                Arguments.of("[]")
        );
    }

    @Test
    public void testGetMessagesFromJsonWithIncorrectInputData() {
//        arrange
        String inputData = "";
//        assert
        Assertions.assertThrows(ParseException.class, () -> new MessageFactory().getMessagesFromJson(inputData));
    }

    @Test
    public void testNewMessage() {
//        arrange
        String title = "you";
        String content = "Any text";
        IMessageFactory messageFactory = new MessageFactory();
//        act
        IMessage result = messageFactory.newMessage(content);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertTrue(result.getDateTime() != null && !result.getDateTime().isEmpty());
    }
}