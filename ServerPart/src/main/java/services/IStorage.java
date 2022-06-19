package services;


import java.io.IOException;

public interface IStorage {

    void newUser(String username, String password);

    boolean isExist(String username);

    void setUserStatusOnline(String login, boolean status);

    boolean passwordIsValid(String login, String password);

    void newMessage(String login, IMessage message);

    boolean messageListIsEmpty(String login);

    IMessage nextMessage(String login);

    void close(ISettings settings) throws IOException;
}
