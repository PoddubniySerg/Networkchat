package services;

import java.io.IOException;

public interface INetClient {

    boolean sergverIsConnected();

    String readLine() throws IOException;

    void sendString(String string);
}
