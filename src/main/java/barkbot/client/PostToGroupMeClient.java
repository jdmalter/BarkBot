package barkbot.client;

import barkbot.model.Mention;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
public class PostToGroupMeClient {
    private static final String POST_URL = "https://api.groupme.com/v3/bots/post";

    @NonNull
    private final HttpClient client;
    @NonNull
    private final String botId;

    public void call(@NonNull final String text, @NonNull final Mention offender, @NonNull final Mention notified) {
        log.info("text={} offender={} notified={}", text, offender, notified);
        final HttpPost post = new HttpPost(POST_URL);
        final String string = String.format(
                "{\"bot_id\":\"%s\",\"text\":\"%s\",\"attachments\":[%s,%s]}",
                botId,
                text,
                offender.toAttachmentJson(),
                notified.toAttachmentJson());

        try {
            post.addHeader("content-type", "application/json");
            post.setEntity(new StringEntity(string));

        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError("bug in JVM; buy lottery tickets");
        }

        safeCall(post);
    }

    private void safeCall(final HttpPost post) {
        try {
            client.execute(post);

        } catch (final IOException e) {
            throw new UncheckedIOException("error in connection", e);
        }
    }
}
