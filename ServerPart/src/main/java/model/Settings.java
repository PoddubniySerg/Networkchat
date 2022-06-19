package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import services.ISettings;

public class Settings implements ISettings {
    private final int port;
    private final String pathLogFile;
    private final String pathUsersFile;
    private final String pathUsersMessages;

    public Settings(String json) {
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

    @Override
    public ISettings getSettingsFromJson(String json) {
        ISettings settings;
        try {
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(json);
            Gson gson = new GsonBuilder().create();
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            settings = gson.fromJson(jsonObject.toJSONString(), Settings.class);
        } catch (org.json.simple.parser.ParseException exception) {
            throw new RuntimeException(exception);
        }
        return settings;
    }
}
