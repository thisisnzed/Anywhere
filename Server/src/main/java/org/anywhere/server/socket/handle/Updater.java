package org.anywhere.server.socket.handle;

import org.anywhere.server.Server;
import org.anywhere.server.data.UserData;
import org.anywhere.server.encoder.EncoderManager;
import org.anywhere.server.socket.HandlerManager;

import java.nio.charset.StandardCharsets;

public class Updater {

    private final Server server;
    private final Writer writer;
    private final EncoderManager encoderManager;

    public Updater(final HandlerManager handlerManager, final Server server) {
        this.server = server;
        this.writer = handlerManager.getWriter();
        this.encoderManager = server.getEncoderManager();
    }

    public void updateCounter() {
        this.writer.writeToMaster("all_masters", "S", new String((this.encoderManager.encode("updateCounter:" + (int) this.server.userData.stream().filter(userData -> !userData.isMaster()).count() + ":" + (int) this.server.userData.stream().filter(UserData::isMaster).count())).getBytes(StandardCharsets.UTF_8)));
    }

    /*public void addAgent(final String id, final String data, final String address, final String port) {
        this.writer.writeToMaster("all_masters", "S", new String((this.encoderManager.encode("addAgent:" + address + ":" + port + ":" + this.removeColons(id) + ":" + data)).getBytes(StandardCharsets.UTF_8)));
    }

    public void removeAgent(final String id) {
        this.writer.writeToMaster("all_masters", "S", new String((this.encoderManager.encode("removeAgent:" + id)).getBytes(StandardCharsets.UTF_8)));
    }

    private String removeColons(final String text) {
        return text.replace(":", "");
    }*/
}
