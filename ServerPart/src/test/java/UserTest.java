import model.ChatMessage;
import model.User;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import services.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {

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
    public void newUserTest() {
//        arrange
        String username = "username";
        String password = "password";
//        act
        User result = User.newUser(username, password);
//        assert
        Assertions.assertEquals(result.getUsername(), username);
        Assertions.assertEquals(result.getPassword(), password);
        Assertions.assertFalse(result.isUserOnline());
        Assertions.assertTrue(result.messageListIsEmpty());
        Assertions.assertNull(result.nextMessage());
    }

    @Test
    public void usersFromJsonTest() throws ParseException {
//        arrange
        String json = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        int expectedCount = 5;
        User expected = new User("Serg", "12345");
//        act
        List<User> result = User.usersFromJson(json);
//        assert
        Assertions.assertEquals(result.size(), expectedCount);
        Assertions.assertEquals(result.get(0), expected);
        Assertions.assertFalse(result.get(0).isUserOnline());
        Assertions.assertTrue(result.get(0).messageListIsEmpty());
        Assertions.assertNull(result.get(0).nextMessage());
    }

    @ParameterizedTest
    @MethodSource("parametersForUsersFromJsonFromJsonWithInvalidArgumentTest")
    public void usersFromJsonWithInvalidArgumentTest(String json) {
        Assertions.assertThrows(ClassCastException.class, () -> User.usersFromJson(json));
    }

    private Stream<Arguments> parametersForUsersFromJsonFromJsonWithInvalidArgumentTest() {
        return Stream.of(
                Arguments.of("{}"),
                Arguments.of("22")
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForUsersFromJsonWithParseExceptionTest")
    public void usersFromJsonWithParseExceptionTest(String json) {
//        act
//        List<User> result = User.usersFromJson(json);
//        assert
        Assertions.assertThrows(ParseException.class, () -> User.usersFromJson(json));
    }

    private Stream<Arguments> parametersForUsersFromJsonWithParseExceptionTest() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("[{22}]")
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForUsersFromJsonWithClassCastExceptionTest")
    public void usersFromJsonWithClassCastExceptionTest(String json) {
//        assert
        Assertions.assertThrows(ClassCastException.class, () -> User.usersFromJson(json));
    }

    private Stream<Arguments> parametersForUsersFromJsonWithClassCastExceptionTest() {
        return Stream.of(
                Arguments.of("24"),
                Arguments.of("[22]")
        );
    }

    @Test
    public void usersFromJsonWithEmptyUserListTest() throws ParseException {
//        arrange
        String json = "[]";
//        act
        List<User> result = User.usersFromJson(json);
//        assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void usersFromJsonWithNullTest() {
//        arrange
        String json = null;
//        assert
        Assertions.assertThrows(NullPointerException.class, () -> User.usersFromJson(json));
    }

    @ParameterizedTest
    @MethodSource("parametersForJsonFromUsersListTest")
    public void jsonFromUsersListTest(List<User> userList, String expected) {
//        act
        String result = User.jsonFromUsersList(userList);
//        assert
        Assertions.assertEquals(result, expected);
    }

    private Stream<Arguments> parametersForJsonFromUsersListTest() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("Serg", "12345"));
        userList.add(new User("Petya", "12345"));
        userList.add(new User("DDD", "12345"));
        userList.add(new User("Vasya", "12345"));
        userList.add(new User("Ira", "12345"));
        String json = "[" +
                "{\"username\":\"Serg\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Petya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"DDD\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Vasya\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}," +
                "{\"username\":\"Ira\",\"password\":\"12345\",\"userOnline\":false,\"messages\":[]}" +
                "]";
        return Stream.of(
                Arguments.of(userList, json),
                Arguments.of(new ArrayList<User>(), "[]")
        );
    }

    @Test
    public void jsonFromUsersListWithNullTest() {
//        arrange
        List<User> userList = null;
        String expected = "null";
//        act
        String result = User.jsonFromUsersList(userList);
//        assert
        Assertions.assertEquals(result, expected);
    }

    @ParameterizedTest
    @MethodSource("parametersForIsUserOnlineWithSetUserStatusOnlineTest")
    public void isUserOnlineWithSetUserStatusOnlineTest(User user, boolean expected) {
//        act
        boolean result = user.isUserOnline();
//        assert
        Assertions.assertEquals(result, expected);
    }

    private Stream<Arguments> parametersForIsUserOnlineWithSetUserStatusOnlineTest() {
        User user1 = new User("Serg", "12345");
        user1.setUserStatusOnline(true);
        User user2 = new User("Serg", "12345");
        user2.setUserStatusOnline(true);
        user2.setUserStatusOnline(false);
        return Stream.of(
                Arguments.of(new User("Serg", "12345"), false),
                Arguments.of(user1, true),
                Arguments.of(user2, false)
        );
    }

    @Test
    public void getUsernameTest() {
//        arrange
        String expected = "Serg";
        User user = new User("Serg", "12345");
//        act
        String result = user.getUsername();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getPasswordTest() {
//        arrange
        String expected = "12345";
        User user = new User("Serg", "12345");
//        act
        String result = user.getPassword();
//        assert
        Assertions.assertEquals(result, expected);
    }

    @ParameterizedTest
    @MethodSource("parametersForMessageListIsEmptyWithNewMessageByJsonTest")
    public void messageListIsEmptyWithNewMessageByJsonTest(User user, boolean expected) {
//        act
        boolean result = user.messageListIsEmpty();
//        assert
        Assertions.assertEquals(result, expected);
    }

    private Stream<Arguments> parametersForMessageListIsEmptyWithNewMessageByJsonTest() throws ParseException {
        User user = new User("Serg", "12345");
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        user.newMessage(jsonMessage);
        return Stream.of(
                Arguments.of(new User("Serg", "12345"), true),
                Arguments.of(user, false)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForNextMessageTest")
    public void nextMessageTest(User user, IMessage expected) {
//        act
        IMessage result = user.nextMessage();
//        assert
        Assertions.assertEquals(result, expected);
    }

    private Stream<Arguments> parametersForNextMessageTest() throws ParseException {
        User user = new User("Serg", "12345");
        String jsonMessage = "{\"title\":\"title\",\"content\":\"content\",\"localDateTime\":\"localDateTime\"}";
        user.newMessage(jsonMessage);
        return Stream.of(
                Arguments.of(new User("Serg", "12345"), null),
                Arguments.of(user, ChatMessage.getMessageFromJson(jsonMessage))
        );
    }

    @Test
    public void newMessageWithIMessageArgumentTest() {
//        arrange
        User user = new User("Serg", "12345");
        IMessage messageMock = Mockito.mock(IMessage.class);
//        act
        user.newMessage(messageMock);
//        assert
        Mockito.verify(messageMock, Mockito.times(1)).getTitle();
        Mockito.verify(messageMock, Mockito.times(1)).getContent();
    }

    @Test
    public void testEqualsMethod() {
//        arrange
        User expected = new User("Serg", "12345");
//        act
        User result = new User("Serg", "12345");
//        assert
        Assertions.assertEquals(result.getUsername(), expected.getUsername());
        Assertions.assertEquals(result.getPassword(), expected.getPassword());
    }
}
