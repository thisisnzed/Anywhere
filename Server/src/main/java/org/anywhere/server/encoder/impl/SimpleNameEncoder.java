package org.anywhere.server.encoder.impl;

public class SimpleNameEncoder {

    public String exec(final String str) {
        return new StringBuffer(str).reverse().toString();
    }
}
