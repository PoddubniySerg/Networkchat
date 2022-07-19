import services.factory.ChatResponseFactory;
import services.client.Client;
import model.settings.Settings;
import services.server.NetServer;
import view.ConsoleView;
import repository.logger.FileLogger;
import repository.storage.FileStorage;
import repository.logger.ILogger;
import services.client.IClient;
import model.settings.ISettings;

import java.io.IOException;

public class Main {

    public final static String PATH_SETTINGS = "ClientPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        ISettings settings;
        try {
            settings = Settings.getSettingsFromJson(new FileStorage(PATH_SETTINGS).getSettingsJson());
            ILogger logger = new FileLogger(settings);
            IClient model = new Client(logger, new ConsoleView());
            new NetServer(logger, settings, model, new ChatResponseFactory()).start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
