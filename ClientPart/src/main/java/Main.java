import model.MessageFactory;
import model.Model;
import model.Settings;
import org.json.simple.parser.ParseException;
import repository.NetClient;
import repository.ConsoleView;
import repository.FileLogger;
import repository.FileStorage;
import services.ILogger;
import services.IModel;
import services.ISettings;

import java.io.IOException;

public class Main {

    public final static String PATH_SETTINGS = "ClientPart/src/main/resources/Settings.txt";

    public static void main(String[] args) {
        ISettings settings;
        try {
            settings = Settings.getSettingsFromJson(new FileStorage(PATH_SETTINGS).getSettingsJson());
            ILogger logger = new FileLogger(settings);
            IModel model = new Model(logger, new ConsoleView(), new MessageFactory());
            new NetClient(logger, settings, model, new MessageFactory()).start();
        } catch (ParseException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
