package model;

import services.ILogger;
import services.IMessage;
import services.IStorage;

public class MessageHandler {

    private final ILogger logger;

    private final IStorage storage;

    public MessageHandler(ILogger logger, IStorage storage) {
        this.logger = logger;
        this.storage = storage;
    }

    public void inputMessage(String textContent, String username, String dateTime, boolean messageForAuthor) {
        IMessage newMessage = null;
        if (messageForAuthor) newMessage = this.storage.getUser(username).newMessage(username, textContent, dateTime);
        for (String name : this.storage.getUsernameSet()) {
            if (!name.equals(username)) {
                newMessage = this.storage.getUser(name).newMessage(username, textContent, dateTime);
            }
        }
        this.logger.log(newMessage);
    }

    public String outputMessage(String username) {
        while (true) {
            if (!this.storage.getUser(username).messageListIsEmpty())
                return this.storage.getUser(username).getJsonMessages();
        }
    }
}
