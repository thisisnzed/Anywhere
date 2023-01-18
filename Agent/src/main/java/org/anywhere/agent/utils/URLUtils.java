package org.anywhere.agent.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {

    public static String readFromURL(final String address) throws IOException {
        final URL url = new URL(address);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Cache-Control", "no-store");
        con.setRequestProperty("Expires", "0");
        con.setRequestProperty("Pragma", "no-cache");
        con.setConnectTimeout(30000);
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            return response.toString();
        } catch (final Exception ignored) {
        }
        throw new IOException("URL Error");
    }

    public static void download(final String link, final File tempPath) throws IOException {
        final URL url = new URL(link);
        final URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Cache-Control", "no-store");
        con.setRequestProperty("Expires", "0");
        con.setRequestProperty("Pragma", "no-cache");
        final DataInputStream dis = new DataInputStream(con.getInputStream());
        final byte[] fileData = new byte[con.getContentLength()];
        for (int q = 0; q < fileData.length; q++) fileData[q] = dis.readByte();
        dis.close();
        final FileOutputStream fos = new FileOutputStream(tempPath);
        fos.write(fileData);
        fos.close();
    }
}
