/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.password;

import java.util.Random;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;

/**
 * Defines password character sets.
 */
public enum PasswordCharacterSet {
    /**
     * Upper case letters.
     * <p><code>A B C D E F G H I J K L M N O P Q R S T U V W X Y Z</code></p>
     */
    UPPER_CASE_LETTERS(new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    }, new char[]{'I', 'O'}),

    /**
     * Lower case letters.
     * <p><code>a b c d e f g h i j k l m n o p q r s t u v w x y z</code></p>
     */
    LOWER_CASE_LETTERS(new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    }, new char[]{'l'}),

    /**
     * Digits.
     * <p><code>0 1 2 3 4 5 6 7 8 9</code></p>
     */
    DIGITS(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}),

    /**
     * Symbols.
     * <p><code>@ # $ % &amp; * ( ) - + = ^ . ,</code></p>
     */
    SYMBOLS(new char[]{'@', '#', '$', '%', '&', '*', '(', ')', '-', '+', '=', '^', '.', ','});

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private final char[] characters;
    private final char[] ambiguousCharacters;

    PasswordCharacterSet(char[] characters, char[] ambiguousCharacters) {
        this.characters = characters;
        sort(this.characters);
        this.ambiguousCharacters = ambiguousCharacters;
        sort(this.ambiguousCharacters);
    }

    PasswordCharacterSet(char[] characters) {
        this(characters, new char[0]);
    }

    char nextCharacter(boolean allowAmbiguousLetters) {
        while (true) {
            var sym = characters[RANDOM.nextInt(characters.length)];
            if (allowAmbiguousLetters || !isAmbiguous(sym)) {
                return sym;
            }
        }
    }

    char[] getCharacters() {
        return characters;
    }

    private boolean isAmbiguous(char c) {
        return ambiguousCharacters.length > 0 && binarySearch(ambiguousCharacters, c) >= 0;
    }

    boolean hasCharacter(char[] password) {
        for (var c : password) {
            if (binarySearch(characters, c) >= 0) {
                return true;
            }
        }
        return false;
    }
}
