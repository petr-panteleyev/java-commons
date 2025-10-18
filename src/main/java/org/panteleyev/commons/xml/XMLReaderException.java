/*
 Copyright © 2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

/**
 * Wrapper for XML reader exceptions.
 */
public class XMLReaderException extends XMLException {
    /**
     * Creates a new XMLReaderException instance with the cause.
     *
     * @param cause the cause
     */
    public XMLReaderException(Throwable cause) {
        super(cause);
    }
}
