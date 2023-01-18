package org.apache.commons.io.file.spi.arguments;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ArgumentParser {

    private final Configuration configuration;
    private final String[] args;

    public ArgumentParser(final Configuration configuration, final String[] args) {
        this.configuration = configuration;
        this.args = args;
    }

    public void loadArguments() {
        final AtomicReference<String> latest = new AtomicReference<>("unknown");
        Arrays.stream(this.args).forEach(argument -> {
            if (!latest.get().equals("unknown")) {
                this.configuration.set(latest.toString().replaceFirst("-", ""), argument);
                latest.set("unknown");
            } else latest.set(argument);
        });
    }
}