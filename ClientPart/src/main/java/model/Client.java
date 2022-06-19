package model;

import services.*;

import java.io.IOException;

public class Client implements IClient {
    private static final String EXIT_COMMAND = "/exit";
    private static final String INFO_CONTENT = "/info";
    private static final String VALID_COMMAND = "/ok";

    private final ILogger logger;
    private final IView view;

    public Client(ILogger logger, IView view) {
        this.logger = logger;
        this.view = view;
    }

    @Override
    public void start(IServer server) throws IOException {
        String startText = "Start the client";
        String stopWithLogged = "Stop the client";
        String stopWithoutLogged = "Stop the client without authorization";
        String endText = "The program is finished";
        this.logger.log(startText);
        this.view.printMessage(startText);
        if (this.isLogged(server)) {
            Thread outputThread = new Thread(() -> this.sendMessageToServer(server));
            outputThread.setDaemon(true);
            outputThread.start();
            this.inputFromServer(server);
            outputThread.interrupt();
            this.logger.log(stopWithLogged);
            this.view.printMessage(stopWithLogged);
        } else {
            this.logger.log(stopWithoutLogged);
            this.view.printMessage(stopWithoutLogged);
        }
        this.logger.log(endText);
        this.view.printMessage(endText);
        server.close();
        this.view.closeView();
    }

    private void sendMessageToServer(IServer server) {
        while (!Thread.currentThread().isInterrupted() && server.serverIsConnected()) {
            server.sendRequest(this.view.getString());
        }
    }


    private void inputFromServer(IServer server) throws IOException {
        try {
            IResponse response = server.getResponse();
            while (!response.getTitle().equals(EXIT_COMMAND) && server.serverIsConnected()) {
                if (!response.getContent().isEmpty()) {
                    view.printMessage(response.toString());
                    logger.log(response);
                }
                response = server.getResponse();
            }
        } catch (IOException | NullPointerException exception) {
            this.logger.log(exception.getMessage());
            view.printMessage(exception.getMessage());
        }
    }

    private boolean isLogged(IServer server) throws IOException {
        try {
            server.sendRequest("/authorization");
            while (server.serverIsConnected()) {
                IResponse serverResponse = server.getResponse();
                if (
                        serverResponse == null
                                || serverResponse.getTitle() == null
                                || serverResponse.getContent() == null
                                || serverResponse.getDateTime() == null
                ) {
                    return false;
                }
                switch (serverResponse.getTitle()) {
                    case EXIT_COMMAND:
                        return false;
                    case VALID_COMMAND:
                        return true;
                    case INFO_CONTENT:
                        this.view.printMessage(serverResponse.getContent());
                        break;
                    default:
                        this.view.printMessage(serverResponse.getContent());
                        server.sendRequest(this.view.getString());
                        break;
                }
            }
        } catch (IOException | NullPointerException exception) {
            this.logger.log(exception.getMessage());
            this.view.printMessage(exception.getMessage());
            return false;
        }
        this.logger.log("Disconnected by server");
        return false;
    }
}
