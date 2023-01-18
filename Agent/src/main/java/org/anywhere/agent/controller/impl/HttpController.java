package org.anywhere.agent.controller.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpController {

    public void attack(final String host, final String agent, final ConcurrentLinkedQueue<Object> requests) {
        try {
            final URL url = new URL(host);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", agent);
            connection.setConnectTimeout(13000);
            connection.connect();
            connection.getResponseCode();
            requests.add(connection);
        } catch (final NoSuchElementException | IOException ignored) {
        }
    }
}
