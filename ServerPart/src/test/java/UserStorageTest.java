import model.message.IMessage;
import model.settings.ISettings;
import services.constant.CommandsList;
import repository.storage.UsersStorage;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import repository.logger.ILogger;
import repository.IRepository;
import view.IAdmin;

import java.io.IOException;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStorageTest {

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
    public void newUserTest() throws IOException {
//        arrange
        String username = "username";
        String password = "password";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn("[]");
        IAdmin adminMock = Mockito.mock(IAdmin.class);
        UsersStorage result = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        act
        result.newUser(username, password);
//        assert
        Assertions.assertTrue(result.isExist(username));
        Assertions.assertTrue(result.passwordIsValid(username, password));
    }

    @Test
    public void isExistTest() throws IOException {
//        arrange
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        UsersStorage result = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        assert
        Assertions.assertTrue(result.isExist("Serg"));
        Assertions.assertTrue(result.isExist("Petya"));
        Assertions.assertTrue(result.isExist("DDD"));
        Assertions.assertTrue(result.isExist("Vasya"));
        Assertions.assertTrue(result.isExist("Ira"));
        Assertions.assertFalse(result.isExist("Kolya"));
        Assertions.assertFalse(result.isExist(null));
    }

    @Test
    public void passwordIsValidTest() throws IOException {
//        arrange
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        UsersStorage result = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        assert
        Assertions.assertTrue(result.passwordIsValid("Serg", "12345"));
        Assertions.assertTrue(result.passwordIsValid("Petya", "00000"));
        Assertions.assertTrue(result.passwordIsValid("DDD", "kkkkk"));
        Assertions.assertTrue(result.passwordIsValid("Vasya", "$$$$$"));
        Assertions.assertTrue(result.passwordIsValid("Ira", "12345"));
        Assertions.assertFalse(result.passwordIsValid("Kolya", "12345"));
        Assertions.assertFalse(result.passwordIsValid("Vasya", "12345"));
        Assertions.assertFalse(result.passwordIsValid(null, "12345"));
        Assertions.assertFalse(result.passwordIsValid("Vasya", null));
    }

    @Test
    public void messageSyncrTest() throws IOException, ParseException {
//        arrange
        String login = "login";
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).messageSyncr(login, settingsMock);
//        assert
        Mockito.verify(repositoryMock, Mockito.times(1)).getUserMessages(login, settingsMock);
    }

    @Test
    public void newMessageWithExitTitleTest() throws IOException {
//        arrange
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
        IMessage messageMock = Mockito.mock(IMessage.class);
        Mockito.when(messageMock.getTitle()).thenReturn(CommandsList.EXIT.command());
//        act
        new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        assert
        Mockito.verify(loggerMock, Mockito.times(0)).log(Mockito.anyString());
        Mockito.verify(messageMock, Mockito.times(0)).getContent();
        Mockito.verify(repositoryMock, Mockito.times(0))
                .saveUserMessage(Mockito.anyString(), Mockito.anyString(), Mockito.any(ISettings.class));
    }

    @Test
    public void newMessageWithoutExitTitleTest() throws IOException {
//        arrange
        String login = "Serg";
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
        IMessage messageMock = Mockito.mock(IMessage.class);
        Mockito.when(messageMock.getTitle()).thenReturn(Mockito.anyString());
//        act
        new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).newMessage(login, messageMock, settingsMock);
