/*
 Copyright © 2017-2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.password;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Implements password generation functionality.
 * <p>
 *     The following characters are considered ambiguous from visual standpoint: <code>I l O</code>. They are excluded
 *     by default and can be included with {@link #generate(Set, int, boolean)}.
 * </p>
 * <p>
 *     Password length must be greater than 4 in order to ensure all specified character sets can be present in the
 *     generated string. There is no upper limit for password length.
 * </p>
 * <p>
 *     Instances of this class are thread safe and can be reused.
 * </p>
 */
public final class PasswordGenerator {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Generates password and avoids ambiguous letters.
     *
     * @param characterSets password character sets
     * @param length        password length
     * @return password
     * @throws IllegalArgumentException if no character sets are selected or password length &lt; 4
     */
    public String generate(Set<PasswordCharacterSet> characterSets, int length) {
        return generate(characterSets, length, false);
    }

    /**
     * Generates password.
     *
     * @param characterSets         password character sets
     * @param length                password length
     * @param allowAmbiguousLetters true if set of ambiguous letters should be included
     * @return password
     * @throws IllegalArgumentException if no character sets are selected or password length &lt; 4
     */
    public String generate(Set<PasswordCharacterSet> characterSets, int length, boolean allowAmbiguousLetters) {
        if (characterSets.isEmpty()) {
            throw new IllegalArgumentException("At least one character set must be used");
        }
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4");
        }

        var characterSetList = new ArrayList<>(characterSets);

        var password = new char[length];
        while (true) {
            for (var i = 0; i < length; i++) {
                var characterSet = characterSetList.get(RANDOM.nextInt(characterSetList.size()));
                password[i] = characterSet.nextCharacter(allowAmbiguousLetters);
            }

            if (characterSetList.stream().allMatch(characterSet -> characterSet.hasCharacter(password))) {
                return new String(password);
            }
        }
    }
}
