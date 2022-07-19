package model.settings;

import com.google.gson.Gson;

public class Settings implements ISettings {
    private final int port;
    private final String pathLogFile;
    private final String host;

    public Settings(int port, String pathLogFile, String host) {
        this.port = port;
        this.pathLogFile = pathLogFile;
        this.host = host;
    }

    public static Settings getSettingsFromJson(String jsonSettings) {
        return new Gson().fromJson(jsonSettings, Settings.class);
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

    @Override
    public int hashCode() {
        return port + pathLogFile.hashCode() + host.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