//        assert
        Mockito.verify(loggerMock, Mockito.times(1)).log(messageMock);
        Mockito.verify(messageMock, Mockito.times(1)).getContent();
        Mockito.verify(repositoryMock, Mockito.times(5))
                .saveUserMessage(Mockito.any(), Mockito.any(), Mockito.any(ISettings.class));
    }

    @ParameterizedTest
    @MethodSource("parametersForMessageListIsEmptyTest")
    public void messageListIsEmptyTest(String login, String jsonUsers, boolean expected) throws IOException {
//        arrange
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        boolean result = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).messageListIsEmpty(login);
//        assert
        Assertions.assertEquals(result, expected);
    }

    private Stream<Arguments> parametersForMessageListIsEmptyTest() {
        String userWithEmptyMessageList = "[{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}]";
        String userWithMessage = "[{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[" +
                "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}" +
                "]}]";
        return Stream.of(
                Arguments.of("Serg", userWithEmptyMessageList, true),
                Arguments.of("Serg", userWithMessage, false)
        );
    }

    @Test
    public void nextMessageTest() throws IOException {
//        arrange
        String login = "Serg";
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        String jsonWithMessage = "[{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[" +
                jsonMessage +
                "]}]";
        String title = "title";
        String content = "content";
        String localDateTime = "localDateTime";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonWithMessage);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
        UsersStorage usersStorage = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        act
        IMessage result = usersStorage.nextMessage(login, settingsMock);
        IMessage nullResult = usersStorage.nextMessage(login, settingsMock);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertEquals(result.getDateTime(), localDateTime);
        Assertions.assertNull(nullResult);
        Mockito.verify(repositoryMock, Mockito.times(1)).saveUserMessage(login, jsonMessage, settingsMock);
        Mockito.verify(loggerMock, Mockito.times(0)).log(Mockito.any(String.class));
    }

    @Test
    public void nextMessageWithIOExeptionTest() throws IOException {
//        arrange
        String login = "Serg";
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        String jsonWithMessage = "[{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[" +
                jsonMessage +
                "]}]";
        String title = "title";
        String content = "content";
        String localDateTime = "localDateTime";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        IOException exception = new IOException();
        Mockito.doThrow(exception).when(repositoryMock).saveUserMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonWithMessage);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
        UsersStorage usersStorage = new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock);
//        act
        IMessage result = usersStorage.nextMessage(login, settingsMock);
        IMessage nullResult = usersStorage.nextMessage(login, settingsMock);
//        assert
        Assertions.assertEquals(result.getTitle(), title);
        Assertions.assertEquals(result.getContent(), content);
        Assertions.assertEquals(result.getDateTime(), localDateTime);
        Assertions.assertNull(nullResult);
        Mockito.verify(repositoryMock, Mockito.times(1)).saveUserMessage(login, jsonMessage, settingsMock);
        Mockito.verify(loggerMock, Mockito.times(1)).log(exception.getMessage());
    }

    @Test
    public void closeTest() throws IOException {
//        arrange
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[" + jsonMessage + "]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).close(settingsMock);
//        assert
        Mockito.verify(repositoryMock, Mockito.times(1))
                .saveUserMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(repositoryMock, Mockito.times(1)).saveUsersJson(Mockito.any(), Mockito.any());
        Mockito.verify(loggerMock, Mockito.times(0)).log(Mockito.any(String.class));
        Mockito.verify(loggerMock, Mockito.times(0)).log(Mockito.any(IMessage.class));
    }

    @Test
    public void closeWithIOExeptionTest() throws IOException {
//        arrange
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        String jsonUsers = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[" + jsonMessage + "]}," +
                "{\"username\":\"Petya\",\"password\":\"00000\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"kkkkk\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"$$$$$\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        IOException exception = new IOException();
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.when(repositoryMock.getUsersJson(settingsMock)).thenReturn(jsonUsers);
        Mockito.doThrow(exception).when(repositoryMock).saveUsersJson(Mockito.any(), Mockito.any());
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        assert
        Assertions.assertThrows(IOException.class,
                () -> new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).close(settingsMock));
        Mockito.verify(repositoryMock, Mockito.times(1))
                .saveUserMessage(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(repositoryMock, Mockito.times(1)).saveUsersJson(Mockito.any(), Mockito.any());
    }

    @Test
    public void constructorIOExeptionTest() throws IOException {
//        arrange
        IOException exception = new IOException();
        ILogger loggerMock = Mockito.mock(ILogger.class);
        ISettings settingsMock = Mockito.mock(ISettings.class);
        IRepository repositoryMock = Mockito.mock(IRepository.class);
        Mockito.doThrow(exception).when(repositoryMock).getUsersJson(settingsMock);
        IAdmin adminMock = Mockito.mock(IAdmin.class);
//        act
        new UsersStorage(loggerMock, repositoryMock, settingsMock, adminMock).close(settingsMock);
//        assert
        Mockito.verify(loggerMock, Mockito.times(1)).log(exception.getMessage());
        Mockito.verify(adminMock, Mockito.times(1)).printMessage(exception.getMessage());
    }
}
