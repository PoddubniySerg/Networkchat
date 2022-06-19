package services;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface IMessageHendler {

    void start(IClientService clientService, ISettings settings) throws IOException, ParseException;
}
