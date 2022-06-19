package model;

import services.IMessage;

import java.time.LocalDateTime;

public class ChatMessage implements IMessage {

    private final String title;

    private final String content;

    private final String localDateTime;

    public ChatMessage(String title, String content) {
        this.title = title;
        this.content = content;
        this.localDateTime = LocalDateTime.now().toString();
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
