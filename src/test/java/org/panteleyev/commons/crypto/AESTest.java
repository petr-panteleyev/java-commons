/*
 Copyright © 2020-2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.crypto;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AESTest {
    private static final String PASSWORD = randomString();
    private static final String TEXT = "This is 1st line of text\nSecond line\nThird line";

    @Test
    public void testAES256EncryptDecrypt() {
        var aes = AES.aes256();
        var enc = aes.encrypt(TEXT, PASSWORD);
        var dec = aes.decrypt(enc, PASSWORD);
        assertEquals(TEXT, new String(dec, StandardCharsets.UTF_8), "Decoded text is the same as original");
        assertEquals(TEXT, aes.decryptString(enc, PASSWORD), "Decoded text is the same as original");
    }

    @Test
    public void testAESInputStreamDecrypt() throws Exception {
        var aes = AES.aes256();
        var enc = aes.encrypt(TEXT, PASSWORD);

        try (var in = new ByteArrayInputStream(enc)) {
            var dec = aes.decrypt(in, PASSWORD);
            assertEquals(TEXT, new String(dec, StandardCharsets.UTF_8), "Decoded text is the same as original");
        }
    }

    @Test
    public void testAESOutputStreamEncrypt() throws Exception {
        var aes = AES.aes256();

        try (var out = new ByteArrayOutputStream()) {
            aes.encrypt(TEXT, PASSWORD, out);

            var decrypted = aes.decryptString(out.toByteArray(), PASSWORD);
            assertEquals(TEXT, decrypted);
        }
    }

    @Test
    public void testNewInstance() {
        var aes256_1 = AES.aes256();
        var aes256_2 = AES.aes256();
        assertEquals(aes256_1, aes256_2);
    }

    @Test
    public void testKeyGen() {
        var key256 = AES.generate256key(randomString());
        assertEquals(256 / 8, key256.length);
    }

    @Test
    public void testAESgetOutputStream() throws Exception {
        var aes256 = AES.aes256();

        try (var out = new ByteArrayOutputStream()) {
            try (var cout = aes256.getOutputStream(out, PASSWORD)) {
                cout.write(TEXT.getBytes(StandardCharsets.UTF_8));
            }

            var encrypted = out.toByteArray();
            var decrypted = aes256.decryptString(encrypted, PASSWORD);
            assertEquals(TEXT, decrypted);
        }
    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }
}
