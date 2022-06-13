package model;

import services.ILogger;
import services.ISettings;
import services.IStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final String EXIT_COMMAND = "/exit";
    private final ISettings settings;
    private final IStorage storage;
    private final ILogger logger;

    public ChatServer(ISettings settings, IStorage storage, ILogger logger) {
        this.settings = settings;
        this.storage = storage;
        this.logger = logger;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(this.settings.getPort());
             BufferedReader adminCommand = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Server is started on port " + this.settings.getPort());
            System.out.println("For stop enter '" + EXIT_COMMAND + "'");
            this.logger.log("Server is started");
            Thread connectionThread = new Thread(() -> this.connect(server));
            connectionThread.start();
            while (!server.isClosed()) {
                if (adminCommand.ready()) {
                    if (adminCommand.readLine().equalsIgnoreCase(EXIT_COMMAND)) {
                        this.disableUsers();
                        server.close();
                        this.storage.closeStorage();
                        this.logger.log("Server is stopped");
                        connectionThread.interrupt();
                        System.out.println("Server is stopped");
                        break;
                    }
                }
            }
        } catch (IOException exception) {
            logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private void connect(ServerSocket server) {
        final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                runnableTask -> {
                    Thread thread = Executors.defaultThreadFactory().newThread(runnableTask);
                    thread.setDaemon(true);
                    return thread;
                }
        );
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket clientAccept = server.accept();
                ClientService clientService =
                        new ClientService(clientAccept, this.logger, this.storage);
                threadPool.execute(clientService::start);
            } catch (IOException exception) {
                logger.log(exception.getMessage());
                threadPool.shutdownNow();
            }
        }
    }

    private void disableUsers() {
        for (String user : this.storage.getUsernameSet()) {
            if (this.storage.getUser(user).isUserOnline()) {
                new MessageHandler(this.logger, this.storage).inputMessage(
                        "is disabled"
                        , user
                        , LocalDateTime.now().toString()
                        , true);
                this.storage.getUser(user).setUserStatusOnline(false);
            }
        }
    }
}
