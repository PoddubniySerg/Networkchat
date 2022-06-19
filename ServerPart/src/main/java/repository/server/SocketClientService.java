package repository.server;

import model.CommandsList;
import org.json.simple.parser.ParseException;
import services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClientService implements IClientService {

    private final ILogger logger;
    private final IAdmin admin;
    private final IMessageHendler messageHendler;
    private final Socket socket;

    private BufferedReader in;

    private PrintWriter out;

    public SocketClientService(Socket socket, ILogger logger, IAdmin admin, IMessageHendler messageHendler) {
        this.logger = logger;
        this.admin = admin;
        this.messageHendler = messageHendler;
        this.socket = socket;
    }

    public void start(ISettings settings) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            this.in = in;
            this.out = out;
            messageHendler.start(this, settings);
        } catch (IOException | ParseException exception) {
            this.logger.log(exception.getMessage());
            this.admin.printMessage(exception.getMessage());
        }
    }

    @Override
    public IMessage getClientRequest(IMessageFactory messageFactory) {
        try {
            String input = in.readLine();
            if (input.equals(CommandsList.EXIT.command()))
                return messageFactory.newMessage(CommandsList.EXIT.command(), "");
            if (input.equals(CommandsList.REGISTRATION.command()))
                return messageFactory.newMessage(CommandsList.REGISTRATION.command(), input);
            if (input.equals(CommandsList.AUTHORIZATION.command()))
                return messageFactory.newMessage(CommandsList.AUTHORIZATION.command(), input);
            return messageFactory.newMessage(CommandsList.MESSAGE.command(), input);
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            this.admin.printMessage(exception.getMessage());
        }
        return null;
    }

    @Override
    public IMessage getClientRequest(String username, IMessageFactory messageFactory) throws IOException {
        String input = in.readLine();
        return messageFactory
                .newMessage(input.equals(CommandsList.EXIT.command()) ? CommandsList.EXIT.command() : username, input);

    }

    @Override
    public void sendResponse(IMessage message) {
        if (
                message != null
                        && message.getTitle() != null
                        && !message.getTitle().isEmpty()
                        && message.getContent() != null
                        && !message.getContent().isEmpty()
                        && message.getDateTime() != null
                        && !message.getDateTime().isEmpty()
        ) {
            this.out.println(message.getTitle());
            this.out.println(message.getContent());
            this.out.println(message.getDateTime());
        }
    }

    @Override
    public boolean serverIsClosed() {
        return socket.isClosed();
    }
}
