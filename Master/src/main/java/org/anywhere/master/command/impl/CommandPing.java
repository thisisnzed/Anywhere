package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.utils.NumberUtils;
import org.anywhere.master.utils.Print;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class CommandPing extends Command {

    public CommandPing() {
        super("ping", null, "ping <host> <port>", "Get server response time", false);
    }

    @Override
    public void execute(final String[] args) {
        if (args.length != 3 || !NumberUtils.isInteger(args[2])) {
            Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
            return;
        }
        final String host = args[1];
        final int port = Integer.parseInt(args[2]);
        try {
            final InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
            final SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            final Date date = new Date();
            if (!socketChannel.connect(socketAddress)) {
                Print.println(Print.LIGHT_RED + "Cannot reach " + host + ":" + port + ", probably down?");
                return;
            }
            Print.println(Print.LIGHT_GREY + "Response time from " + Print.LIGHT_CYAN + host + ":" + port + Print.LIGHT_GREY + " is " + Print.LIGHT_CYAN + (new Date().getTime() - date.getTime()) + "ms");
        } catch (final IOException exception) {
            Print.println(Print.LIGHT_RED + "Cannot reach " + host + ":" + port + ", probably down?");
        }
    }
}
