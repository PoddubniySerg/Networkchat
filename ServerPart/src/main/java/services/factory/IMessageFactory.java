package services.factory;

import model.message.IMessage;

public interface IMessageFactory {

    IMessage newMessage(String title, String content);
}
