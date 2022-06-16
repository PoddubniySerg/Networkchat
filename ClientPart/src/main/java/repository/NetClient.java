package repository;

import services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class NetClient implements INetClient {
    private final ILogger logger;
    private final ISettings settings;
    private final IModel model;
    private final IMessageFactory messageFactory;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public NetClient(ILogger logger, ISettings settings, IModel model, IMessageFactory messageFactory) {
        this.logger = logger;
        this.settings = settings;
        this.model = model;
        this.messageFactory = messageFactory;
    }

    public void start() throws IOException {
        try (
                Socket socket = new Socket(this.settings.getHost(), this.settings.getPort());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.model.start(this);
        } catch (IOException exception) {
            this.logger.log(this.messageFactory.newMessage(exception.getMessage()));
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean sergverIsConnected() {
        return !this.socket.isClosed();
    }

    @Override
    public String readLine() throws IOException {
        return this.in.readLine();
    }

    @Override
    public void sendString(String string) {
        this.out.println(string);
    }
}
