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

    public static List<Message> getMessagesFromJson(String jsonStr, ILogger logger) {
        List<Message> messageList = new ArrayList<>();
        if (jsonStr != null) {
            JSONArray jsonArray;
            try {
                jsonArray = (JSONArray) new JSONParser().parse(jsonStr);
                Gson gson = new GsonBuilder().create();
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    Message message = gson.fromJson(jsonObject.toJSONString(), Message.class);
                    messageList.add(message);
                }
            } catch (ParseException exception) {
                logger.log(exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
        return messageList;
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
}
