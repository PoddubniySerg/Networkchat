package services;

import java.io.IOException;
import java.util.List;

public interface IRepository {

    String getSettingsJson(String pathSettingsFile) throws IOException;

    String getUsersJson(ISettings settings) throws IOException;

    void saveUsersJson(String json, ISettings settings) throws IOException;

    void saveUserMessage(String username, String jsonMessage, ISettings settings) throws IOException;

    List<String> getUserMessages(String username, ISettings settings) throws IOException;
}
