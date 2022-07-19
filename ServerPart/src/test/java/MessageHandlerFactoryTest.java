import services.handler.message.MessageHandler;
import services.factory.MessageHandlerFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import services.handler.message.IMessageHendler;
import repository.storage.IStorage;

public class MessageHandlerFactoryTest {

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
    public void newClientServiceTest() {
//        arrange
        MessageHandlerFactory messageHandlerFactory = new MessageHandlerFactory();
        IStorage storage = Mockito.mock(IStorage.class);
        IMessageHendler expected = new MessageHandler(storage);
//        act
        IMessageHendler result = messageHandlerFactory.newClientService(storage);
//        assert
        Assertions.assertEquals(result, expected);
    }
}
