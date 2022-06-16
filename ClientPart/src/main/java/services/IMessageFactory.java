package services;

import org.json.simple.parser.ParseException;

import java.util.List;

public interface IMessageFactory {

    IMessage newMessage(String content);

    List<IMessage> getMessagesFromJson(String jsonStr) throws ParseException;
}
