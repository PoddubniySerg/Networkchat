package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.IMessage;
import services.IMessageFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageFactory implements IMessageFactory {
    @Override
    public IMessage newMessage(String content) {
        return new Message("you", content, LocalDateTime.now().toString());
    }

    @Override
    public List<IMessage> getMessagesFromJson(String jsonStr) throws ParseException {
        List<IMessage> messageList = new ArrayList<>();
        if (jsonStr != null) {
            JSONArray jsonArray;
            jsonArray = (JSONArray) new JSONParser().parse(jsonStr);
            Gson gson = new GsonBuilder().create();
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                IMessage message = gson.fromJson(jsonObject.toJSONString(), Message.class);
                messageList.add(message);
            }
        }
        return messageList;
    }
}
