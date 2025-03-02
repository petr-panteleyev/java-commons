/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResultTest {
    private static final int VALUE = 100;

    @Test
    public void testSuccess() {
        var result = Result.success(VALUE);

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());

        assertEquals(VALUE, result.getOrThrow());

        result.onSuccess(value -> assertEquals(VALUE, value))
                .onFailure(exception -> fail("onFailure should not be called"));

        result.onFailure(exception -> fail("onFailure should not be called"))
                .onSuccess(value -> assertEquals(VALUE, value));
    }

    @Test
    public void testOnFailure() {
        var result = Result.failure(new RuntimeException());

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());

        assertThrows(RuntimeException.class, result::getOrThrow);
        assertThrows(RuntimeException.class, result::throwIfFailure);

        result.onSuccess(value -> fail("onSuccess should not be called"))
                .onFailure(throwable -> assertInstanceOf(RuntimeException.class, throwable));

        result.onFailure(throwable -> assertInstanceOf(RuntimeException.class, throwable))
                .onSuccess(value -> fail("onSuccess should not be called"));
    }

    @Test
    public void testNpe() {
        assertDoesNotThrow(() -> Result.success(null));
        assertThrows(NullPointerException.class, () -> Result.failure(null));

        assertDoesNotThrow(() -> Result.empty());
    }
}
