import model.Model;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import services.ILogger;
import services.IMessageFactory;
import services.INetClient;
import services.IView;

import java.io.IOException;

public class ModelTest {

    private static ILogger loggerMock = Mockito.mock(ILogger.class);
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
    public void testIsLoggedMethodTrue() throws IOException {
        IView viewMock = Mockito.mock(IView.class);
        IMessageFactory messageFactoryMock = Mockito.mock(IMessageFactory.class);
        INetClient iNetClientMock = Mockito.mock(INetClient.class);
        Mockito.when(iNetClientMock.sergverIsConnected()).thenReturn(true);
        Mockito.when(iNetClientMock.readLine()).thenReturn("OK!");
        Mockito.when(viewMock.getString()).thenReturn(Mockito.any());
        new Model(loggerMock, viewMock, messageFactoryMock).start(iNetClientMock);
    }
}
