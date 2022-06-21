package model;

import org.json.simple.parser.ParseException;
import services.*;

import java.io.IOException;

public class MessageHandler implements IMessageHendler {
    private final IStorage storage;
    private final IMessage startMessage;

    public MessageHandler(IStorage storage) {
        this.storage = storage;
        this.startMessage = new ChatMessage(
                CommandsList.AUTHORIZATION.command(),
                "Write login to enter chat, or '" +
                        CommandsList.EXIT.command() +
                        "' for exit, or '/registration' for attached a new user"
        );
    }

    @Override
    public void start(IClientService clientService, ISettings settings) throws IOException, ParseException {
        String username = this.authorization(clientService, settings);
        if (username != null && !username.equals(CommandsList.EXIT.command())) {
            Thread outThread = new Thread(() -> this.sendMessage(clientService, username, settings));
            outThread.start();
            this.inputMessage(clientService, username, settings);
            outThread.interrupt();
        }
    }

    private String authorization(IClientService clientService, ISettings settings) throws IOException, ParseException {
        while (!clientService.serverIsClosed()) {
            IMessage request = clientService.getClientRequest(new ChatMessageFactory());
            if (request.getTitle().equals(CommandsList.AUTHORIZATION.command())) {
                clientService.sendResponse(this.startMessage);
            } else if (request.getTitle().equals(CommandsList.EXIT.command())) {
                return CommandsList.EXIT.command();
            } else if (request.getTitle().equals(CommandsList.REGISTRATION.command())) {
                clientService.sendResponse(
                        new ChatMessage(CommandsList.REGISTRATION.command(), "Enter login")
                );
                IMessage login = clientService.getClientRequest(new ChatMessageFactory());
//If the username exists, registration is not possible
                if (this.storage.isExist(login.getContent())) {
                    clientService.sendResponse(
                            new ChatMessage(
                                    CommandsList.INFO_CONTENT.command(), "The login already exists, choose another one")
                    );
                    clientService.sendResponse(this.startMessage);
                } else {
                    this.registrationNewUser(clientService, login.getContent(), settings);
                    this.successfulAuthorization(clientService, login.getContent(), settings);
                    return login.getContent();
                }
            } else {
//If content is not command and this is not a login then print info
                if (!this.storage.isExist(request.getContent())) {
                    clientService.sendResponse(
                            new ChatMessage(CommandsList.INFO_CONTENT.command(), "Unknown user or command")
                    );
                    clientService.sendResponse(this.startMessage);
                } else {
                    if (this.isAttachedToChat(clientService, request.getContent(), settings)) {
                        return request.getContent();
                    } else {
                        clientService.sendResponse(
                                new ChatMessage(CommandsList.INFO_CONTENT.command(), "Unknown login or password")
                        );
                        clientService.sendResponse(this.startMessage);
                    }
                }
            }
        }
        return CommandsList.EXIT.command();
    }

    private void registrationNewUser(IClientService clientService, String login, ISettings settings) throws IOException {
        clientService.sendResponse(
                new ChatMessage(
                        CommandsList.REGISTRATION.command(),
                        "Enter password"
                )
        );
        IMessage password = clientService.getClientRequest(new ChatMessageFactory());
        this.storage.newUser(login, password.getContent());
        this.storage.newMessage(
                login,
                new ChatMessage(login, "New user in chat. Welcome!"),
                settings
        );
    }

    private boolean isAttachedToChat(IClientService clientService, String login, ISettings settings) throws IOException, ParseException {
        clientService.sendResponse(
                new ChatMessage(CommandsList.AUTHORIZATION.command(), "Enter password")
        );

        if (this.storage.passwordIsValid(login, clientService.getClientRequest(login, new ChatMessageFactory()).getContent())) {
            this.successfulAuthorization(clientService, login, settings);
            return true;
        }
        return false;
    }

    private void successfulAuthorization(IClientService clientService, String login, ISettings settings) throws IOException, ParseException {
        clientService.sendResponse(
                new ChatMessage(
                        CommandsList.VALID_COMMAND.command(),
                        CommandsList.VALID_COMMAND.command()));
        this.storage.setUserStatusOnline(login, true);
        this.storage.messageSyncr(login, settings);
        this.storage.newMessage(login, new ChatMessage(login, "attached to chat"), settings);
    }

    private void inputMessage(IClientService clientService, String login, ISettings settings) throws IOException {
        while (!clientService.serverIsClosed()) {
            IMessage message = clientService.getClientRequest(login, new ChatMessageFactory());
            if (message != null && !message.getTitle().equals(CommandsList.EXIT.command())) {
                if (!message.getContent().isEmpty()) this.storage.newMessage(login, message, settings);
            } else {
                this.storage.newMessage(login, new ChatMessage(login, "left chat"), settings);
                this.storage.setUserStatusOnline(login, false);
                this.storage.newMessage(login, message, settings);
                break;
            }
        }

    }

    private void sendMessage(IClientService clientService, String login, ISettings settings) {
        while (!Thread.currentThread().isInterrupted()) {
            while (!this.storage.messageListIsEmpty(login)) {
                clientService.sendResponse(this.storage.nextMessage(login, settings));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MessageHandler)) return false;
        MessageHandler messageHandler = (MessageHandler) obj;
        return this.storage.equals(messageHandler.storage)
                && this.startMessage.getTitle().equals(messageHandler.startMessage.getTitle())
                && this.startMessage.getContent().equals(messageHandler.startMessage.getContent());
    }
}
