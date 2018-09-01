package barkbot.factory;

import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomCollectionFactory {
    private RandomCollectionFactory() {
        throw new UnsupportedOperationException();
    }

    public static <T> List<T> create(@NonNull final Supplier<T> supplier, final long size) {
        Preconditions.checkArgument(
                size >= 0,
                "size (%s) must be at least 0",
                size);
        return Stream.generate(supplier)
                .limit(size)
                .collect(Collectors.toList());
    }
}
