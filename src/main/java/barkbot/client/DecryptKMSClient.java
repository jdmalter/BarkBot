package barkbot.client;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class DecryptKMSClient {
    @NonNull
    private final AWSKMS awskms;

    public String decrypt(final String secret) {
        final DecryptRequest request = new DecryptRequest()
                .withCiphertextBlob(stringToByteBuffer(secret));
        return byteBufferToString(awskms.decrypt(request).getPlaintext());
    }

    private ByteBuffer stringToByteBuffer(final String string) {
        return ByteBuffer.wrap(string.getBytes());
    }

    private String byteBufferToString(final ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return new String(byteBuffer.array());
        }

        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return new String(bytes);
    }
}
