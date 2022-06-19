package services;

public interface IMessageHandlerFactory {

    IMessageHendler newClientService(IStorage storage);
}
