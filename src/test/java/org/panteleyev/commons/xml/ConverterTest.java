/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    private static List<Arguments> testStringToValueArguments() {
        return List.of(
                Arguments.of(
                        "true", Boolean.class, true, false
                ),
                Arguments.of(
                        "string", String.class, "string", false
                ),
                Arguments.of(
                        "100", Integer.class, 100, false
                ),
                Arguments.of(
                        "100", Long.class, 100L, false
                ),
                Arguments.of(
                        "100", Double.class, 100.0, false
                ),
                Arguments.of(
                        "2024-12-11T01:02:03", LocalDateTime.class, LocalDateTime.of(2024, 12, 11, 1, 2, 3), false
                ),
                Arguments.of(
                        "2024-12-11", LocalDate.class, LocalDate.of(2024, 12, 11), false
                ),
                Arguments.of(
                        "20068", LocalDate.class, LocalDate.of(2024, 12, 11), true
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testStringToValueArguments")
    public void testStringToValue(String text, Class<?> type, Object expected, boolean localDateAsEpochDay) {
        var actual = Converter.stringToValue(type, text, localDateAsEpochDay);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testToAndFrom(boolean localDateAsEpochDay) {
        var values = List.of(
                100, 100L, BigDecimal.TEN, "string",
                LocalDateTime.of(2024, 12, 11, 1, 2, 3),
                LocalDate.of(2024, 12, 11),
                UUID.randomUUID(),
                new byte[]{1, 2, 3}
        );

        for (var originalValue : values) {
            var stringValue = Converter.valueToString(originalValue, localDateAsEpochDay);
            var restored = Converter.stringToValue(originalValue.getClass(), stringValue, localDateAsEpochDay);

            if (originalValue.getClass().isArray()) {
                assertArrayEquals((byte[]) originalValue, (byte[]) restored);
            } else {
                assertEquals(originalValue, restored);
            }
        }
    }
}
