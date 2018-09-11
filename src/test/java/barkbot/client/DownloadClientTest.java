package barkbot.client;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

class DownloadClientTest {
    private DownloadClient subject;

    @BeforeEach
    void setUp() {
        subject = new DownloadClient();
    }

    @Test
    void successfulConvert() {
        final URL url = getClass().getClassLoader().getResource("funny-dog-thoughts-tweets.jpg");
        Preconditions.checkNotNull(url);
        final String string = url.getProtocol() + ":" + url.getPath();

        Assertions.assertNotNull(subject.convert(string));
    }

    @Test
    void badUrl() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> subject.convert(""));
    }
}