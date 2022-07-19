package model.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class ChatMessage implements IMessage {

    private final String title;

    private final String content;

    private final String localDateTime;

    private ChatMessage(String title, String content, String localDateTime) {
        this.title = title;
        this.content = content;
        this.localDateTime = localDateTime;
    }

    public ChatMessage(String title, String content) {
        this.title = title;
        this.content = content;
        this.localDateTime = LocalDateTime.now().toString();
    }

    public static ChatMessage getMessageFromJson(String json) {
        return new Gson().fromJson(json, ChatMessage.class);
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

    @Override
    public String getJson() {
        Type type = new TypeToken<ChatMessage>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatMessage)) return false;
        ChatMessage message = (ChatMessage) obj;
        return this.title.equals(message.getTitle())
                && this.content.equals(message.getContent())
                && this.localDateTime.equals(message.getDateTime());
    }
}
