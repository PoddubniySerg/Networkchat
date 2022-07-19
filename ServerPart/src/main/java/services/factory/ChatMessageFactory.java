package services.factory;

import model.message.ChatMessage;
import model.message.IMessage;

public class ChatMessageFactory implements IMessageFactory {
    @Override
    public IMessage newMessage(String title, String content) {
        return new ChatMessage(title, content);
    }
}
