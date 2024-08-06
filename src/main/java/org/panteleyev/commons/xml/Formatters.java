/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import java.time.format.DateTimeFormatter;

final class Formatters {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private Formatters() {
    }
}
