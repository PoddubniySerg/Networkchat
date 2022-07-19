import model.settings.ISettings;
import model.settings.Settings;
import repository.*;
import repository.logger.FileLogger;
import repository.logger.ILogger;
import repository.storage.IStorage;
import repository.storage.UsersStorage;
import services.factory.MessageHandlerFactory;
import services.server.ChatServer;
import view.ConsoleAdmin;
import view.IAdmin;

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