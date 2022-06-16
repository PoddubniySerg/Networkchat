package repository;

import services.IStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileStorage implements IStorage {

    private final String pathSettingsFile;

    public FileStorage(String pathSettingsFile) {
        this.pathSettingsFile = pathSettingsFile;
    }


    @Override
    public String getSettingsJson() throws IOException {
        return Files.readString(Path.of(this.pathSettingsFile));
    }
}
