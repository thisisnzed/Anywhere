package org.anywhere.server.socket.handle;

import org.anywhere.server.Server;
import org.anywhere.server.data.UserData;
import org.anywhere.server.runnable.ExecutorRunnable;
import org.anywhere.server.socket.HandlerManager;
import org.anywhere.server.socket.TSocket;
import org.anywhere.server.utils.TimeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Register {

    private final Server server;
    private final HandlerManager handlerManager;

    public Register(final HandlerManager handlerManager, final Server server) {
        this.server = server;
        this.handlerManager = handlerManager;
    }

    public void register(final TSocket tSocket, final ExecutorRunnable executorRunnable, final boolean master, final Socket socket, final Thread thread, final String id, final String password, final String data) {
        if (this.handlerManager.getChecker().isExists(socket, id)) {
            this.handlerManager.getRemover().close(socket, thread);
            return;
        }
        final String[] remoteSocketAddress = socket.getRemoteSocketAddress().toString().split(":");
        final String address = remoteSocketAddress[0].replace("/", "");
        final String port = remoteSocketAddress[1];
        final Updater updater = this.handlerManager.getUpdater();
        if (master) {
            final String allowed = String.valueOf(this.server.getConfiguration().get("allowed"));
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            if (!allowed.contains(";" + address + ";") && !allowed.equalsIgnoreCase("all")) {
                System.out.println(TimeUtils.getDate() + "(Master) Connection refused from " + address + " due to forbidden IP");
                this.sendError(printWriter, "ip");
                return;
            }
            if (!password.equals(String.valueOf(this.server.getConfiguration().get("password")))) {
                System.out.println(TimeUtils.getDate() + "(Master) Connection refused from " + address + " due to bad password");
                this.sendError(printWriter, "password");
                return;
            }
            this.server.userData.add(new UserData(thread, socket, true, id, "master"));
            System.out.println(TimeUtils.getDate() + "(Master) Connection established from " + address + ":" + port);
                /*this.server.userData.stream().filter(userData -> !userData.isMaster()).forEach(userData -> {
                    final String[] userDataSocket = userData.getSocket().getRemoteSocketAddress().toString().split(":");
                    this.server.getHandlerManager().getWriter().writeToMaster(id, "S", new String((this.server.getEncoderManager().encode("addAgent:" + userDataSocket[0].replace("/", "") + ":" + userDataSocket[1] + ":" + userData.getIdentifier().replace(":", "") + ":" + userData.getData())).getBytes(StandardCharsets.UTF_8)));
                });*/
        } else {
            this.server.userData.add(new UserData(thread, socket, false, id, data));
            /*updater.addAgent(id, data, address, port);*/
            System.out.println(TimeUtils.getDate() + "(Agent) Connection established from " + address + ":" + port);
            executorRunnable.hashMapBoolean.put(tSocket, false);
            executorRunnable.hashMap.put(tSocket, -1L);
        }
        updater.updateCounter();
    }

    private void sendError(final PrintWriter printWriter, final String message) {
        if(printWriter != null) {
            printWriter.println("S:" + new String((this.server.getEncoderManager().encode("connectionError:" + message)).getBytes(StandardCharsets.UTF_8)));
            printWriter.flush();
        }
    }
}
