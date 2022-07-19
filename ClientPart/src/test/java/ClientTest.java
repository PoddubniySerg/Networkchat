import model.response.IResponse;
import repository.logger.ILogger;
import services.client.Client;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import services.server.IServer;
import view.IView;

import java.io.IOException;

public class ClientTest {

    private static final String EXIT_COMMAND = "/exit";
    private static final String VALID_COMMAND = "/ok";

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
    public void testIsLoggedMethodWithNullResponse() throws IOException {
//        arrange
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenReturn(null);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }

    @Test
    public void testIsLoggedMethodWithNullTitle() throws IOException {
//        arrange
        IResponse responseMock = Mockito.mock(IResponse.class);
        Mockito.when(responseMock.getTitle()).thenReturn(null);
        Mockito.when(responseMock.getContent()).thenReturn(VALID_COMMAND);
        Mockito.when(responseMock.getDateTime()).thenReturn(VALID_COMMAND);
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenReturn(responseMock);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }

    @Test
    public void testIsLoggedMethodWithNullContent() throws IOException {
//        arrange
        IResponse responseMock = Mockito.mock(IResponse.class);
        Mockito.when(responseMock.getTitle()).thenReturn(VALID_COMMAND);
        Mockito.when(responseMock.getContent()).thenReturn(null);
        Mockito.when(responseMock.getDateTime()).thenReturn(VALID_COMMAND);
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenReturn(responseMock);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }

    @Test
    public void testIsLoggedMethodWithNullDate() throws IOException {
//        arrange
        IResponse responseMock = Mockito.mock(IResponse.class);
        Mockito.when(responseMock.getTitle()).thenReturn(VALID_COMMAND);
        Mockito.when(responseMock.getContent()).thenReturn(VALID_COMMAND);
        Mockito.when(responseMock.getDateTime()).thenReturn(null);
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenReturn(responseMock);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }

    @Test
    public void testIsLoggedMethodWithExitTitle() throws IOException {
//        arrange
        IResponse responseMock = Mockito.mock(IResponse.class);
        Mockito.when(responseMock.getTitle()).thenReturn(EXIT_COMMAND);
        Mockito.when(responseMock.getContent()).thenReturn(VALID_COMMAND);
        Mockito.when(responseMock.getDateTime()).thenReturn(VALID_COMMAND);
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenReturn(responseMock);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }

    @Test
    public void testIsLoggedMethodTrowExeption() throws IOException {
//        arrange
        IView viewMock = Mockito.mock(IView.class);
        IServer serverMock = Mockito.mock(IServer.class);
        ILogger loggerMock = Mockito.mock(ILogger.class);
        Mockito.when(serverMock.serverIsConnected()).thenReturn(true);
        Mockito.when(serverMock.getResponse()).thenThrow(IOException.class);
//        act
        new Client(loggerMock, viewMock).start(serverMock);
//        assert
        Mockito.verify(serverMock, Mockito.times(1)).sendRequest("/authorization");
        Mockito.verify(serverMock, Mockito.times(1)).getResponse();
        Mockito.verify(loggerMock, Mockito.times(4)).log((String) Mockito.any());
        Mockito.verify(viewMock, Mockito.times(4)).printMessage(Mockito.any());
        Mockito.verify(loggerMock, Mockito.times(1)).log("Stop the client without authorization");
        Mockito.verify(viewMock, Mockito.times(1)).printMessage("Stop the client without authorization");
        Mockito.verify(serverMock, Mockito.times(1)).close();
        Mockito.verify(viewMock, Mockito.times(1)).closeView();
    }
}
