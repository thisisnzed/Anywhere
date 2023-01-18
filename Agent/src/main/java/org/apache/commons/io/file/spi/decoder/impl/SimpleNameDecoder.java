package org.apache.commons.io.file.spi.decoder.impl;

public class SimpleNameDecoder {

    public String exec(final String str) {
        return new StringBuffer(str).reverse().toString();
    }
}
