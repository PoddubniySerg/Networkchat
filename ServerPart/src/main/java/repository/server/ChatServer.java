package repository.server;

import model.CommandsList;
import services.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final IMessageHandlerFactory messageHandlerFactory;
    private final ISettings settings;
    private final ILogger logger;
    private final IStorage storage;
    private final IAdmin admin;

    public ChatServer(
            IMessageHandlerFactory messageHandlerFactory,
            ISettings settings,
            ILogger logger,
            IStorage storage,
            IAdmin admin) {
        this.messageHandlerFactory = messageHandlerFactory;
        this.settings = settings;
        this.logger = logger;
        this.storage = storage;
        this.admin = admin;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(this.settings.getPort())) {
            admin.printMessage("Server is started on port " + this.settings.getPort());
            admin.printMessage("For stop enter '" + CommandsList.EXIT.command() + "'");
            this.logger.log("Server is started");
            Thread connectionThread = new Thread(() -> this.connect(server));
            connectionThread.start();
            while (!server.isClosed()) {
                if (this.admin.getCommand().equals(CommandsList.EXIT.command())) {
                    this.storage.close(this.settings);
                    server.close();
                    connectionThread.interrupt();
                    this.logger.log("Server is stopped");
                    admin.printMessage("Server is stopped");
                    break;
                }
            }
        } catch (IOException exception) {
            logger.log(exception.getMessage());
            throw new RuntimeException(exception);
        }
        this.admin.closeAdmin();
    }

    private void connect(ServerSocket server) {
        final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                runnableTask -> {
                    Thread thread = Executors.defaultThreadFactory().newThread(runnableTask);
                    thread.setDaemon(true);
                    return thread;
                }
        );
        while (!server.isClosed() && !Thread.currentThread().isInterrupted()) {
            try {
                Socket clientAccept = server.accept();
                SocketClientService clientService = new SocketClientService
                        (clientAccept, this.logger, this.admin, this.messageHandlerFactory.newClientService(this.storage));
                threadPool.execute(() -> clientService.start(this.settings));
            } catch (IOException exception) {
                logger.log(exception.getMessage());
                admin.printMessage(exception.getMessage());
                threadPool.shutdownNow();
            }
        }
    }
}
