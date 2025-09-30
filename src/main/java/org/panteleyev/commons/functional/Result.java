/*
 Copyright © 2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.functional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>A discriminated union that encapsulates either successful result with a value or failure with throwable.</p>
 *
 * <h2>Creating Result</h2>
 *
 * <p>There are three methods to create result:</p>
 * <ul>
 *     <li>{@link #success}</li>
 *     <li>{@link #empty()} - convenience method for {@code Result<Void>}</li>
 *     <li>{@link #failure}</li>
 * </ul>
 *
 * <p>As Java does not have non-nullable types {@code null} can be supplied and received in all methods as a valid
 * result value. If non-nullable type must be represented one can use {@code Result<Optional<T>>} although it is
 * considered a bad practice to use {@link Optional} this way.</p>
 *
 * <p>Direct instantiation of {@link Success} or {@link Failure} is also possible though not recommended.</p>
 *
 * <p><strong>Examples:</strong></p>
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
 *
 * <h2>Handling Result</h2>
 *
 * <h3>Chainable Methods</h3>
 * <ul>
 *     <li>{@link #onSuccess}</li>
 *     <li>{@link #onFailure}</li>
 *     <li>{@link #getOrThrow()}</li>
 *     <li>{@link #throwIfFailure()}</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * // Do some actions with either value or throwable:
 * //
 * getResult().onSuccess(value -> {
 *     // process value
 * }).onFailure(throwable -> {
 *     // process throwable
 * });
 *
 * // Do some actions with either value or throwable then get value or throw {@link RuntimeException}:
 * //
 * var value = getResult().onSuccess(value -> {
 *     // process value
 * }).onFailure(throwable -> {
 *     // process throwable
 * }).getOrThrow();
 * }</pre>
 *
 * <h3>Pattern Matching</h3>
 *
 * <p>Result is represented by either {@link Success} or {@link Failure} instances which can be used in
 * record pattern matching:</p>
 * <pre>
 * {@code
 * public String buildString() {
 *      var result = doSomething();
 *      return switch (result) {
 *          case Success<String>(String value) -> value;
 *          case Failure<?>(Throwable throwable) -> "";
 *      }
 * }
 * }
 * </pre>
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
     * If throwable is not {@link RuntimeException} then {@code RuntimeException(throwable)} is thrown.
     *
     * @return value
     */
    default T getOrThrow() {
        return switch (this) {
            case Success<T>(T value) -> value;
            case Failure<?>(Throwable throwable) -> throw toRuntimeException(throwable);
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
     * <p>Throws throwable if failure. If throwable is not {@link RuntimeException} then
     * {@code RuntimeException(throwable)} is thrown.</p>
     *
     * <p>This method should be used instead of {@link #getOrThrow} if result value is {@link Void} or is not
     * relevant. It can be also used as chained method for {@link #onSuccess}} or {@link #onFailure}.</p>
     *
     * <p><strong>Examples:</strong></p>
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
     *      .onFailure(throwable -> {
     *          // log throwable
     *          // can't throw arbitrary throwable from lambda
     *      }
     *      .throwIfFailure();
     * }</pre>
     */
    default void throwIfFailure() {
        if (this instanceof Failure<?>(Throwable throwable)) {
            throw toRuntimeException(throwable);
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
     * Calls consumer with throwable in case of failure.
     *
     * @param consumer throwable consumer
     * @return this instance
     */
    default Result<T> onFailure(Consumer<Throwable> consumer) {
        if (this instanceof Failure<?>(Throwable throwable)) {
            consumer.accept(throwable);
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
     * @param throwable cause of failure
     * @param <T>       value type
     * @return result
     * @throws NullPointerException if throwable is null
     */
    static <T> Result<T> failure(Throwable throwable) {
        return new Failure<>(Objects.requireNonNull(throwable, "Failure throwable must not be null"));
    }

    private static RuntimeException toRuntimeException(Throwable throwable) {
        return throwable instanceof RuntimeException runtimeException ?
                runtimeException : new RuntimeException(throwable);
    }
}




