/*
 Copyright © 2020-2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.crypto;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * This interface provides methods for AES encryption and decryption.
 */
public interface AES {
    /**
     * Encrypt string.
     *
     * @param str      string
     * @param password password
     * @return encrypted bytes
     * @throws CryptographyException if cryptography related error occurs
     */
    default byte[] encrypt(String str, String password) {
        return encrypt(str.getBytes(StandardCharsets.UTF_8), password);
    }

    /**
     * Encrypt byte array.
     *
     * @param src      byte array
     * @param password password
     * @return encrypted bytes
     * @throws CryptographyException if cryptography related error occurs
     */
    byte[] encrypt(byte[] src, String password);

    /**
     * Encrypt bytes and write the result into the stream.
     *
     * @param src      bytes to encrypt
     * @param password password
     * @param out      stream
     * @throws CryptographyException if cryptography related error occurs
     * @throws UncheckedIOException  if an I/O error occurs
     */
    void encrypt(byte[] src, String password, OutputStream out);

    /**
     * Encrypt string and write the result into the stream.
     *
     * @param str      string to encrypt
     * @param password password
     * @param out      stream
     * @throws CryptographyException if cryptography related error occurs
     * @throws UncheckedIOException  if an I/O error occurs.
     */
    default void encrypt(String str, String password, OutputStream out) {
        encrypt(str.getBytes(StandardCharsets.UTF_8), password, out);
    }

    /**
     * Decrypt byte array.
     *
     * @param bytes    bytes to decrypt
     * @param password password to decrypt
     * @return decrypted bytes
     * @throws CryptographyException if cryptography related error occurs
     */
    byte[] decrypt(byte[] bytes, String password);

    /**
     * Decrypt string
     *
     * @param bytes    bytes to decrypt
     * @param password password to decrypt
     * @return decrypted bytes
     * @throws CryptographyException if cryptography related error occurs
     */
    String decryptString(byte[] bytes, String password);

    /**
     * Decrypt input stream. Stream must be obtained via {@link #getInputStream(InputStream, String)}.
     *
     * @param in       stream to decrypt
     * @param password password to decrypt
     * @return decrypted bytes
     * @throws CryptographyException if cryptography related error occurs
     * @throws UncheckedIOException  if an I/O error occurs.
     */
    byte[] decrypt(InputStream in, String password);

    /**
     * Returns input stream to decrypt encrypted stream with password.
     *
     * @param in       encrypted stream
     * @param password password
     * @return instance of {@link CipherInputStream}
     * @throws CryptographyException if cryptography related error occurs
     * @throws UncheckedIOException  if an I/O error occurs
     */
    InputStream getInputStream(InputStream in, String password);

    /**
     * Returns output stream to encrypt given output stream.
     *
     * @param out      output stream
     * @param password password
     * @return instance of {@link CipherOutputStream}
     * @throws CryptographyException if cryptography related error occurs
     * @throws UncheckedIOException  if an I/O error occurs.
     */
    OutputStream getOutputStream(OutputStream out, String password);

    /**
     * Default 256-bit key generator. This implementation uses SHA-256 message
     * digest algorithm.
     *
     * @param password password string
     * @return key bytes
     * @throws CryptographyException if cryptography related error occurs
     */
    static byte[] generate256key(String password) {
        try {
            return AESImpl.generateKey(password, "SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptographyException(ex);
        }
    }

    /**
     * Return AES instance with specified key generation function.
     *
     * @param keyGen key generation function
     * @return AES instance
     * @throws NullPointerException if keyGen function is null
     */
    static AES aes(Function<String, byte[]> keyGen) {
        return AESImpl.getInstance(keyGen);
    }

    /**
     * Return AES instance with default 256-bit key generation function.
     * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
     * for the appropriate JRE must be installed to use 256-bit keys.
     *
     * @return AES instance
     * @throws CryptographyException if cryptography related error occurs
     */
    static AES aes256() {
        return aes(AES::generate256key);
    }
}
