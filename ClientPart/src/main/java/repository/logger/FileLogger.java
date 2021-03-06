package repository.logger;

import model.response.IResponse;
import model.settings.ISettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Collections;

public class FileLogger implements ILogger {

    private final Path path;

    public FileLogger(ISettings settings) {
        this.path = Path.of(settings.getPathLogFile());
    }

    @Override
    public void log(IResponse message) throws IOException {
        synchronized (this.path) {
            if (!Files.exists(this.path)) {
                Files.createFile(this.path);
            }
            if (Files.exists(this.path) && Files.isWritable(this.path)) {
                Files.write(this.path, Collections.singleton(message.getTitle()), StandardOpenOption.APPEND);
                Files.write(this.path, Collections.singleton(message.getContent()), StandardOpenOption.APPEND);
                Files.write(this.path, Collections.singleton(message.getDateTime()), StandardOpenOption.APPEND);
                Files.write(this.path, Collections.singleton(""), StandardOpenOption.APPEND);
            }
        }
    }

    @Override
    public void log(String message) throws IOException {
        synchronized (this.path) {
            if (!Files.exists(this.path)) {
                Files.createFile(this.path);
            }
            if (Files.exists(this.path) && Files.isWritable(this.path)) {
                Files.write(
                        this.path,
                        Collections.singleton(message + "\n" + LocalDateTime.now() + "\n"), StandardOpenOption.APPEND
                );
            }
        }
    }
}
