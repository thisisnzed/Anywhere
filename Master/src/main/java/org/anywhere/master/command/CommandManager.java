package org.anywhere.master.command;

import lombok.Getter;
import org.anywhere.master.Master;
import org.anywhere.master.command.impl.*;
import org.anywhere.master.command.impl.methods.*;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandManager {

    @Getter
    private final ArrayList<Command> commands;

    public CommandManager(final Master master) {
        final ConcurrentLinkedQueue<ServerData> serverData = master.serverData;
        final EncoderManager encoderManager = master.getEncoderManager();
        this.commands = new ArrayList<>();

        this.commands.add(new CommandHelp(this));
        this.commands.add(new CommandExit());
        this.commands.add(new CommandColors(master));
        this.commands.add(new CommandPing());
        this.commands.add(new CommandServers(serverData, master.getServerManager()));
        this.commands.add(new CommandDownload(serverData, encoderManager));
        this.commands.add(new CommandStop(serverData, encoderManager));
        this.commands.add(new CommandMethods(this));

        // METHODS
        this.commands.add(new CommandHttp(serverData, encoderManager));
        this.commands.add(new CommandSlowHTTP(serverData, encoderManager));
        this.commands.add(new CommandSocket(serverData, encoderManager));
        this.commands.add(new CommandUDP(serverData, encoderManager));
    }
}
