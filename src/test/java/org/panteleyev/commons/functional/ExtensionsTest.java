/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.panteleyev.commons.functional.Extensions.apply;

public class ExtensionsTest {
    @Test
    public void testApply() {
        var counter = new AtomicInteger(0);

        Consumer<AtomicInteger> consumer = c -> Objects.requireNonNull(c).incrementAndGet();

        var inc = apply(counter, consumer);
        assertEquals(1, inc.get());

        assertNull(apply(null, consumer));
    }
}
