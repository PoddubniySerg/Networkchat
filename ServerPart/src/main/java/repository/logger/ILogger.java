package repository.logger;


import model.message.IMessage;

public interface ILogger {

    void log(IMessage message);

    void log(String message);
}
