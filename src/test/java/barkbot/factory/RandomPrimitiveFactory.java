package barkbot.factory;

import java.util.Random;

public class RandomPrimitiveFactory {
    private static final Random RANDOM = new Random();
    private static final int MAXIMUM_BYTES_IN_STRING = 16;

    private RandomPrimitiveFactory() {
        throw new UnsupportedOperationException();
    }

    public static boolean createBoolean() {
        return RANDOM.nextBoolean();
    }

    public static float createFloat() {
        return RANDOM.nextFloat();
    }

    public static int createInt(final int bound) {
        return RANDOM.nextInt(bound);
    }

    public static long createLong() {
        return RANDOM.nextLong();
    }

    public static long createLong(final int bound) {
        return RANDOM.nextInt(bound);
    }

    public static String createString() {
        final byte[] bytes = new byte[RANDOM.nextInt(MAXIMUM_BYTES_IN_STRING)];
        RANDOM.nextBytes(bytes);
        return new String(bytes);
    }
}
