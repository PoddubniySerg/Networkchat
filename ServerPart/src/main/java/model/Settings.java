package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.ISettings;

public class Settings implements ISettings {
    private final int port;
    private final String pathLogFile;
    private final String pathUsersFile;
    private final String pathUsersMessages;

    public Settings(String json) throws ParseException {
        ISettings settings = this.getSettingsFromJson(json);
        this.port = settings.getPort();
        this.pathLogFile = settings.getPathLogFile();
        this.pathUsersFile = settings.getPathUsersFile();
        this.pathUsersMessages = settings.getPathUsersMessages();
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getPathLogFile() {
        return this.pathLogFile;
    }

    @Override
    public String getPathUsersFile() {
        return this.pathUsersFile;
    }

    @Override
    public String getPathUsersMessages() {
        return this.pathUsersMessages;
    }

    private ISettings getSettingsFromJson(String json) throws ParseException {
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(json);
        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        return gson.fromJson(jsonObject.toJSONString(), Settings.class);
    }
}
