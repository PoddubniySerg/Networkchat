package services;

import java.io.IOException;

public interface IClient {

    void start(IServer netClient) throws IOException;
}
