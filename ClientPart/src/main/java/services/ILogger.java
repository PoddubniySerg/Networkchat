package services;


import java.io.IOException;

public interface ILogger {

    void log(IResponse response) throws IOException;

    void log(String message) throws IOException;
}
