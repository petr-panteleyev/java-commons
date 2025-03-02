/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

/**
 * This class represents successful result with value of type {@code T}.
 *
 * @param value result value
 * @param <T>   result type
 */
public record Success<T>(T value) implements Result<T> {
}
