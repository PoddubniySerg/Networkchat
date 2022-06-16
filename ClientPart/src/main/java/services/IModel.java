package services;

import java.io.IOException;

public interface IModel {

    void start(INetClient netClient) throws IOException;
}
