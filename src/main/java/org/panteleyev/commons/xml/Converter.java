/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

final class Converter {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private static final String TYPE_STRING = "java.lang.String";
    private static final String TYPE_BIG_DECIMAL = "java.math.BigDecimal";
    private static final String TYPE_BOOLEAN = "java.lang.Boolean";
    private static final String TYPE_BOOL = "boolean";
    private static final String TYPE_INTEGER = "java.lang.Integer";
    private static final String TYPE_INT = "int";
    private static final String TYPE_LONG = "java.lang.Long";
    private static final String TYPE_LONG_P = "long";
    private static final String TYPE_DOUBLE = "java.lang.Double";
    private static final String TYPE_DOUBLE_P = "double";
    private static final String TYPE_UUID = "java.util.UUID";
    private static final String TYPE_LOCAL_DATE = "java.time.LocalDate";
    private static final String TYPE_LOCAL_DATE_TIME = "java.time.LocalDateTime";
    private static final String TYPE_BYTE_ARRAY = "byte[]";

    static String valueToString(Object value, boolean localDateAsEpochDay) {
        Objects.requireNonNull(value);

        if (value.getClass().getTypeName().equals(TYPE_BYTE_ARRAY)) {
            return Base64.getEncoder().encodeToString((byte[]) value);
        } else {
            return switch (value) {
                case String stringValue -> stringValue;
                case LocalDateTime localDateTime -> localDateTime.format(DATE_TIME_FORMATTER);
                case Enum<?> enumValue -> enumValue.name();
                case LocalDate localDate when localDateAsEpochDay -> Long.toString(localDate.toEpochDay());
                case LocalDate localDate -> localDate.format(DATE_FORMATTER);
                default -> value.toString();
            };
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T> T stringToValue(Class<T> type, String stringValue, boolean localDateAsEpochDay) {
        Objects.requireNonNull(stringValue);

        if (type.isEnum()) {
            return (T) Enum.valueOf((Class) type, stringValue);
        } else {
            return (T) switch (type.getTypeName()) {
                case TYPE_STRING -> stringValue;
                case TYPE_INT -> Integer.parseInt(stringValue);
                case TYPE_INTEGER -> Integer.valueOf(stringValue);
                case TYPE_LONG_P -> Long.parseLong(stringValue);
                case TYPE_LONG -> Long.valueOf(stringValue);
                case TYPE_BOOL -> Boolean.parseBoolean(stringValue);
                case TYPE_BOOLEAN -> Boolean.valueOf(stringValue);
                case TYPE_DOUBLE -> Double.valueOf(stringValue);
                case TYPE_DOUBLE_P -> Double.parseDouble(stringValue);
                case TYPE_BIG_DECIMAL -> new BigDecimal(stringValue);
                case TYPE_UUID -> UUID.fromString(stringValue);
                case TYPE_LOCAL_DATE -> localDateAsEpochDay ?
                        LocalDate.ofEpochDay(Long.parseLong(stringValue)) :
                        LocalDate.parse(stringValue, DATE_FORMATTER);
                case TYPE_LOCAL_DATE_TIME -> LocalDateTime.parse(stringValue, DATE_TIME_FORMATTER);
                case TYPE_BYTE_ARRAY -> Base64.getDecoder().decode(stringValue);
                default -> throw new IllegalArgumentException("Unsupported type: " + type.getTypeName());
            };
        }
    }

    private Converter() {
    }
}
