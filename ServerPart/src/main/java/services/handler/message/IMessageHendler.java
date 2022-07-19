package services.handler.message;

import model.settings.ISettings;
import org.json.simple.parser.ParseException;
import services.server.IClientService;

import java.io.IOException;

public interface IMessageHendler {

    void start(IClientService clientService, ISettings settings) throws IOException, ParseException;
}
