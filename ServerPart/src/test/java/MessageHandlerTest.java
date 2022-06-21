import model.CommandsList;
import model.MessageHandler;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import services.*;

import java.io.IOException;

public class MessageHandlerTest {

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
    public void authorizationWithClientServisClosedTest() throws IOException, ParseException {
//        arrange
        IStorage storageMock = Mockito.mock(IStorage.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(true);
//        act
        new MessageHandler(storageMock).start(clientServiceMock, settingsMock);
//        assert
        Mockito.verify(clientServiceMock, Mockito.times(0)).getClientRequest(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(0)).getClientRequest(Mockito.any(), Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(0)).sendResponse(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(1)).serverIsClosed();
        Mockito.verify(storageMock, Mockito.times(0)).messageSyncr(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).close(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).messageListIsEmpty(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).setUserStatusOnline(Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(storageMock, Mockito.times(0)).isExist(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).newUser(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).newMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).nextMessage(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).passwordIsValid(Mockito.any(), Mockito.any());
        Mockito.verify(settingsMock, Mockito.times(0)).getPort();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathLogFile();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersMessages();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersFile();
    }

    @Test
    public void authorizationWithExitCommandTest() throws IOException, ParseException {
//        arrange
        IMessage requestMock = Mockito.mock(IMessage.class);
        Mockito.when(requestMock.getTitle()).thenReturn(CommandsList.EXIT.command());
        IStorage storageMock = Mockito.mock(IStorage.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(false);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.any())).thenReturn(requestMock);
//        act
        new MessageHandler(storageMock).start(clientServiceMock, settingsMock);
//        assert
        Mockito.verify(clientServiceMock, Mockito.times(1)).getClientRequest(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(0)).getClientRequest(Mockito.any(), Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(0)).sendResponse(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(1)).serverIsClosed();
        Mockito.verify(storageMock, Mockito.times(0)).messageSyncr(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).close(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).messageListIsEmpty(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).setUserStatusOnline(Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(storageMock, Mockito.times(0)).isExist(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).newUser(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).newMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).nextMessage(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).passwordIsValid(Mockito.any(), Mockito.any());
        Mockito.verify(settingsMock, Mockito.times(0)).getPort();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathLogFile();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersMessages();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersFile();
    }
    @Test
    public void authorizationWithRegistrationCommandTest() throws IOException, ParseException {
//        arrange
        String login = CommandsList.EXIT.command();
        IMessage requestMock = Mockito.mock(IMessage.class);
        Mockito.when(requestMock.getTitle()).thenReturn(CommandsList.REGISTRATION.command());
        Mockito.when(requestMock.getContent()).thenReturn(login);
        IStorage storageMock = Mockito.mock(IStorage.class);
        Mockito.when(storageMock.isExist(Mockito.any())).thenReturn(false);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(false);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.any())).thenReturn(requestMock);
//        act
        new MessageHandler(storageMock).start(clientServiceMock, settingsMock);
//        assert
        Mockito.verify(clientServiceMock, Mockito.times(3)).getClientRequest(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(0)).getClientRequest(Mockito.any(), Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(3)).sendResponse(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(1)).serverIsClosed();
        Mockito.verify(storageMock, Mockito.times(1)).messageSyncr(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).close(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).messageListIsEmpty(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(1)).setUserStatusOnline(requestMock.getContent(), true);
        Mockito.verify(storageMock, Mockito.times(1)).isExist(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(1)).newUser(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(2)).newMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).nextMessage(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).passwordIsValid(Mockito.any(), Mockito.any());
        Mockito.verify(settingsMock, Mockito.times(0)).getPort();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathLogFile();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersMessages();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersFile();
    }

    @Test
    public void authorizationWithValidUserTest() throws IOException, ParseException {
//        arrange
        String login = CommandsList.EXIT.command();
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IMessage requestMock = Mockito.mock(IMessage.class);
        Mockito.when(requestMock.getTitle()).thenReturn(CommandsList.MESSAGE.command());
        Mockito.when(requestMock.getContent()).thenReturn(login);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(false);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.any())).thenReturn(requestMock);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.anyString(), Mockito.any(IMessageFactory.class))).thenReturn(requestMock);
        IStorage storageMock = Mockito.mock(IStorage.class);
        Mockito.when(storageMock.isExist(Mockito.any())).thenReturn(true);
        Mockito.when(storageMock.passwordIsValid(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
//        act
        new MessageHandler(storageMock).start(clientServiceMock, settingsMock);
//        assert
        Mockito.verify(clientServiceMock, Mockito.times(1)).getClientRequest(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(1)).getClientRequest(Mockito.any(), Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(2)).sendResponse(Mockito.any());
        Mockito.verify(clientServiceMock, Mockito.times(1)).serverIsClosed();
        Mockito.verify(storageMock, Mockito.times(1)).messageSyncr(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).close(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).messageListIsEmpty(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(1)).setUserStatusOnline(requestMock.getContent(), true);
        Mockito.verify(storageMock, Mockito.times(1)).isExist(Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).newUser(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(1)).newMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(0)).nextMessage(Mockito.any(), Mockito.any());
        Mockito.verify(storageMock, Mockito.times(1)).passwordIsValid(Mockito.any(), Mockito.any());
        Mockito.verify(settingsMock, Mockito.times(0)).getPort();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathLogFile();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersMessages();
        Mockito.verify(settingsMock, Mockito.times(0)).getPathUsersFile();
    }

    @Test
    public void authorizationWithIOExceptionTest() throws IOException, ParseException {
//        arrange
        String login = CommandsList.EXIT.command();
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IMessage requestMock = Mockito.mock(IMessage.class);
        Mockito.when(requestMock.getTitle()).thenReturn(CommandsList.MESSAGE.command());
        Mockito.when(requestMock.getContent()).thenReturn(login);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(false);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.any())).thenReturn(requestMock);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.anyString(), Mockito.any(IMessageFactory.class))).thenReturn(requestMock);
        IStorage storageMock = Mockito.mock(IStorage.class);
        Mockito.when(storageMock.isExist(Mockito.any())).thenReturn(true);
        Mockito.when(storageMock.passwordIsValid(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.doThrow(new IOException()).when(storageMock).messageSyncr(login, settingsMock);
//        assert
        Assertions.assertThrows(IOException.class, () -> new MessageHandler(storageMock).start(clientServiceMock, settingsMock));
    }

    @Test
    public void authorizationWithParseExceptionTest() throws IOException, ParseException {
//        arrange
        String login = CommandsList.EXIT.command();
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IMessage requestMock = Mockito.mock(IMessage.class);
        Mockito.when(requestMock.getTitle()).thenReturn(CommandsList.MESSAGE.command());
        Mockito.when(requestMock.getContent()).thenReturn(login);
        IClientService clientServiceMock = Mockito.mock(IClientService.class);
        Mockito.when(clientServiceMock.serverIsClosed()).thenReturn(false);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.any())).thenReturn(requestMock);
        Mockito.when(clientServiceMock.getClientRequest(Mockito.anyString(), Mockito.any(IMessageFactory.class))).thenReturn(requestMock);
        IStorage storageMock = Mockito.mock(IStorage.class);
        Mockito.when(storageMock.isExist(Mockito.any())).thenReturn(true);
        Mockito.when(storageMock.passwordIsValid(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.doThrow(ParseException.class).when(storageMock).messageSyncr(login, settingsMock);
//        assert
        Assertions.assertThrows(ParseException.class, () -> new MessageHandler(storageMock).start(clientServiceMock, settingsMock));
    }
}