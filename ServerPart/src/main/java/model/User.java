package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.IMessage;
import services.IUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User implements IUser {

    private final String username;
    private final String password;
    private boolean userOnline;

    private final ConcurrentLinkedQueue<Message> messages;

    public User(String username, String password) {
        this.messages = new ConcurrentLinkedQueue<>();
        this.username = username;
        this.password = password;
    }

    public static List<IUser> jsonToList(String json) throws ParseException {
        List<IUser> userList = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(json);
        Gson gson = new GsonBuilder().create();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            User user = gson.fromJson(jsonObject.toJSONString(), User.class);
            userList.add(user);
        }
        return userList;
    }

    public static String listToJson(List<IUser> userList) {
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(userList, listType);
    }

    @Override
    public boolean isUserOnline() {
        return this.userOnline;
    }

    @Override
    public void setUserStatusOnline(boolean isOnline) {
        this.userOnline = isOnline;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean messageListIsEmpty() {
        return this.messages.isEmpty();
    }

    @Override
    public String getJsonMessages() {
        List<Message> messageList = new ArrayList<>();
        while (!this.messages.isEmpty()) {
            messageList.add(this.messages.poll());
        }
        return Message.listToJson(messageList);
    }

    @Override
    public IMessage newMessage(String title, String content, String localDateTime) {
        Message message = new Message(title, content, localDateTime);
        this.messages.add(message);
        return message;
    }
}
