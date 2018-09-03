package barkbot.client;

import barkbot.factory.RandomPrimitiveFactory;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;

@ExtendWith(MockitoExtension.class)
class DecryptKMSClientTest {
    private DecryptKMSClient subject;
    @Mock
    private AWSKMS awskms;

    @BeforeEach
    void setUp() {
        subject = new DecryptKMSClient(awskms);
    }

    @Test
    void successfulDecrypt() {
        final String expected = RandomPrimitiveFactory.createString();
        final String secret = RandomPrimitiveFactory.createString();
        final DecryptRequest request = new DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(secret.getBytes()));
        final DecryptResult result = new DecryptResult()
                .withPlaintext(ByteBuffer.wrap(expected.getBytes()));
        Mockito.when(awskms.decrypt(request)).thenReturn(result);

        final String actual = subject.decrypt(secret);

        Assertions.assertEquals(expected, actual);
    }
}