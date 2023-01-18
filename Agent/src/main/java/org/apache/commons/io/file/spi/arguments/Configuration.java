package org.apache.commons.io.file.spi.arguments;

import java.util.concurrent.ConcurrentHashMap;

public class Configuration {

    private final ConcurrentHashMap<String, Object> args;

    public Configuration() {
        this.args = new ConcurrentHashMap<>();
    }

    public void set(final String key, final Object value) {
        this.args.put(key, value);
    }

    public Object get(final String key) {
        return this.args.get(key);
    }
}