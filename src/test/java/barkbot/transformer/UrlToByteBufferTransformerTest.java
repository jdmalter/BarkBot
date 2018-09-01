package barkbot.transformer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

class UrlToByteBufferTransformerTest {
    private UrlToByteBufferTransformer subject;

    @BeforeEach
    void setUp() {
        subject = new UrlToByteBufferTransformer();
    }

    @Test
    void successfulConvert() {
        final URL url = getClass().getClassLoader().getResource("funny-dog-thoughts-tweets.jpg");
        final String string = url.getProtocol() + ":" + url.getPath();

        Assertions.assertNotNull(subject.convert(string));
    }

    @Test
    void badUrl() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> subject.convert(""));
    }
}