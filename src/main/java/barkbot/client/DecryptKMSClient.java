package barkbot.client;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class DecryptKMSClient {
    static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    @NonNull
    private final AWSKMS awskms;

    public String decrypt(final String secret) {
        final DecryptRequest request = new DecryptRequest()
                .withCiphertextBlob(stringToByteBuffer(secret));
        return byteBufferToString(awskms.decrypt(request).getPlaintext());
    }

    private ByteBuffer stringToByteBuffer(final String string) {
        return ByteBuffer.wrap(string.getBytes(CHARSET));
    }

    private String byteBufferToString(final ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return new String(byteBuffer.array(), CHARSET);
        }

        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return new String(bytes, CHARSET);
    }
}
