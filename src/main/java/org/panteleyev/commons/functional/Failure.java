/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

/**
 * This class represents failed result with exception.
 *
 * @param exception result exception
 * @param <T>       result type
 */
public record Failure<T>(Exception exception) implements Result<T> {
}