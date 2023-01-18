package org.anywhere.server.runnable;

import org.anywhere.server.Server;
import org.anywhere.server.socket.TSocket;
import org.anywhere.server.data.UserManager;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorRunnable {

    public final ConcurrentHashMap<TSocket, Long> hashMap;
    public final ConcurrentHashMap<TSocket, Boolean> hashMapBoolean;
    private final Server server;

    public ExecutorRunnable(final Server server) {
        this.hashMap = new ConcurrentHashMap<>();
        this.hashMapBoolean = new ConcurrentHashMap<>();
        this.server = server;
    }

    public void start() {
        final UserManager userManager = this.server.getUserManager();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> this.hashMap.keySet().forEach(tSocket -> {
            final Socket socket = tSocket.getSocket();
            if (userManager.getUserData(socket) != null) {
                final long lastActivity = this.hashMap.get(tSocket);
                if (lastActivity != -1L && (lastActivity < System.currentTimeMillis() - 100000L)) {
                    if (!tSocket.isInterrupted() && !socket.isClosed() && this.server.getUserManager().getUserData(socket) != null) {
                        if (!this.hashMapBoolean.get(tSocket)) {
                            this.hashMapBoolean.put(tSocket, true);
                            this.server.getHandlerManager().getRemover().remove(null, socket, userManager.getUserData(socket).getIdentifier(), true);
                            this.remove(tSocket);
                        }
                    } else this.remove(tSocket);
                }
            } else this.remove(tSocket);
        }), 0L, 120L, TimeUnit.SECONDS);
    }

    private void remove(final TSocket tSocket) {
        this.hashMap.remove(tSocket);
        this.hashMapBoolean.remove(tSocket);
    }
}
