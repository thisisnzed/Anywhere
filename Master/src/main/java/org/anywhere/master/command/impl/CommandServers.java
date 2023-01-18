package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.server.ServerManager;
import org.anywhere.master.utils.CLibrary;
import org.anywhere.master.utils.NumberUtils;
import org.anywhere.master.utils.Print;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandServers extends Command {

    private final ConcurrentLinkedQueue<ServerData> serverData;
    private final ServerManager serverManager;

    public CommandServers(final ConcurrentLinkedQueue<ServerData> serverData, final ServerManager serverManager) {
        super("server", "servers", "server <list/add/remove> [address] [port] [password]", "Manage the servers to which the master is connected", false);
        this.serverData = serverData;
        this.serverManager = serverManager;
    }

    @Override
    public void execute(final String[] args) {
        if (args.length == 1) {
            Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
            return;
        }
        switch (args[1].toLowerCase()) {
            case "list": {
                if (this.serverData == null || this.serverData.size() == 0) {
                    Print.println(Print.LIGHT_RED + "You haven't added any servers");
                    return;
                }
                Print.println(" ");
                this.serverData.forEach(serverData -> Print.println("  * " + Print.LIGHT_CYAN + serverData.getAddress() + ":" + serverData.getPort() + Print.LIGHT_GREY + " - " + (serverData.isConnected() ? "Connected (" + Print.LIGHT_CYAN + serverData.getAgents() + Print.LIGHT_GREY + ")" : "Disconnected")));
                Print.println(" ");
                Print.println(Print.LIGHT_GREY + "  Connected to " + Print.LIGHT_CYAN + this.serverData.stream().filter(ServerData::isConnected).count() + "/" + this.serverData.size() + Print.LIGHT_GREY + " servers for " + Print.LIGHT_CYAN + this.serverData.stream().mapToInt(ServerData::getAgents).sum() + Print.LIGHT_GREY + " agents");
                Print.println(" ");
                break;
            }
            case "add": {
                if (args.length != 5 || !NumberUtils.isInteger(args[3])) {
                    Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
                    return;
                }
                final String address = args[2];
                final int port = Integer.parseInt(args[3]);
                final String password = args[4];
                if (this.serverManager.isExists(address, port)) {
                    Print.println(Print.LIGHT_RED + "The server is already registered");
                    return;
                }
                if (!this.serverManager.addServer(address, port, password)) {
                    Print.println(Print.LIGHT_RED + "Cannot register " + address + ":" + port + " due to error");
                    return;
                }
                Print.println(Print.LIGHT_GREY + "Added " + Print.LIGHT_CYAN + address + ":" + port + Print.LIGHT_GREY + " to servers");
                CLibrary.INSTANCE.SetConsoleTitleA("Connected to " + this.serverData.stream().filter(ServerData::isConnected).count() + " servers | " + this.serverData.stream().mapToInt(ServerData::getAgents).sum() + " devices");
                break;
            }
            case "remove": {
                if (args.length != 4 || !NumberUtils.isInteger(args[3])) {
                    Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
                    return;
                }
                final String address = args[2];
                final int port = Integer.parseInt(args[3]);
                if (!this.serverManager.isExists(address, port)) {
                    Print.println(Print.LIGHT_RED + "The server has never been registered");
                    return;
                }
                if (!this.serverManager.removeServer(address, port)) {
                    Print.println(Print.LIGHT_RED + "Cannot remove " + address + ":" + port + " due to error");
                    return;
                }
                Print.println(Print.LIGHT_GREY + "Removed " + Print.LIGHT_CYAN + address + ":" + port + Print.LIGHT_GREY + " from servers");
                CLibrary.INSTANCE.SetConsoleTitleA("Connected to " + this.serverData.stream().filter(ServerData::isConnected).count() + " servers | " + this.serverData.stream().mapToInt(ServerData::getAgents).sum() + " devices");
                break;
            }
            default: {
                Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
                break;
            }
        }
    }
}