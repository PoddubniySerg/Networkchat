package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import services.IMessage;

import java.lang.reflect.Type;
import java.util.List;

public class Message implements IMessage {

    private final String title;

    private final String content;

    private final String localDateTime;

    public Message(String title, String content, String localDateTime) {
        this.title = title;
        this.content = content;
        this.localDateTime = localDateTime;
    }

    public static String listToJson(List<Message> messageList) {
        Type listType = new TypeToken<List<Message>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(messageList, listType);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getDateTime() {
        return this.localDateTime;
    }
}
