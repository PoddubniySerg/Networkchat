package services.server;

import model.response.IResponse;

import java.io.IOException;

public interface IServer {

    boolean serverIsConnected();

    IResponse getResponse() throws IOException;

    void sendRequest(String content);

    void close() throws IOException;
}
