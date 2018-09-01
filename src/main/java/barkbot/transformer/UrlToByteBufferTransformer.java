package barkbot.transformer;

import lombok.NonNull;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

public class UrlToByteBufferTransformer {
    public ByteBuffer convert(@NonNull final String string) {
        try {
            return ByteBuffer.wrap(IOUtils.toByteArray(new URL(string)));

        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("bad url");

        } catch (final IOException e) {
            throw new UncheckedIOException("error in low-level I/O", e);
        }
    }
}
