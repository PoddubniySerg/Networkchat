package services;

public interface IAdmin {

    String getCommand();

    void printMessage(String message);

    void closeAdmin();
}
