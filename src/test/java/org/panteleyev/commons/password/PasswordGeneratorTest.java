/*
 Copyright © 2021-2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.password;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.binarySearch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordGeneratorTest {
    private static List<Arguments> testPasswordContentData() {
        return List.of(
                Arguments.of(Set.of(PasswordCharacterSet.UPPER_CASE_LETTERS)),
                Arguments.of(Set.of(PasswordCharacterSet.LOWER_CASE_LETTERS)),
                Arguments.of(Set.of(PasswordCharacterSet.DIGITS)),
                Arguments.of(Set.of(PasswordCharacterSet.SYMBOLS)),
                Arguments.of(Set.of(PasswordCharacterSet.values()))
        );
    }

    @Test
    public void testPasswordLength() {
        for (var length = 4; length <= 32; length++) {
            assertEquals(length,
                    new PasswordGenerator().generate(Set.of(PasswordCharacterSet.UPPER_CASE_LETTERS), length).length());
        }
    }

    @ParameterizedTest
    @MethodSource("testPasswordContentData")
    public void testPasswordContent(Set<PasswordCharacterSet> options) {
        var passwordCharacters = new PasswordGenerator().generate(options, 48).toCharArray();

        for (var opt : options) {
            assertTrue(hasCharacters(opt.getCharacters(), passwordCharacters));
        }
    }

    @Test
    public void testNoCharacterSetSelected() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordGenerator().generate(Set.of(), 8));
    }

    @Test
    public void testWrongLength() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordGenerator().generate(Set.of(PasswordCharacterSet.DIGITS), 3));
    }

    private static boolean hasCharacters(char[] characters, char[] password) {
        for (var c : password) {
            if (binarySearch(characters, c) >= 0) {
                return true;
            }
        }
        return false;
    }

}