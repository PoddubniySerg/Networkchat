package services;

public interface IMessageFactory {

    IMessage newMessage(String title, String content);
}
