package repository.storage;

import java.io.IOException;

public interface IStorage {

    String getSettingsJson() throws IOException;
}
