package services.client;

import services.server.IServer;

import java.io.IOException;

public interface IClient {

    void start(IServer netClient) throws IOException;
}
