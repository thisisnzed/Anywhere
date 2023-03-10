package org.anywhere.agent.socket;

import lombok.Getter;
import lombok.Setter;
import org.anywhere.agent.socket.connection.Connection;
import org.anywhere.agent.Agent;

public class SocketManager {

    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private int port;
    @Getter
    private final Connection connection;

    public SocketManager(final Agent agent) {
        this.address = "127.0.0.1";
        this.port = 5254;
        this.connection = new Connection(agent, this);
    }
}