package org.apache.commons.io.file.spi.decoder.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class CompressedBase64Decoder {

    public String exec(final String str) {
        final byte[] decodedBytes = Base64.getDecoder().decode(str);
        if (decodedBytes == null)
            return "error";
        try {
            final ByteArrayInputStream bytesIn = new ByteArrayInputStream(decodedBytes);
            final InflaterInputStream inflater = new InflaterInputStream(bytesIn, new Inflater(true));
            return this.toString(inflater);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String toString(final InputStream stream) {
        try {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            final Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) out.append(buffer, 0, numRead);
            return out.toString();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
