package services;

public interface ISettings {

    int getPort();

    String getPathLogFile();

    String getPathUsersFile();

    ISettings getSettingsFromJson(String json);
}
