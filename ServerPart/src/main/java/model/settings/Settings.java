package model.settings;

import com.google.gson.Gson;

public class Settings implements ISettings {
    private final int port;
    private final String pathLogFile;
    private final String pathUsersFile;
    private final String pathUsersMessages;

    public Settings(String json) throws NullPointerException{
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

    private ISettings getSettingsFromJson(String json) {
        var h = new Gson().fromJson(json, Settings.class);
        return new Gson().fromJson(json, Settings.class);
    }
}
