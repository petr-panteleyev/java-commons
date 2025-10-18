/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This class provides general purpose extension methods.
 */
public final class Extensions {
    private Extensions() {
    }

    /**
     * Calls consumer with value if value is not {@code null}, otherwise does nothing.
     *
     * @param value    value
     * @param consumer consumer
     * @param <T>      value type
     * @return value
     * @throws NullPointerException if consumer is null
     */
    public static <T> T apply(T value, Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "Consumer cannot be null");
        if (value != null) {
            consumer.accept(value);
        }
        return value;
    }
}
