package repository.storage;

import org.json.simple.parser.ParseException;
import model.message.IMessage;
import model.settings.ISettings;

import java.io.IOException;

public interface IStorage {

    void newUser(String username, String password);

    boolean isExist(String username);

    void setUserStatusOnline(String login, boolean status);

    void messageSyncr(String login, ISettings settings) throws IOException, ParseException;

    boolean passwordIsValid(String login, String password);

    void newMessage(String login, IMessage message, ISettings settings) throws IOException;

    boolean messageListIsEmpty(String login);

    IMessage nextMessage(String login, ISettings settings);

    void close(ISettings settings) throws IOException;
}
