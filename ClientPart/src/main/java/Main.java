import model.NetServerResponseFactory;
import model.Client;
import model.Settings;
import org.json.simple.parser.ParseException;
import repository.NetServer;
import repository.ConsoleView;
import repository.FileLogger;
import repository.FileStorage;
import services.ILogger;
import services.IClient;
import services.ISettings;

import java.io.IOException;

public class Main {

    public final static String PATH_SETTINGS = "ClientPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        ISettings settings;
        try {
            settings = Settings.getSettingsFromJson(new FileStorage(PATH_SETTINGS).getSettingsJson());
            ILogger logger = new FileLogger(settings);
            IClient model = new Client(logger, new ConsoleView());
            new NetServer(logger, settings, model, new NetServerResponseFactory()).start();
        } catch (ParseException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
