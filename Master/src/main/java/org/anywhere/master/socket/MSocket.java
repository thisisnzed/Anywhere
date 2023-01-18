package org.anywhere.master.socket;

import lombok.Getter;
import org.anywhere.master.Master;
import org.anywhere.master.decoder.DecoderManager;
import org.anywhere.master.encoder.EncoderManager;
import org.anywhere.master.server.ServerData;
import org.anywhere.master.settings.Settings;
import org.anywhere.master.utils.CLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MSocket {

    private final String ip, password;
    private final int port;
    @Getter
    private final EncoderManager encoderManager;
    private final DecoderManager decoderManager;
    private final ServerData serverData;
    private final ConcurrentLinkedQueue<ServerData> serversData;
    @Getter
    private PrintWriter printWriter;
    @Getter
    private Socket socket;

    public MSocket(final Master master, final String ip, final int port, final String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.encoderManager = master.getEncoderManager();
        this.decoderManager = master.getDecoderManager();
        this.serversData = master.serverData;
        this.serverData = master.getServerManager().getServerData(ip, port);
        this.serverData.setThread(Thread.currentThread());
        this.infiniteConnect();
    }

    public void infiniteConnect() {
        while (!this.serverData.isConnected() && !this.serverData.isBlocked()) this.read();
    }

    public void read() {
        try {
            final Socket socket = new Socket(this.ip, this.port);
            final PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            this.socket = socket;
            this.printWriter = printWriter;
            this.serverData.setSocket(socket);
            this.serverData.setPrintWriter(printWriter);
            printWriter.println("M-SRV:" + new String((this.encoderManager.encode("master-" + Settings.RANDOM)).getBytes(StandardCharsets.UTF_8)) + ":" + new String((this.encoderManager.encode(this.password)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
            final InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if ((line == null) || line.equalsIgnoreCase("")) {
                        this.disconnected();
                        socket.close();
                        return;
                    }
                    line = line.contains("all_masters") ? line.split(":")[0] + ":" + new String((this.decoderManager.decode(line.split(":")[2])).getBytes(StandardCharsets.UTF_8)) : line.split(":")[0] + ":" + new String((this.decoderManager.decode(line.split(":")[1])).getBytes(StandardCharsets.UTF_8));
                    /*if (line.startsWith("A:")) {
                        this.readFromAgent(line.replaceFirst("A:", ""));
                    } else*/
                    if (line.startsWith("S:")) {
                        this.readFromServer(line.replaceFirst("S:", ""));
                    }
                } catch (final SocketException ignored) {
                    this.disconnected();
                    return;
                } catch (final IOException ignored) {
                    return;
                }
            }
        } catch (final IOException ignored) {
            this.disconnected();
        }
    }

    private void disconnected() {
        this.printWriter = null;
        this.socket = null;
        this.serverData.setPrintWriter(null);
        this.serverData.setSocket(null);
        this.serverData.setConnected(false);
        CLibrary.INSTANCE.SetConsoleTitleA("Connected to " + this.serversData.stream().filter(ServerData::isConnected).count() + " servers | " + this.serversData.stream().mapToInt(ServerData::getAgents).sum() + " devices");
    }

    private void readFromServer(final String data) {
        if (data.startsWith("updateCounter")) {
            this.serverData.setConnected(true);
            final String[] split = data.replace("updateCounter:", "").split(":");
            this.serverData.setAgents(Integer.parseInt(split[0]));
            this.serverData.setMasters(Integer.parseInt(split[1]));
            CLibrary.INSTANCE.SetConsoleTitleA("Connected to " + this.serversData.stream().filter(ServerData::isConnected).count() + " servers | " + this.serversData.stream().mapToInt(ServerData::getAgents).sum() + " devices");
        } else if (data.startsWith("connectionError")) {
            this.serverData.setBlocked(true);
            switch (data.split(":")[1]) {
                case "ip":
                    this.serverData.setReason("forbidden IP");
                    break;
                case "password":
                    this.serverData.setReason("bad password");
                    break;
            }
            this.disconnected();
        }
    }
}