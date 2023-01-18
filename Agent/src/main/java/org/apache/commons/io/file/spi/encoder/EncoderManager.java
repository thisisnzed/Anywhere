package org.apache.commons.io.file.spi.encoder;

import org.apache.commons.io.file.spi.encoder.impl.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class EncoderManager {

    private final SimpleNameEncoder simpleNameEncoder;
    private final XOREncoder xorEncoder;
    private final ROT47Encoder rot47Encoder;
    private final Base64Encoder base64Encoder;
    private final CompressedBase64Encoder compressedBase64Encoder;

    public EncoderManager() {
        this.simpleNameEncoder = new SimpleNameEncoder();
        this.xorEncoder = new XOREncoder();
        this.rot47Encoder = new ROT47Encoder();
        this.base64Encoder = new Base64Encoder();
        this.compressedBase64Encoder = new CompressedBase64Encoder();
    }

    public String encode(final String str) {
        return new String(this.searchAt(null, str, SimpleNameEncoder.class, Base64Encoder.class, XOREncoder.class, ROT47Encoder.class, CompressedBase64Encoder.class/*, Base64Encoder.class*/).getBytes(StandardCharsets.UTF_8));
    }

    public String encode(final String str, final String key) {
        return new String(this.searchAt(key, str, SimpleNameEncoder.class, Base64Encoder.class, XOREncoder.class, ROT47Encoder.class, CompressedBase64Encoder.class/*, Base64Encoder.class*/).getBytes(StandardCharsets.UTF_8));
    }

    private String searchAt(final String key, final String str, final Class<?>... c) {
        final AtomicReference<String> encoded = new AtomicReference<>(str);
        Arrays.stream(c).forEach(clazz -> {
            switch (clazz.getSimpleName()) {
                case "SimpleNameEncoder":
                    encoded.set(this.simpleNameEncoder.exec(encoded.get()));
                    break;
                case "Base64Encoder":
                    encoded.set(this.base64Encoder.exec(encoded.get()));
                    break;
                case "XOREncoder":
                    encoded.set(this.xorEncoder.exec(encoded.get(), key == null ? "TBtf9AujY3qYsvQ4MWKNJ3ELWD4qcPyxbm47q2NC8GBPmczAdq7L6YkqeSWC6MhFt9S4ubHQs9Ckrceyx5XfFhZ77478gV3Z" : key));
                    break;
                case "ROT47Encoder":
                    encoded.set(this.rot47Encoder.exec(encoded.get()));
                    break;
                case "CompressedBase64Encoder":
                    encoded.set(this.compressedBase64Encoder.exec(encoded.get()));
                    break;
                default:
                    break;
            }
        });
        return encoded.get();
    }
}
