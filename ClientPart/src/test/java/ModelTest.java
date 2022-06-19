import model.Client;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import services.ILogger;
import services.IResponseFactory;
import services.IServer;
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
        IResponseFactory messageFactoryMock = Mockito.mock(IResponseFactory.class);
        IServer iNetClientMock = Mockito.mock(IServer.class);
        Mockito.when(iNetClientMock.serverIsConnected()).thenReturn(true);

        Mockito.when(viewMock.getString()).thenReturn(Mockito.any());
        new Client(loggerMock, viewMock).start(iNetClientMock);
    }
}
