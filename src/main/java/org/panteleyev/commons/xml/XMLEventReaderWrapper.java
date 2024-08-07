/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.util.Optional;

/**
 * Implements convenience wrapper for {@link XMLEventReader} instances.
 */
public class XMLEventReaderWrapper implements AutoCloseable {
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    private final XMLEventReader reader;

    /**
     * Creates new instance of {@link XMLStreamWriterWrapper}.
     *
     * @param inputStream input stream
     * @return wrapper instance
     */
    public static XMLEventReaderWrapper newInstance(InputStream inputStream) {
        try {
            return new XMLEventReaderWrapper(INPUT_FACTORY.createXMLEventReader(inputStream));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private XMLEventReaderWrapper(XMLEventReader reader) {
        this.reader = reader;
    }

    /**
     * Returns wrapped reader instance.
     *
     * @return reader
     */
    public XMLEventReader getReader() {
        return reader;
    }

    /**
     * Closes the wrapped reader. See {@link XMLEventReader#close()}.
     */
    @Override
    public void close() {
        try {
            reader.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks if there is next event available. See {@link XMLEventReader#hasNext()}.
     *
     * @return true if there is next event available
     */
    public boolean hasNext() {
        return reader.hasNext();
    }

    /**
     * Returns wrapped next event. See {@link XMLEventReader#nextEvent()}.
     *
     * @return next event
     */
    public XMLEventWrapper nextEvent() {
        try {
            return new XMLEventWrapper(reader.nextEvent());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Check the next XMLEvent without reading it from the stream. See {@link XMLEventReader#peek()}.
     *
     * @return next event if available
     */
    public Optional<XMLEventWrapper> peek() {
        try {
            var event = reader.peek();
            return event == null ? Optional.empty() : Optional.of(new XMLEventWrapper(event));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reads the content of a text-only element. See {@link XMLEventReader#getElementText()}.
     *
     * @return element text
     */
    public Optional<String> getElementText() {
        try {
            return Optional.of(reader.getElementText());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
