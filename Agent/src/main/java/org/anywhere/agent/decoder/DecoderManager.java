package org.anywhere.agent.decoder;

import org.anywhere.agent.decoder.impl.*;
import org.apache.commons.io.file.agent.decoder.impl.*;
import org.apache.commons.io.file.spi.decoder.impl.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class DecoderManager {

    private final SimpleNameDecoder simpleNameDecoder;
    private final XORDecoder xorDecoder;
    private final ROT47Decoder rot47Decoder;
    private final Base64Decoder base64Decoder;
    private final CompressedBase64Decoder compressedBase64Decoder;

    public DecoderManager() {
        this.simpleNameDecoder = new SimpleNameDecoder();
        this.xorDecoder = new XORDecoder();
        this.rot47Decoder = new ROT47Decoder();
        this.base64Decoder = new Base64Decoder();
        this.compressedBase64Decoder = new CompressedBase64Decoder();
    }

    public String decode(final String str) {
        return new String(this.searchAt(str, CompressedBase64Decoder.class, ROT47Decoder.class, XORDecoder.class, Base64Decoder.class, SimpleNameDecoder.class).getBytes(StandardCharsets.UTF_8));
    }

    private String searchAt(final String str, final Class<?>... c) {
        final AtomicReference<String> decoded = new AtomicReference<>(str);
        Arrays.stream(c).forEach(clazz -> {
            switch (clazz.getSimpleName()) {
                case "SimpleNameDecoder":
                    decoded.set(this.simpleNameDecoder.exec(decoded.get()));
                    break;
                case "Base64Decoder":
                    decoded.set(this.base64Decoder.exec(decoded.get()));
                    break;
                case "XORDecoder":
                    decoded.set(this.xorDecoder.exec(decoded.get(), "TBtf9AujY3qYsvQ4MWKNJ3ELWD4qcPyxbm47q2NC8GBPmczAdq7L6YkqeSWC6MhFt9S4ubHQs9Ckrceyx5XfFhZ77478gV3Z"));
                    break;
                case "ROT47Decoder":
                    decoded.set(this.rot47Decoder.exec(decoded.get()));
                    break;
                case "CompressedBase64Decoder":
                    decoded.set(this.compressedBase64Decoder.exec(decoded.get()));
                    break;
                default:
                    break;
            }
        });
        return decoded.get();
    }
}
