/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>A discriminated union that encapsulates either successful result with a value or failure with exception.</p>
 *
 * <h1>Creating Result</h1>
 *
 * <p>There are three methods to create result:
 * <ul>
 *     <li>{@link #success}</li>
 *     <li>{@link #empty()} - convenience method for {@code Result<Void>}</li>
 *     <li>{@link #failure}</li>
 * </ul>
 *
 * </p>
 *
 * <p>As Java does not have non-nullable types {@code null} can be supplied and received in all methods as a valid
 * result value. If non-nullable type must be represented one can use {@code Result<Optional<T>>} although it is
 * considered a bad practice to use {@link Optional} this way.</p>
 * <p>
 * Direct instantiation of {@link Success} or {@link Failure} is also possible though not recommended.
 *
 * <p><strong>Examples:</strong>
 * <pre>{@code
 * Result<Integer> calculateSomething() {
 *     try {
 *         return Result.success(123);
 *     } catch (Exception ex) {
 *         return Result.failure(ex);
 *     }
 * }
 *
 * Result<Void> doSomething() {
 *     try {
 *         return Result.empty();
 *     } catch (Exception ex) {
 *         return Result.failure(ex);
 *     }
 * }
 *
 * Result<Optional<Integer>> doSomething() {
 *     try {
 *         return Result.success(Optional.empty());
 *     } catch (Exception ex) {
 *         return Result.failure(ex);
 *     }
 * }
 * }</pre>
 * </p>
 *
 * <h1>Handling Result</h1>
 *
 * <h2>Chainable Methods</h2>
 * <p>
 * <ul>
 *     <li>{@link #onSuccess}</li>
 *     <li>{@link #onFailure}</li>
 *     <li>{@link #getOrThrow()}</li>
 *     <li>{@link #throwIfFailure()}</li>
 * </ul>
 * </p>
 *
 * <p><strong>Examples:</strong>
 * <pre>{@code
 * // Do some actions with either value or exception:
 * //
 * getResult().onSuccess(value -> {
 *     // process value
 * }).onFailure(exception -> {
 *     // process exception
 * });
 *
 * // Do some actions with either value or exception then get value or throw {@link RuntimeException}:
 * //
 * var value = getResult().onSuccess(value -> {
 *     // process value
 * }).onFailure(exception -> {
 *     // process exception
 * }).getOrThrow();
 * }</pre>
 * </p>
 *
 * <h2>Pattern Matching</h2>
 *
 * <p>Result is represented by either {@link Success} or {@link Failure} instances which can be used in
 * record pattern matching:
 * <pre>
 * {@code
 * public String buildString() {
 *      var result = doSomething();
 *      return switch (result) {
 *          case Success<String>(String value) -> value;
 *          case Failure<?>(Exception exception) -> "";
 *      }
 * }
 * }
 * </pre>
 * </p>
 *
 * @param <T> value type
 */
public sealed interface Result<T> permits Success, Failure {
    /**
     * Returns if this result is a success.
     *
     * @return success
     */
    default boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * Returns if this result is a failure.
     *
     * @return failure
     */
    default boolean isFailure() {
        return this instanceof Failure;
    }

    /**
     * Returns value if success or throws exception if failure.
     * If exception is not {@link RuntimeException} then {@code RuntimeException(exception)} is thrown.
     *
     * @return value
     */
    default T getOrThrow() {
        return switch (this) {
            case Success<T>(T value) -> value;
            case Failure<?>(Exception exception) -> throw toRuntimeException(exception);
        };
    }

    /**
     * <p>Returns optional value if success or empty optional if failure.</p>
     *
     * <p>In case of {@code null} value there is no way to determine if this was
     * success or failure. Use {@link #isSuccess()} or {@link #isFailure()} in this case.</p>
     *
     * @return optional value or empty
     */
    default Optional<T> get() {
        return this instanceof Success<T>(T value) ? Optional.ofNullable(value) : Optional.empty();
    }

    /**
     * <p>Throws exception if failure. If exception is not {@link RuntimeException} then
     * {@code RuntimeException(exception)} is thrown.</p>
     *
     * <p>This method should be used instead of {@link #getOrThrow} if result value is {@link Void} or is not
     * relevant. It can be also used as chained method for {@link #onSuccess}} or {@link #onFailure}.</p>
     *
     * <p><strong>Examples:</strong>
     * <pre>{@code
     * getResult()
     *      .onSuccess(value -> {
     *          // ...
     *       })
     *      .throwIfFailure();
     *
     * getResult()
     *      .onSuccess(value -> {
     *          // ...
     *      })
     *      .onFailure(exception -> {
     *          // log exception
     *          // can't throw arbitrary exception from lambda
     *      }
     *      .throwIfFailure();
     * }</pre>
     * </p>
     */
    default void throwIfFailure() {
        if (this instanceof Failure<?>(Exception exception)) {
            throw toRuntimeException(exception);
        }
    }

    /**
     * <p>Calls consumer with result value in case of success.</p>
     *
     * <p>If result was created by {@link #empty} then {@code null} value will be supplied to the consumer.</p>
     *
     * @param consumer value consumer
     * @return this instance
     */
    default Result<T> onSuccess(Consumer<T> consumer) {
        if (this instanceof Success<T>(T value)) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * Calls consumer with exception in case of failure.
     *
     * @param consumer exception consumer
     * @return this instance
     */
    default Result<T> onFailure(Consumer<Exception> consumer) {
        if (this instanceof Failure<?>(Exception exception)) {
            consumer.accept(exception);
        }
        return this;
    }

    /**
     * Creates a successful result.
     *
     * @param value result value
     * @param <T>   value type
     * @return result
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates an empty successful result. This is a convenience method for {@code Result<Void>}.
     *
     * @param <T> value type
     * @return result
     */
    static <T> Result<T> empty() {
        return success(null);
    }

    /**
     * Creates a failed result.
     *
     * @param exception cause of failure
     * @return result
     * @throws NullPointerException if exception is null
     */
    static <T> Result<T> failure(Exception exception) {
        return new Failure<>(Objects.requireNonNull(exception, "Failure exception must not be null"));
    }

    private static RuntimeException toRuntimeException(Exception exception) {
        return exception instanceof RuntimeException runtimeException ?
                runtimeException : new RuntimeException(exception);
    }
}




