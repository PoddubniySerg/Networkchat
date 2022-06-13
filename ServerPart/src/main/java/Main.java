import model.ChatServer;
import repository.FileLogger;
import repository.FileSettings;
import repository.JsonFileStorage;
import services.ILogger;
import services.ISettings;
import services.IStorage;

import java.nio.file.Path;

public class Main {
    public final static String PATH_SETTINGS = "ServerPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        ISettings settings = new FileSettings(Path.of(PATH_SETTINGS));
        ILogger logger = new FileLogger(settings);
        IStorage storage = new JsonFileStorage(settings, logger);
        new ChatServer(settings, storage, logger).start();
    }
}