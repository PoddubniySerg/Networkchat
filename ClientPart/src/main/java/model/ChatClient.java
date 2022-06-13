package model;

import services.ILogger;
import services.IMessage;
import services.ISettings;
import services.IView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class ChatClient {

    private static final String EXIT_COMMAND = "/exit";

    private final ISettings settings;
    private final ILogger logger;

    private final IView view;

    public ChatClient(ISettings settings, ILogger logger, IView view) {
        this.settings = settings;
        this.logger = logger;
        this.view = view;
    }

    public void start() {
        try (
                Socket socket = new Socket(this.settings.getHost(), this.settings.getPort());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            this.logger.log("Start the client");
            if (this.isLogged(socket, in, out)) {
                Thread inputThread = new Thread(() -> this.inputFromServer(in, this.logger, this.view));
                inputThread.setDaemon(true);
                inputThread.start();
                this.sendMessageToServer(out, inputThread);
                inputThread.interrupt();
                this.logger.log("The program is finished");
            } else {
                this.logger.log("The program is closed without authorization");
            }
            this.view.printMessage("The program is finished");
            this.view.closeView();
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private void sendMessageToServer(PrintWriter out, Thread inputThread) {
        String userInput = this.view.getString();
        while (!userInput.equals(EXIT_COMMAND) && inputThread.isAlive()) {
            if (!userInput.isEmpty()) {
                IMessage message = new Message("you", userInput, LocalDateTime.now().toString());
                out.println(message.getContent());
                out.println(message.getDateTime());
                this.logger.log(message);
            }
            userInput = this.view.getString();
        }
        out.println(userInput);
    }

    private void inputFromServer(BufferedReader in, ILogger logger, IView view) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String inputStr = in.readLine();
                List<Message> messages = Message.getMessagesFromJson(inputStr, logger);
                if (!messages.isEmpty()) {
                    for (IMessage message : messages) {
                        view.printMessage(message.toString());
                        logger.log(message);
                    }
                }
            }
        } catch (IOException exception) {
            logger.log(exception.getMessage());
            view.printMessage(exception.getMessage());
        }
    }

    private boolean isLogged(Socket socket, BufferedReader in, PrintWriter out) {
        try {
            while (!socket.isClosed()) {
                String serverRequest = in.readLine();
                this.view.printMessage(serverRequest);
                String userResponse = this.view.getString();
                out.println(userResponse);
                if (userResponse.equals(EXIT_COMMAND)) return false;
                serverRequest = in.readLine();
                if (serverRequest.equals("OK!")) return true;
                this.view.printMessage(serverRequest);
            }
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            return false;
        }
        this.logger.log("Disconnected by server");
        return false;
    }
}
