package barkbot.client;

import barkbot.model.Mention;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class PostToGroupMeClient {
    private static final String POST_URL = "https://api.groupme.com/v3/bots/post";

    @NonNull
    private final HttpClient client;
    @NonNull
    private final Supplier<String> botIdSupplier;

    public void call(@NonNull final String text, @NonNull final Mention mention) {
        log.info("text={} mention={}", text, mention);
        final HttpPost post = new HttpPost(POST_URL);

        try {
            post.setEntity(
                    new UrlEncodedFormEntity(
                            Lists.newArrayList(
                                    new BasicNameValuePair("bot_id", botIdSupplier.get()),
                                    new BasicNameValuePair("text", text),
                                    new BasicNameValuePair("attachments", mention.toAttachmentJson()))));

        } catch (final UnsupportedEncodingException e) {
            log.error("bug in JVM; buy lottery tickets", e);
            throw new AssertionError("bug in JVM; buy lottery tickets");
        }

        safeCall(post);
    }

    private void safeCall(final HttpPost post) {
        try {
            client.execute(post);

        } catch (final IOException e) {
            log.error("error in connection", e);
            throw new UncheckedIOException("error in connection", e);
        }
    }
}
