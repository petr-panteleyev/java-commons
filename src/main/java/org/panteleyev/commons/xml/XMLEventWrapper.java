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
 */
public class XMLEventWrapper {
    private final XMLEvent event;
    private final XMLEventReaderWrapper wrapper;

    XMLEventWrapper(XMLEvent event, XMLEventReaderWrapper wrapper) {
        this.event = event;
        this.wrapper = wrapper;
    }

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
            handler.accept(new StartElementWrapper(startElement, wrapper));
        }
    }

    /**
     * Returns {@link StartElementWrapper} if {@link StartElement#isStartElement()} is true and name matches.
     *
     * @param name element name
     * @return start element wrapper
     */
    public Optional<StartElementWrapper> asStartElement(QName name) {
        if (!event.isStartElement()) {
            return Optional.empty();
        }

        var startElement = event.asStartElement();
        return startElement.getName().equals(name) ?
                Optional.of(new StartElementWrapper(startElement, wrapper)) : Optional.empty();
    }

    /**
     * Returns {@link StartElementWrapper} if {@link StartElement#isStartElement()} is true.
     *
     * @return start element wrapper
     */
    public Optional<StartElementWrapper> asStartElement() {
        return event.isStartElement() ?
                Optional.of(new StartElementWrapper(event.asStartElement(), wrapper)) : Optional.empty();
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
