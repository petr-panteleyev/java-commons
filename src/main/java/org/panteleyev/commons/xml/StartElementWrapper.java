/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.util.Optional;

import static org.panteleyev.commons.xml.Converter.stringToValue;

/**
 * Implements convenience wrapper for {@link StartElement} instances.
 */
public class StartElementWrapper {
    private final StartElement startElement;
    private final XMLEventReaderWrapper wrapper;

    StartElementWrapper(StartElement startElement, XMLEventReaderWrapper wrapper) {
        this.startElement = startElement;
        this.wrapper = wrapper;
    }

    /**
     * Returns name of the wrapped element.
     *
     * @return name
     */
    public QName getName() {
        return startElement.getName();
    }

    /**
     * Returns attribute value as string. This is the fastest way of getting attribute value as there is no attempt to
     * parse the raw value from {@link Attribute#getValue()}.
     *
     * @param name attribute name
     * @return attribute value or null if attribute does not exist
     */
    public Optional<String> getAttributeValue(QName name) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? Optional.empty() : Optional.of(attribute.getValue());
    }

    /**
     * Returns attribute value of specified class.
     *
     * @param name attribute name
     * @param type value class
     * @param <T>  value type
     * @return value as instance of class
     */
    public <T> Optional<T> getAttributeValue(QName name, Class<T> type) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ?
                Optional.empty() :
                Optional.of(stringToValue(type, attribute.getValue(), wrapper.isLocalDateAsEpochDay()));
    }

    /**
     * Returns attribute value of specified class or default value.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttributeValue(QName name, T defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ?
                defaultValue :
                (T) stringToValue(defaultValue.getClass(), attribute.getValue(), wrapper.isLocalDateAsEpochDay());
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
     * Returns attribute value as long.
     *
     * @param name         attribute name
     * @param defaultValue attribute default value
     * @return attribute value or default value if attribute does not exist
     */
    public long getAttributeValue(QName name, long defaultValue) {
        var attribute = startElement.getAttributeByName(name);
        return attribute == null ? defaultValue : Long.parseLong(attribute.getValue());
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
}
