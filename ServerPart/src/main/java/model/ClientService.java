package model;

import services.ILogger;
import services.IStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ClientService {
    private static final String EXIT_COMMAND = "/exit";

    private final Socket socket;
    private final ILogger logger;
    private final MessageHandler messageHandler;
    private final IStorage storage;

    public ClientService(Socket socket, ILogger logger, IStorage storage) {
        this.socket = socket;
        this.logger = logger;
        this.messageHandler = new MessageHandler(logger, storage);
        this.storage = storage;
    }

    public void start() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            String username = this.authorization(in, out);
            if (username != null && !username.equals(EXIT_COMMAND)) {
                Thread outThread = new Thread(() -> this.sendMessage(out, this.messageHandler, username));
                outThread.start();
                this.inputMessage(in, username);
                outThread.interrupt();
            }
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
        try {
            this.socket.close();
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private String authorization(BufferedReader in, PrintWriter out) {
        try {
            while (true) {
                out.println("Write login to enter chat, or '" +
                        EXIT_COMMAND +
                        "' for exit, or '/registration' for attached a new user");
                String input = in.readLine();
                if (input == null) {
                    out.println("Unknown user or command");
                } else {
                    String login;
                    String password;
                    switch (input) {
                        case EXIT_COMMAND:
                            return EXIT_COMMAND;
                        case "/registration":
                            out.println("Command OK");
                            out.println("Enter login");
                            login = in.readLine();
                            if (this.storage.getUsernameSet().contains(login)) {
                                out.println("The login already exists, choose another one");
                                break;
                            }
                            out.println("Login OK");
                            out.println("Enter password");
                            password = in.readLine();
                            if (login != null && password != null && this.storage.newUser(login, password)) {
                                out.println("OK!");
                                this.messageHandler.inputMessage(
                                        "New user in chat. Welcome!"
                                        , login
                                        , LocalDateTime.now().toString()
                                        , true
                                );
                                return login;
                            } else {
                                out.println("Incorrect input");
                            }
                            break;
                        default:
                            if (
                                    this.storage.getUsernameSet() != null
                                            && !this.storage.getUsernameSet().isEmpty()
                                            && this.storage.getUsernameSet().contains(input)) {
                                out.println();
                                out.println("Enter password");
                                password = in.readLine();
                                if (
                                        password != null
                                                && this.storage.getUser(input).getPassword().equals(password)
                                ) {
                                    out.println("OK!");
                                    return input;
                                } else {
                                    out.println("Unknown login or password");
                                }
                                break;
                            }
                            out.println("Unknown user or command");
                            break;
                    }
                }
            }
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private void inputMessage(BufferedReader in, String username) {
        try {
            this.storage.getUser(username).setUserStatusOnline(true);
            this.messageHandler.inputMessage(
                    "is attached to chat"
                    , username
                    , LocalDateTime.now().toString()
                    , true
            );
            String inputStr;
            while (!this.socket.isClosed()) {
                inputStr = in.readLine();
                if (inputStr == null || inputStr.equals(EXIT_COMMAND)) {
                    this.storage.getUser(username).setUserStatusOnline(false);
                    this.messageHandler.inputMessage(
                            "leave chat"
                            , username
                            , LocalDateTime.now().toString()
                            , true
                    );
                    return;
                }
                if (!inputStr.isEmpty()) this.messageHandler.inputMessage(inputStr, username, in.readLine(), false);
            }
        } catch (IOException exception) {
            this.logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private void sendMessage(PrintWriter out, MessageHandler messageHandler, String username) {
        while (!Thread.currentThread().isInterrupted()) {
            String outputMessage = messageHandler.outputMessage(username);
            if (outputMessage != null && !outputMessage.isEmpty()) {
                out.println(outputMessage);
            }
        }
    }
}
