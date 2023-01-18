package org.anywhere.master.server;

import org.anywhere.master.Master;
import org.anywhere.master.socket.MSocket;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ServerManager {

    private final Master master;
    private final String path;

    public ServerManager(final Master master) {
        this.master = master;
        this.path = System.getenv("LOCALAPPDATA") + "\\Anywhere\\servers\\";
    }

    public Optional<ServerData> getServer(final String address, final int port) {
        return this.master.serverData.stream().filter(data -> data.getAddress().equals(address) && data.getPort() == port).findFirst();
    }

    public ServerData getServerData(final String address, final int port) {
        return this.master.serverData.stream().filter(data -> data.getAddress().equals(address) && data.getPort() == port).findFirst().orElse(null);
    }

    public ServerData getServerData(final MSocket mSocket) {
        return this.master.serverData.stream().filter(data -> data.getMSocket() == mSocket).findFirst().orElse(null);
    }

    public ServerData getServerData(final Socket socket) {
        return this.master.serverData.stream().filter(data -> data.getSocket() == socket).findFirst().orElse(null);
    }

    public void delete(final String address, final int port) {
        this.getServer(address, port).ifPresent(this.master.serverData::remove);
    }
    public void loadAllServers() {
        final File folder = new File(this.path);
        final File[] fList = folder.listFiles();
        assert fList != null;
        for (final File file : fList) {
            if (file.isFile()) {
                final String[] splited = file.getName().split("@");
                final String address = splited[0];
                final int port = Integer.parseInt(splited[1]);
                final String password = org.anywhere.master.utils.FileUtils.read(file).replace(" ", "").replaceAll("\n", "");
                final ServerData serverData = new ServerData(address, port, password);
                this.master.serverData.add(serverData);
                new Thread(() -> serverData.setMSocket(new MSocket(this.master, address, port, password))).start();

            }
        }
    }

    public boolean addServer(final String address, final int port, final String password) {
        if (this.isExists(address, port)) return false;
        try {
            final String serverPath = this.path + "\\" + address + "@" + port;
            if (new File(serverPath).createNewFile()) {
                final FileWriter fileWriter = new FileWriter(serverPath);
                fileWriter.write(password);
                fileWriter.close();
                final ServerData serverData = new ServerData(address, port, password);
                this.master.serverData.add(serverData);
                new Thread(() -> serverData.setMSocket(new MSocket(this.master, address, port, password))).start();
                return true;
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean removeServer(final String address, final int port) {
        if (!this.isExists(address, port)) return false;
        final String serverPath = this.path + "\\" + address + "@" + port;
        try {
            final ServerData serverData = this.getServerData(address, port);
            FileUtils.forceDelete(new File(serverPath));
            if (serverData.getSocket() != null) {
                serverData.setBlocked(true);
                serverData.getSocket().close();
            }
            serverData.getThread().interrupt();
            this.delete(address, port);
            return true;
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean isExists(final String address, final int port) {
        return this.getServerData(address, port) != null || new File(this.path + "\\" + address + "@" + port).exists();
    }
}
