package barkbot.factory;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomPrimitiveFactory {
    private static final Random RANDOM = new Random();
    private static final int MAXIMUM_CHARACTERS = 16;

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
        return RandomStringUtils.randomAscii(RANDOM.nextInt(MAXIMUM_CHARACTERS));
    }
}
