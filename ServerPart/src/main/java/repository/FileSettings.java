package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import services.ISettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSettings implements ISettings {
    private final int port;
    private final String pathLogFile;
    private final String pathStorageFile;

    public FileSettings(Path path) {
        FileSettings settings = this.getSettingsFromFile(path);
        this.port = settings.getPort();
        this.pathLogFile = settings.getPathLogFile();
        this.pathStorageFile = settings.pathStorageFile;
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
    public String getPathStorageFile() {
        return this.pathStorageFile;
    }

    private FileSettings getSettingsFromFile(Path path) {
        List<FileSettings> settingsList = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(Files.readString(path));
            Gson gson = new GsonBuilder().create();
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                FileSettings settings = gson.fromJson(jsonObject.toJSONString(), FileSettings.class);
                settingsList.add(settings);
            }
        } catch (IOException | org.json.simple.parser.ParseException exception) {
            throw new RuntimeException(exception);
        }
        return settingsList.get(0);
    }
}
