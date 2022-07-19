package services.factory;

import repository.storage.IStorage;
import services.handler.message.IMessageHendler;

public interface IMessageHandlerFactory {

    IMessageHendler newClientService(IStorage storage);
}
