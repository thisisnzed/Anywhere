package org.apache.commons.io.file.spi.decoder.impl;

public class ROT47Decoder {

    public String exec(final String str) {
        final int key = 47;
        final StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int temp = (int) str.charAt(i) - key;
            if ((int) str.charAt(i) == 32) decrypted.append(" ");
            else {
                if (temp < 32) temp += 94;
                decrypted.append((char) temp);
            }
        }
        return decrypted.toString();
    }
}
