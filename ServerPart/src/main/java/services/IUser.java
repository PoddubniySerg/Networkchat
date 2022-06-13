package services;

public interface IUser {

    boolean isUserOnline();

    void setUserStatusOnline(boolean isOnline);

    String getUsername();

    String getPassword();

    boolean messageListIsEmpty();

    String getJsonMessages();

    IMessage newMessage(String title, String content, String localDateTime);
}
