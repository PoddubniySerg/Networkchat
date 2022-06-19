package model;

import services.*;

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
        } catch (IOException exception) {
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
        return this.users.containsKey(username);
    }

    @Override
    public void setUserStatusOnline(String login, boolean status) {
        this.users.get(login).setUserStatusOnline(status);
    }

    @Override
    public boolean passwordIsValid(String login, String password) {
        return password.equals(this.users.get(login).getPassword());
    }

    @Override
    public void newMessage(String login, IMessage message) {
        if (message.getTitle().equals(CommandsList.EXIT.command())) {
            this.users.get(login).newMessage(message);
            return;
        }
        this.logger.log(message);
        for (User user : this.users.values()) {
            if (user.getUsername().equals(login)) {
                user.newMessage(new ChatMessage("you", message.getContent()));
            } else user.newMessage(message);
        }
    }

    @Override
    public boolean messageListIsEmpty(String login) {
        return this.users.get(login).messageListIsEmpty();
    }

    @Override
    public IMessage nextMessage(String login) {
        return this.users.get(login).nextMessage();
    }

    @Override
    public void close(ISettings settings) throws IOException {
        for (User user : users.values()) {
            if (user.isUserOnline()) {
                this.newMessage(user.getUsername(), new ChatMessage(user.getUsername(), "disabled by server"));
                user.setUserStatusOnline(false);
            }
        }
        repository.saveUsersJson(User.jsonFromUsersList(new ArrayList<>(this.users.values())), settings);
    }
}
