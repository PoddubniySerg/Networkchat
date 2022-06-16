package model;

import org.json.simple.parser.ParseException;
import services.*;

import java.io.IOException;
import java.util.List;

public class Model implements IModel {
    private static final String EXIT_COMMAND = "/exit";

    private final ILogger logger;
    private final IView view;
    private final IMessageFactory messageFactory;

    public Model(ILogger logger, IView view, IMessageFactory messageFactory) {
        this.logger = logger;
        this.view = view;
        this.messageFactory = messageFactory;
    }

    @Override
    public void start(INetClient netClient) throws IOException {
        this.logger.log(this.messageFactory.newMessage("Start the client"));
        if (this.isLogged(netClient)) {
            Thread inputThread = new Thread(() -> {
                try {
                    this.inputFromServer(netClient, this.logger, this.view);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            inputThread.setDaemon(true);
            inputThread.start();
            this.sendMessageToServer(netClient, inputThread);
            inputThread.interrupt();
            this.logger.log(this.messageFactory.newMessage("The program is finished"));
        } else {
            this.logger.log(this.messageFactory.newMessage("The program is closed without authorization"));
        }
        this.view.printMessage("The program is finished");
        this.view.closeView();
    }

    private void sendMessageToServer(INetClient netClient, Thread inputThread) throws IOException {
        IMessage messageInit = this.messageFactory.newMessage("attached to chat");
        netClient.sendString("/message");
        netClient.sendString(messageInit.getContent());
        netClient.sendString(messageInit.getDateTime());
        this.view.printMessage(messageInit.toString());
        this.logger.log(messageInit);
        String userInput = this.view.getString();
        while (!userInput.equals(EXIT_COMMAND) && inputThread.isAlive()) {
            if (!userInput.isEmpty()) {
                IMessage message = this.messageFactory.newMessage(userInput);
                netClient.sendString("/message");
                netClient.sendString(message.getContent());
                netClient.sendString(message.getDateTime());
                this.logger.log(message);
            }
            userInput = this.view.getString();
        }
        IMessage messageExit = this.messageFactory.newMessage("leave chat");
        netClient.sendString(EXIT_COMMAND);
        netClient.sendString(messageExit.getContent());
        netClient.sendString(messageExit.getDateTime());
        this.view.printMessage(messageExit.toString());
        this.logger.log(messageExit);
    }


    private void inputFromServer(INetClient netClient, ILogger logger, IView view) throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String inputStr = netClient.readLine();
                List<IMessage> messages = this.messageFactory.getMessagesFromJson(inputStr);
                if (!messages.isEmpty()) {
                    for (IMessage message : messages) {
                        view.printMessage(message.toString());
                        logger.log(message);
                    }
                }
            }
        } catch (IOException | ParseException exception) {
            this.logger.log(this.messageFactory.newMessage(exception.getMessage()));
            view.printMessage(exception.getMessage());
        }
    }

    private boolean isLogged(INetClient netClient) throws IOException {
        try {
            netClient.sendString("/authorization");
            while (netClient.sergverIsConnected()) {
                String serverResponse = netClient.readLine();
                this.view.printMessage(serverResponse);
                String userInput = this.view.getString();
                netClient.sendString(userInput);
                if (userInput.equals(EXIT_COMMAND)) return false;
                serverResponse = netClient.readLine();
                if (serverResponse.equals("OK!")) return true;
                this.view.printMessage(serverResponse);
            }
        } catch (IOException exception) {
            this.logger.log(this.messageFactory.newMessage(exception.getMessage()));
            return false;
        }
        this.logger.log(this.messageFactory.newMessage("Disconnected by server"));
        return false;
    }
}
