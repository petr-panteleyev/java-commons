/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import java.time.LocalDate;

/**
 * Serialization options.
 */
public enum SerializationOption {
    /**
     * Exports {@link LocalDate} as {@link LocalDate#toEpochDay() epoch day}, otherwise as ISO_DATE.
     */
    LOCAL_DATE_AS_EPOCH_DAY
}
