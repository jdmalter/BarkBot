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
        final String body = String.format(
                "{\"bot_id\":\"%s\",\"text\":\"%s\",\"attachments\":[%s]}",
                botId,
                text,
                String.format("{\"type\":\"mentions\",\"user_ids\":[%s,%s],\"loci\":[[%d,%d],[%d,%d]]}",
                        offender.getUserId(),
                        notified.getUserId(),
                        offender.getOffset(),
                        offender.getLength(),
                        notified.getOffset(),
                        notified.getLength()));
        log.info("body={}", body);

        try {
            post.addHeader("content-type", "application/json");
            post.setEntity(new StringEntity(body));

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
