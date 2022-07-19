package services.server;

import model.message.IMessage;
import services.factory.IMessageFactory;

import java.io.IOException;

public interface IClientService {

    IMessage getClientRequest(String username, IMessageFactory messageFactory) throws IOException;

    void sendResponse(IMessage message);

    boolean serverIsClosed();

    IMessage getClientRequest(IMessageFactory messageFactory);
}
