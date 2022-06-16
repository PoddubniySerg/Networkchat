package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.ILogger;
import services.IMessage;

import java.util.ArrayList;
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
    public String toString() {
        return this.title + "\n" + this.content + "\n" + this.localDateTime + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message)) return false;
        Message message = (Message) obj;
        return this.title.equals(message.getTitle())
                && this.content.equals(message.getContent())
                && this.localDateTime.equals(message.getDateTime());
    }
}
