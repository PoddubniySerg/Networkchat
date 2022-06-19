package model;

import services.IMessage;
import services.IMessageFactory;

public class ChatMessageFactory implements IMessageFactory {
    @Override
    public IMessage newMessage(String title, String content) {
        return new ChatMessage(title, content);
    }
}
