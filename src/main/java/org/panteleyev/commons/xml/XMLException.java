/*
 Copyright © 2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

/**
 * Wrapper for all kinds of XML related exceptions.
 */
public class XMLException extends RuntimeException {
    /**
     * Creates a new XMLException instance with the cause.
     *
     * @param cause the cause
     */
    public XMLException(Throwable cause) {
        super(cause);
    }
}
