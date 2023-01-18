package org.anywhere.server.launch;

import org.anywhere.server.Server;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class Launch {

    public static void main(final String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        /*try {
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (final IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }*/
        new Server(args).start();
    }
}
