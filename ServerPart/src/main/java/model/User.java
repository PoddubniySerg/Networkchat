package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.IMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private final String username;
    private final String password;

    private boolean userOnline;

    private final ConcurrentLinkedQueue<ChatMessage> messages;

    public User(String username, String password) {
        this.messages = new ConcurrentLinkedQueue<>();
        this.username = username;
        this.password = password;
    }

    public static User newUser(String username, String password) {
        return new User(username, password);
    }

    public static List<User> usersFromJson(String json) throws ParseException {
        List<User> userList = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(json);
            Gson gson = new GsonBuilder().create();
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                User user = gson.fromJson(jsonObject.toJSONString(), User.class);
                userList.add(user);
            }
        return userList;
    }

    public static String jsonFromUsersList(List<User> users) {
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(users, listType);
    }

    public boolean isUserOnline() {
        return this.userOnline;
    }

    public void setUserStatusOnline(boolean isOnline) {
        this.userOnline = isOnline;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean messageListIsEmpty() {
        return this.messages.isEmpty();
    }

    public IMessage nextMessage() {
        return this.messages.poll();
    }

    public void newMessage(IMessage message) {
        this.messages.add(new ChatMessage(message.getTitle(), message.getContent()));
    }

    public void newMessage(String jsonMessage) throws ParseException {
        if (jsonMessage != null && !jsonMessage.isEmpty()) {
            ChatMessage message = ChatMessage.getMessageFromJson(jsonMessage);
            this.messages.add(message);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return this.username.equals(user.getUsername())
                && this.password.equals(user.getPassword());
    }
}
