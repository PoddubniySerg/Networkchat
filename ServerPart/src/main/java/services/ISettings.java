package services;

public interface ISettings {

    int getPort();

    String getPathLogFile();

    String getPathUsersFile();

    String getPathUsersMessages();

    ISettings getSettingsFromJson(String json);
}
