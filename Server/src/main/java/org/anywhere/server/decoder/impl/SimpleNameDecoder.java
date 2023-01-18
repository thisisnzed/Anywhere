package org.anywhere.server.decoder.impl;

public class SimpleNameDecoder {

    public String exec(final String str) {
        return new StringBuffer(str).reverse().toString();
    }
}
