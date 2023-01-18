package org.anywhere.server.socket.handle;

import org.anywhere.server.Server;
import org.anywhere.server.data.UserManager;

import java.net.Socket;

public class Checker {

    private final Server server;

    public Checker(final Server server) {
        this.server = server;
    }

    public boolean isExists(final Socket socket, final String id) {
        final UserManager userManager = this.server.getUserManager();
        return userManager.getUserData(socket) != null || userManager.getUserData(id) != null;
    }
}
