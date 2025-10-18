/*
 Copyright © 2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.crypto;

/**
 * Wrapper for crypto related exceptions.
 */
public class CryptographyException extends RuntimeException {
    /**
     * Creates an instance of CryptoException with the cause.
     * @param cause the cause.
     */
    public CryptographyException(Throwable cause) {
        super(cause);
    }
}
