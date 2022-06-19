package services;

import java.io.IOException;

public interface IMessageHendler {

    void start(IClientService clientService) throws IOException;
}
