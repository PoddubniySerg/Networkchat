import model.ChatClient;
import model.ConsoleView;
import repository.FileLogger;
import repository.FileSettings;
import services.ISettings;

import java.nio.file.Path;

public class Main {

    public final static String PATH_SETTINGS = "ClientPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        ISettings settings = new FileSettings(Path.of(PATH_SETTINGS));
        new ChatClient(settings, new FileLogger(settings), new ConsoleView()).start();
    }
}
