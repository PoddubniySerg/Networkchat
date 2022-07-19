package repository.storage;

import model.message.ChatMessage;
import model.message.IMessage;
import model.settings.ISettings;
import model.User;
import org.json.simple.parser.ParseException;
import repository.IRepository;
import repository.logger.ILogger;
import services.constant.CommandsList;
import view.IAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UsersStorage implements IStorage {

    private final ConcurrentMap<String, User> users;
    private final ILogger logger;
    private final IRepository repository;

    public UsersStorage(
            ILogger logger,
            IRepository repository,
            ISettings settings,
            IAdmin admin
    ) {
        this.users = new ConcurrentHashMap<>();
        this.logger = logger;
        this.repository = repository;
        try {
            this.fillUsers(Objects.requireNonNull(User.usersFromJson(repository.getUsersJson(settings))));
        } catch (IOException | ParseException exception) {
            logger.log(exception.getMessage());
            admin.printMessage(exception.getMessage());
        }
    }

    private void fillUsers(List<User> userList) {
        if (userList.isEmpty()) return;
        for (User user : userList) {
            this.users.put(user.getUsername(), user);
        }
    }

    @Override
    public void newUser(String username, String password) {
        this.users.put(username, User.newUser(username, password));
    }

    @Override
    public boolean isExist(String username) {
        if (username == null) return false;
        return this.users.containsKey(username);
    }

    @Override
    public void setUserStatusOnline(String login, boolean status) {
        this.users.get(login).setUserStatusOnline(status);
    }

    @Override
    public void messageSyncr(String login, ISettings settings) throws IOException, ParseException {
        List<String> messages = this.repository.getUserMessages(login, settings);
        for (String message : messages) {
            this.users.get(login).newMessage(message);
        }
    }

    @Override
    public boolean passwordIsValid(String login, String password) {
        if (!this.isExist(login) || password == null) return false;
        return password.equals(this.users.get(login).getPassword());
    }

    @Override
    public void newMessage(String login, IMessage message, ISettings settings) throws IOException {
        if (message.getTitle().equals(CommandsList.EXIT.command())) {
            this.users.get(login).newMessage(message);
            return;
        }
        this.logger.log(message);
        IMessage forAuthorMessage = new ChatMessage("you", message.getContent());
        for (User user : this.users.values()) {
            if (user.isUserOnline()) {
                user.newMessage(!user.getUsername().equals(login) ? message : forAuthorMessage);
            } else {
                this.repository.saveUserMessage(
                        user.getUsername(),
                        !user.getUsername().equals(login) ? message.getJson() : forAuthorMessage.getJson(),
                        settings);
            }
        }
    }

    @Override
    public boolean messageListIsEmpty(String login) {
        return this.users.get(login).messageListIsEmpty();
    }

    @Override
    public IMessage nextMessage(String login, ISettings settings) {
        IMessage message = this.users.get(login).nextMessage();
        if (message != null) {
            try {
                this.repository.saveUserMessage(login, message.getJson(), settings);
            } catch (IOException exception) {
                this.logger.log(exception.getMessage());
            }
        }
        return message;
    }

    @Override
    public void close(ISettings settings) throws IOException {
        for (User user : this.users.values()) {
            if (user.isUserOnline()) {
                this.newMessage(user.getUsername(), new ChatMessage(user.getUsername(), "disabled by server"), settings);
                user.setUserStatusOnline(false);
            }
            IMessage message = user.nextMessage();
            while (message != null) {
                this.repository.saveUserMessage(user.getUsername(), message.getJson(), settings);
                message = user.nextMessage();
            }
        }
        repository.saveUsersJson(User.jsonFromUsersList(new ArrayList<>(this.users.values())), settings);
    }
}
