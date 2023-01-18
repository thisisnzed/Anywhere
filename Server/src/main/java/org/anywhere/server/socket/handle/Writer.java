package org.anywhere.server.socket.handle;

import org.anywhere.server.Server;
import org.anywhere.server.data.UserData;

import java.io.PrintWriter;

public class Writer {

    private final Server server;

    public Writer(final Server server) {
        this.server = server;
    }

    public void writeToAgent(final String line) {
        this.server.userData.stream().filter(agent -> !agent.isMaster()).forEach(userData -> {
            final PrintWriter printWriter = userData.getPrintWriter();
            printWriter.println(line);
            printWriter.flush();
            //if (request.equals("restartSocket")) this.server.getHandlerManager().getRemover().remove(null, userData.getSocket(), userData.getIdentifier(), false);
        });
    }

    public void writeToMaster(final String id, final String from, final String text) {
        if (id.equals("all_masters")) {
            this.server.userData.stream().filter(UserData::isMaster).map(UserData::getPrintWriter).forEach(printWriter -> {
                printWriter.println(from + ":" + text);
                printWriter.flush();
            });
        } else {
            final UserData userData = this.server.getUserManager().getUserData(id);
            if (userData == null) return;
            final PrintWriter printWriter = userData.getPrintWriter();
            printWriter.println(from + ":" + text);
            printWriter.flush();
        }
    }
}