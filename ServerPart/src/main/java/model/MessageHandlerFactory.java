package model;

import services.*;

public class MessageHandlerFactory implements IMessageHandlerFactory {
    @Override
    public IMessageHendler newClientService(IStorage storage) {
        return new MessageHandler(storage);
    }
}
