package services;


import java.io.IOException;

public interface ILogger {

    void log(IMessage message) throws IOException;
}
