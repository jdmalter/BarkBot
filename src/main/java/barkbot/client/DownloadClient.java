package barkbot.client;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

@Slf4j
public class DownloadClient {
    public ByteBuffer convert(@NonNull final String url) {
        log.info("string={}", url);

        try {
            return ByteBuffer.wrap(IOUtils.toByteArray(new URL(url)));

        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("bad url");

        } catch (final IOException e) {
            throw new UncheckedIOException("error in low-level I/O", e);
        }
    }
}
