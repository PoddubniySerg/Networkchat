package services;

import java.io.IOException;

public interface IClientService {

    IMessage getClientRequest(String username, IMessageFactory messageFactory) throws IOException;

    void sendResponse(IMessage message);

    boolean serverIsClosed();

    IMessage getClientRequest(IMessageFactory messageFactory);
}
