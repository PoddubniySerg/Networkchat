package repository;

import services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class NetServer implements IServer {
    private final ILogger logger;
    private final ISettings settings;
    private final IClient model;
    private final IResponseFactory responseFactory;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public NetServer(ILogger logger, ISettings settings, IClient model, IResponseFactory responseFactory) {
        this.logger = logger;
        this.settings = settings;
        this.model = model;
        this.responseFactory = responseFactory;
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
            this.logger.log(exception.getMessage());
        }
    }

    @Override
    public boolean serverIsConnected() {
        return !this.socket.isClosed();
    }

    @Override
    public IResponse getResponse() throws IOException {
        String title = this.in.readLine();
        String content = this.in.readLine();
        String dateTime = this.in.readLine();
        return responseFactory.newResponse(title, content, dateTime);
    }

    @Override
    public void sendRequest(String string) {
        this.out.println(string);
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.in.close();
        this.out.close();
    }
}
