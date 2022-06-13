package repository;

import model.User;
import services.ILogger;
import services.ISettings;
import services.IStorage;
import services.IUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JsonFileStorage implements IStorage {

    private final Path path;

    private final ConcurrentMap<String, IUser> users;

    private final ILogger logger;

    public JsonFileStorage(ISettings settings, ILogger logger) {
        this.path = Path.of(settings.getPathStorageFile());
        this.users = new ConcurrentHashMap<>();
        this.fillUsers(this.getStorageFromFile(this.path));
        this.logger = logger;
    }

    @Override
    public Set<String> getUsernameSet() {
        return this.users.keySet();
    }

    @Override
    public IUser getUser(String username) {
        return this.users.get(username);
    }

    @Override
    public boolean newUser(String username, String password) {
        this.users.put(username, new User(username, password));
        return this.saveInFile();
    }

    @Override
    public void closeStorage() {
        this.saveInFile();
    }

    private List<IUser> getStorageFromFile(Path path) {
        String fileReader;
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return new ArrayList<>();
            } else if (!Files.isReadable(path) || (fileReader = Files.readString(path)).isEmpty()) {
                return new ArrayList<>();
            }
            return User.jsonToList(fileReader);
        } catch (IOException | org.json.simple.parser.ParseException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException();
        }
    }

    private void fillUsers(List<IUser> userList) {
        if (userList.isEmpty()) return;
        for (IUser user : userList) {
            this.users.put(user.getUsername(), user);
        }
    }

    private boolean saveInFile() {
        if (Files.exists(this.path) && Files.isWritable(this.path)) {
            try {
                Files.write(this.path, Collections.singleton(User.listToJson(new ArrayList<>(this.users.values()))));
                return true;
            } catch (IOException exception) {
                this.logger.log(exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
        return false;
    }
}
