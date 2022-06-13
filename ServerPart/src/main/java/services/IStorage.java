package services;


import java.util.Set;

public interface IStorage {

    IUser getUser(String username);

    Set<String> getUsernameSet();

    boolean newUser(String username, String password);

    void closeStorage();
}
