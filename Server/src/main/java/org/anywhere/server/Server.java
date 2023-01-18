package org.anywhere.server;

import lombok.Getter;
import org.anywhere.server.arguments.ArgumentParser;
import org.anywhere.server.arguments.Configuration;
import org.anywhere.server.data.UserData;
import org.anywhere.server.data.UserManager;
import org.anywhere.server.decoder.DecoderManager;
import org.anywhere.server.encoder.EncoderManager;
import org.anywhere.server.runnable.ExecutorRunnable;
import org.anywhere.server.socket.HandlerManager;
import org.anywhere.server.socket.TSocket;
import org.anywhere.server.utils.TimeUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class Server {

    public final ConcurrentLinkedQueue<UserData> userData;
    private final Configuration configuration;
    private final EncoderManager encoderManager;
    private final DecoderManager decoderManager;
    private final UserManager userManager;
    private final HandlerManager handlerManager;
    public ExecutorRunnable executorRunnable;

    public Server(final String[] args) {
        this.userData = new ConcurrentLinkedQueue<>();
        this.executorRunnable = new ExecutorRunnable(this);
        this.encoderManager = new EncoderManager();
        this.decoderManager = new DecoderManager();
        this.handlerManager = new HandlerManager(this);
        this.userManager = new UserManager(this);
        this.configuration = new Configuration();

        final ArgumentParser argumentParser = new ArgumentParser(this.configuration, args);
        this.configuration.set("allowed", "127.0.0.1");
        this.configuration.set("port", 5254);
        this.configuration.set("debug", false);
        this.configuration.set("password", "HbqFKj8qEFOCjhAnut0oulvhP1WG7e644wmdsWSA");
        argumentParser.loadArguments();
    }

    public void start() {
        this.executorRunnable.start();
        ServerSocket serverSocket = null;
        Socket socket;
        try {
            int port = Integer.parseInt(String.valueOf(this.configuration.get("port")));
            serverSocket = new ServerSocket(port);
            System.out.println(TimeUtils.getDate() + "Server started with port " + port + "!");
        } catch (final IOException exception) {
            exception.printStackTrace();
            System.out.println(TimeUtils.getDate() + "Server returned error : " + exception.getMessage() + " | " + exception.getCause().toString());
        }
        while (true) {
            try {
                socket = Objects.requireNonNull(serverSocket).accept();
                new TSocket(this, socket).start();
            } catch (final IOException exception) {
                exception.printStackTrace();
                System.out.println(TimeUtils.getDate() + "Connection error (IO Exception - A) : " + exception.getMessage() + " | " + exception.getCause().toString());
            }
        }
    }
}
