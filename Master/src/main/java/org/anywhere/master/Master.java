package org.anywhere.master;

import lombok.Getter;
import org.anywhere.master.command.CommandManager;
import org.anywhere.master.command.CommandParser;
import org.anywhere.master.decoder.DecoderManager;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.server.ServerManager;
import org.anywhere.master.utils.CLibrary;
import org.anywhere.master.utils.FileUtils;
import org.anywhere.master.utils.Print;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Master {

    public static boolean COLORS;

    @Getter
    private final ServerManager serverManager;
    @Getter
    private final EncoderManager encoderManager;
    @Getter
    private final DecoderManager decoderManager;
    @Getter
    private final CommandManager commandManager;
    @Getter
    private final CommandParser commandParser;
    @Getter
    private final FileUtils fileUtils;
    public final ConcurrentLinkedQueue<ServerData> serverData;

    public Master() {
        this.serverData = new ConcurrentLinkedQueue<>();
        this.encoderManager = new EncoderManager();
        this.decoderManager = new DecoderManager();
        this.fileUtils = new FileUtils();
        this.serverManager = new ServerManager(this);
        this.fileUtils.create();
        COLORS = Boolean.parseBoolean(this.fileUtils.getValue("colors"));
        this.commandManager = new CommandManager(this);
        this.commandParser = new CommandParser(this.commandManager);
    }

    public void start() {
        CLibrary.INSTANCE.SetConsoleTitleA("Not connected to any server");
        this.serverManager.loadAllServers();
        Print.print(Print.CONSOLE_CLEAR);
        Print.println("   _____                        .__                                  ____    _______      _______   \n" +
                "  /  _  \\   ____ ___.__.__  _  _|  |__   ___________   ____   ___  _/_   |   \\   _  \\     \\   _  \\  \n" +
                " /  /_\\  \\ /    <   |  |\\ \\/ \\/ /  |  \\_/ __ \\_  __ \\_/ __ \\  \\  \\/ /|   |   /  /_\\  \\    /  /_\\  \\ \n" +
                "/    |    \\   |  \\___  | \\     /|   Y  \\  ___/|  | \\/\\  ___/   \\   / |   |   \\  \\_/   \\   \\  \\_/   \\\n" +
                "\\____|__  /___|  / ____|  \\/\\_/ |___|  /\\___  >__|    \\___  >   \\_/  |___| /\\ \\_____  / /\\ \\_____  /\n" +
                "        \\/     \\/\\/                  \\/     \\/            \\/               \\/       \\/  \\/       \\/ ");
        Print.println(" ");
        while (true) this.commandParser.execute();
    }
}
