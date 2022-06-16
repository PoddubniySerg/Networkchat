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
    private final String host;

    public Settings(int port, String pathLogFile, String host) {
        this.port = port;
        this.pathLogFile = pathLogFile;
        this.host = host;
    }

    public static Settings getSettingsFromJson(String jsonSettings) throws ParseException {
        if (jsonSettings == null) jsonSettings = "";
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonSettings);
        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        return gson.fromJson(jsonObject.toJSONString(), Settings.class);
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
    public String getHost() {
        return this.host;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Settings)) return false;
        Settings settings = (Settings) obj;
        return this.port == settings.getPort()
                && this.host.equals(settings.getHost())
                && this.pathLogFile.equals(settings.getPathLogFile());
    }
}
