package services.factory;

import repository.storage.IStorage;
import services.handler.message.IMessageHendler;
import services.handler.message.MessageHandler;

public class MessageHandlerFactory implements IMessageHandlerFactory {
    @Override
    public IMessageHendler newClientService(IStorage storage) {
        return new MessageHandler(storage);
    }
}
