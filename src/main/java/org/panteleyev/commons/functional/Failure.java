/*
 Copyright © 2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

/**
 * This class represents failed result with throwable.
 *
 * @param throwable result throwable
 * @param <T>       result type
 */
public record Failure<T>(Throwable throwable) implements Result<T> {
}