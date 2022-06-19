package services;

import java.io.IOException;

public interface IRepository {

    String getSettingsJson(String pathSettingsFile) throws IOException;

    String getUsersJson(ISettings settings) throws IOException;

    void saveUsersJson(String json, ISettings settings) throws IOException;
}
