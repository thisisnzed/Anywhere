package org.apache.commons.io.file.spi.runnable;

import org.apache.commons.io.file.spi.socket.connection.Connection;

import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorRunnable {

    private final Connection connection;

    public ExecutorRunnable(final Connection connection) {
        this.connection = connection;
    }

    public void start() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (this.connection.isConnected()) {
                if (this.connection.getPrintWriter() != null) {
                    final PrintWriter printWriter = this.connection.getPrintWriter();
                    printWriter.println("sendActivity");
                    printWriter.flush();
                }
            }
        }, 0L, 45L, TimeUnit.SECONDS);
    }
}