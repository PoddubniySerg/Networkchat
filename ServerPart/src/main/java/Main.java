import model.*;
import repository.server.ChatServer;
import repository.ConsoleAdmin;
import repository.FileLogger;
import repository.FileRepository;
import services.*;

import java.io.IOException;

public class Main {
    public final static String PATH_SETTINGS = "ServerPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        try {
            IAdmin admin = new ConsoleAdmin();
            IRepository repository = new FileRepository();
            ISettings settings = new Settings(repository.getSettingsJson(PATH_SETTINGS));
            ILogger logger = new FileLogger(settings);
            IStorage storage = new UsersStorage(logger, repository, settings, admin);
            new ChatServer(new MessageHandlerFactory(), settings, logger, storage, admin).start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}