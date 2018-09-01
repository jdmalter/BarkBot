package barkbot.client;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
public class PostToGroupMeClient {
    private static final String POST_URL = "https://api.groupme.com/v3/bots/post";

    @NonNull
    private final HttpClient client;
    @NonNull
    private final String botId;

    public void call(@NonNull final String text) {
        final HttpPost post = new HttpPost(POST_URL);
        try {
            post.setEntity(
                    new UrlEncodedFormEntity(
                            Lists.newArrayList(
                                    new BasicNameValuePair("bot_id", botId),
                                    new BasicNameValuePair("text", text))));
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
