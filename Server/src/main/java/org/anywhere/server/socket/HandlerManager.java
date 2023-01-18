package org.anywhere.server.socket;

import lombok.Getter;
import org.anywhere.server.Server;
import org.anywhere.server.socket.handle.*;

public class HandlerManager {

    @Getter
    private final Writer writer;
    @Getter
    private final Register register;
    @Getter
    private final Checker checker;
    @Getter
    private final Remover remover;
    @Getter
    private final Updater updater;

    public HandlerManager(final Server server) {
        this.writer = new Writer(server);
        this.register = new Register(this, server);
        this.checker = new Checker(server);
        this.remover = new Remover(this, server);
        this.updater = new Updater(this, server);
    }
}
