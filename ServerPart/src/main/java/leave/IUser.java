package leave;

import services.IMessage;

public interface IUser {

    boolean isUserOnline();

    void setUserStatusOnline(boolean isOnline);

    String getUsername();

    String getPassword();

    boolean messageListIsEmpty();

    IMessage nextMessage();

    void newMessage(IMessage message);
}
