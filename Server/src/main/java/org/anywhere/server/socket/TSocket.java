package org.anywhere.server.socket;

import lombok.Getter;
import org.anywhere.server.Server;
import org.anywhere.server.decoder.DecoderManager;
import org.anywhere.server.runnable.ExecutorRunnable;
import org.anywhere.server.utils.TimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class TSocket extends Thread {

    private final Server server;
    private final DecoderManager decoderManager;
    private final HandlerManager handlerManager;
    @Getter
    public final Socket socket;
    private final boolean debug;
    private final ExecutorRunnable executorRunnable;
    private BufferedReader bufferedReader;

    public TSocket(final Server server, final Socket socket) {
        this.server = server;
        this.debug = Boolean.parseBoolean(String.valueOf(server.getConfiguration().get("debug")));
        this.decoderManager = server.getDecoderManager();
        this.handlerManager = server.getHandlerManager();
        this.executorRunnable = server.getExecutorRunnable();
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (final IOException exception) {
            exception.printStackTrace();
            System.out.println(TimeUtils.getDate() + "Connection error (IO Exception - B) : " + exception.getMessage() + " | " + exception.getCause().toString());
        }
    }

    public void run() {
        while (true) {
            try {
                final String line = this.bufferedReader.readLine();
                if (line == null || line.equals("")) {
                    this.socket.close();
                    return;
                } else {
                    if (this.debug)
                        System.out.println("[DEBUG/INFO - " + this.socket.getRemoteSocketAddress() + "] " + line);
                    if (line.equals("sendActivity")) {
                        this.executorRunnable.hashMap.put(this, System.currentTimeMillis());
                    } else if (line.startsWith("A-SRV:")) { //-> Agent to Server Only
                        this.handlerManager.getRegister().register(this, this.executorRunnable, false, this.socket, this, new String((this.decoderManager.decode(line.split(":")[1])).getBytes(StandardCharsets.UTF_8)), null, this.decoderManager.decode(line.split(":")[2]));
                    } else if (line.startsWith("M-SRV:")) { //-> Master to Server Only
                        this.handlerManager.getRegister().register(this, this.executorRunnable, true, this.socket, this, new String((this.decoderManager.decode(line.split(":")[1])).getBytes(StandardCharsets.UTF_8)), new String((this.decoderManager.decode(line.split(":")[2])).getBytes(StandardCharsets.UTF_8)), "master");
                    } else if (line.startsWith("M:")) { //-> Master to Specific Agent (or all with : all_agents)
                        if (this.server.userData.stream().noneMatch(userData -> userData.getSocket() == this.socket && userData.isMaster()))
                            return;
                        this.handlerManager.getWriter().writeToAgent(line);
                    } else if (line.startsWith("A:") || line.startsWith("A-REG:")) { //-> Agent to all Masters
                        if (this.server.getUserManager().getUserData(this.socket) != null) {
                            final String from = line.split(":")[0];
                            this.handlerManager.getWriter().writeToMaster("all_masters", from, line.replaceFirst(from + ":", ""));
                        }
                    }
                }
            } catch (final SocketTimeoutException | SocketException ignored) {
                this.close();
                return;
            } catch (final IOException exception) {
                exception.printStackTrace();
                this.close();
                return;
            } finally {
                if (this.socket.isClosed() && this.server.getUserManager().getUserData(this.socket) != null)
                    this.handlerManager.getRemover().remove(this, this.socket, this.server.getUserManager().getUserData(this.socket).getIdentifier(), false);
            }
        }
    }

    private void close() {
        try {
            this.socket.close();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        super.interrupt();
    }
}