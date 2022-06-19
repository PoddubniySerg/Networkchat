package repository;

import services.ISettings;
import services.IRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

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
        if (Files.exists(pathFile) && Files.isWritable(pathFile)) {
            Files.write(pathFile, Collections.singleton(json));
        }
    }
}
