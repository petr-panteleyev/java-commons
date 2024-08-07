/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implements convenience wrapper for {@link XMLEvent} instances.
 *
 * @param event wrapped instance
 */
public record XMLEventWrapper(XMLEvent event) {
    /**
     * Calls handler if event is {@link javax.xml.stream.events.StartElement}.
     *
     * @param name    element name
     * @param handler element handler
     */
    public void ifStartElement(QName name, Consumer<StartElementWrapper> handler) {
        if (!event.isStartElement()) {
            return;
        }

        var startElement = event.asStartElement();
        if (startElement.getName().equals(name)) {
            handler.accept(new StartElementWrapper(startElement));
        }
    }

    /**
     * Returns {@link StartElementWrapper} if {@link StartElement#isStartElement()} is true and name matches.
     *
     * @param name element name
     * @return start element wrapper or empty if conditions are not met
     */
    public Optional<StartElementWrapper> asStartElement(QName name) {
        if (!event.isStartElement()) {
            return Optional.empty();
        }

        var startElement = event.asStartElement();
        return startElement.getName().equals(name) ? Optional.of(new StartElementWrapper(startElement)) : Optional.empty();
    }

    /**
     * Checks if event is {@link javax.xml.stream.events.EndElement}.
     *
     * @param name element name
     * @return true if condition is met
     */
    public boolean isEndElement(QName name) {
        return event.isEndElement() && event.asEndElement().getName().equals(name);
    }
}
