package services;


public interface ILogger {

    void log(IMessage message);

    void log(String message);
}
