package org.anywhere.master.server;

import lombok.Getter;
import lombok.Setter;
import org.anywhere.master.socket.MSocket;

import java.io.PrintWriter;
import java.net.Socket;

public class ServerData {

    @Getter
    @Setter
    private String address, password, reason;
    @Getter
    @Setter
    private int port, masters, agents;
    @Getter
    @Setter
    private Socket socket;
    @Getter
    private MSocket mSocket;
    @Getter
    @Setter
    private PrintWriter printWriter;
    @Getter
    @Setter
    private boolean connected, blocked;
    @Getter
    @Setter
    private Thread thread;

    public ServerData(String address, int port, String password) {
        this.address = address;
        this.port = port;
        this.password = password;
        this.mSocket = null;
        this.socket = null;
        this.printWriter = null;
        this.thread = null;
        this.connected = false;
        this.blocked = false;
        this.masters = 0;
        this.agents = 0;
        this.reason = "connection error";
    }

    public void setMSocket(final MSocket mSocket) {
        this.mSocket = mSocket;
        if (mSocket != null) {
            this.printWriter = mSocket.getPrintWriter();
            this.socket = mSocket.getSocket();
        }
    }
}