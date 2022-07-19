import services.factory.ChatMessageFactory;
import org.junit.jupiter.api.*;
import model.message.IMessage;

public class ChatMessageFactoryTest {

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
    public void newMessageTest() {
//        arrange
        ChatMessageFactory messageFactory = new ChatMessageFactory();
        String title = "title";
        String content = "content";
//        act
        IMessage result = messageFactory.newMessage(title, content);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertNotNull(result.getDateTime());
        Assertions.assertFalse(result.getDateTime().isEmpty());
    }
}
