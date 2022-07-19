package repository;

import services.constant.CommandsList;
import model.settings.ISettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileRepository implements IRepository {

    private String getFileContent(String pathString) throws IOException {
        String stringFromFile;
        String stringWithEmptyJson = "[]";
        Path path = Path.of(pathString);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        if (!Files.isReadable(path) || (stringFromFile = Files.readString(path)).isEmpty()) {
            return stringWithEmptyJson;
        }
        return stringFromFile;
    }

    @Override
    public String getSettingsJson(String pathSettingsFile) throws IOException {
        return this.getFileContent(pathSettingsFile);
    }

    @Override
    public String getUsersJson(ISettings settings) throws IOException {
        return this.getFileContent(settings.getPathUsersFile());
    }

    @Override
    public void saveUsersJson(String json, ISettings settings) throws IOException {
        Path pathFile = Path.of(settings.getPathUsersFile());
        if (!Files.exists(pathFile)) {
            Files.createFile(pathFile);
        }
        if (Files.exists(pathFile) && Files.isWritable(pathFile)) {
            Files.write(pathFile, Collections.singleton(json));
        }
    }

    @Override
    synchronized public void saveUserMessage(String username, String jsonMessage, ISettings settings) throws IOException {
        Path pathFile = Path.of(settings.getPathUsersMessages() + username + ".user");
        if (!Files.exists(pathFile)) {
            Files.createFile(pathFile);
        }
        if (Files.exists(pathFile) && Files.isWritable(pathFile) && !jsonMessage.contains(CommandsList.EXIT.command())) {
            Files.write(pathFile, Collections.singleton(jsonMessage), StandardOpenOption.APPEND);
        }
    }

    @Override
    public List<String> getUserMessages(String username, ISettings settings) throws IOException {
        Path pathFile = Path.of(settings.getPathUsersMessages() + username + ".user");
        if (Files.exists(pathFile) && Files.isReadable(pathFile)) {
            List<String> messages = Files.readAllLines(pathFile);
            if (Files.isWritable(pathFile)) Files.write(pathFile, Collections.singleton(""));
            return messages;
        }
        return new ArrayList<>();
    }
}
