import model.NetServerResponseFactory;
import org.junit.jupiter.api.*;
import services.IResponse;
import services.IResponseFactory;

import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NetServerResponseFactoryTest {
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
    public void testNewResponse() {
//        arrange
        String title = "you";
        String content = "Any text";
        String dateTime = LocalDateTime.now().toString();
        IResponseFactory responseFactory = new NetServerResponseFactory();
//        act
        IResponse result = responseFactory.newResponse(title, content, dateTime);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertTrue(result.getDateTime() != null && !result.getDateTime().isEmpty());
    }
}