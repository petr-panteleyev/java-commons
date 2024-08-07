/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import java.util.UUID;

/**
 * Implements convenience wrapper for {@link StartElement} instances.
 *
 * @param startElement wrapped instance
 */
public record StartElementWrapper(StartElement startElement) {
    /**
     * Returns attribute value.
     *
     * @param name attribute name
     * @return attribute value or null if attribute does not exist
     */
    public String getAttributeValue(QName name) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? null : attribute.getValue();
    }

    /**
     * Returns attribute value.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public String getAttributeValue(QName name, String defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : attribute.getValue();
    }

    /**
     * Returns attribute value as int.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public int getAttributeValue(QName name, int defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : Integer.parseInt(attribute.getValue());
    }

    /**
     * Returns attribute value as double.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public double getAttributeValue(QName name, double defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : Double.parseDouble(attribute.getValue());
    }

    /**
     * Returns attribute value as boolean.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public boolean getAttributeValue(QName name, boolean defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : Boolean.parseBoolean(attribute.getValue());
    }

    /**
     * Returns attribute value as {@link UUID}.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public UUID getAttributeValue(QName name, UUID defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : UUID.fromString(attribute.getValue());
    }
}
