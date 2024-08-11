/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import static org.panteleyev.commons.xml.Converter.stringToValue;

/**
 * Implements convenience wrapper for {@link XMLEventReader} instances.
 */
public class XMLEventReaderWrapper implements AutoCloseable {
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    private final XMLEventReader reader;
    private final boolean localDateAsEpochDay;

    /**
     * Creates new instance of {@link XMLStreamWriterWrapper} with default options.
     *
     * @param inputStream input stream
     * @return wrapper instance
     */
    public static XMLEventReaderWrapper newInstance(InputStream inputStream) {
        return newInstance(inputStream, Set.of());
    }

    /**
     * Creates new instance of {@link XMLStreamWriterWrapper}.
     *
     * @param inputStream input stream
     * @return wrapper instance
     */
    public static XMLEventReaderWrapper newInstance(InputStream inputStream, Set<SerializationOption> options) {
        try {
            return new XMLEventReaderWrapper(
                    INPUT_FACTORY.createXMLEventReader(inputStream),
                    options.contains(SerializationOption.LOCAL_DATE_AS_EPOCH_DAY)
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private XMLEventReaderWrapper(XMLEventReader reader, boolean localDateAsEpochDay) {
        this.reader = reader;
        this.localDateAsEpochDay = localDateAsEpochDay;
    }

    boolean isLocalDateAsEpochDay() {
        return localDateAsEpochDay;
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
            return new XMLEventWrapper(reader.nextEvent(), this);
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
            return event == null ? Optional.empty() : Optional.of(new XMLEventWrapper(event, this));
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

    /**
     * Reads the content of a text-only element as boolean.
     *
     * @param defaultValue default value
     * @return boolean value
     */
    public boolean getElementValue(boolean defaultValue) {
        return getElementText().map(Boolean::parseBoolean).orElse(defaultValue);
    }

    /**
     * Reads the content of a text-only element as integer.
     *
     * @param defaultValue default value
     * @return integer value
     */
    public int getElementValue(int defaultValue) {
        return getElementText().map(Integer::parseInt).orElse(defaultValue);
    }


    /**
     * Reads the content of a text-only element as long.
     *
     * @param defaultValue default value
     * @return long value
     */
    public long getElementValue(long defaultValue) {
        return getElementText().map(Long::parseLong).orElse(defaultValue);
    }

    /**
     * Reads the content of a text-only element as typed value.
     *
     * @param <T>  expected value type
     * @param type value type
     * @return value
     */
    public <T> Optional<T> getElementValue(Class<T> type) {
        return getElementText().map(text -> stringToValue(type, text, localDateAsEpochDay));
    }

    /**
     * Reads the content of a text-only element as typed value.
     *
     * @param <T>          expected value type
     * @param defaultValue default value
     * @return value or default value if element text does not exist
     */
    @SuppressWarnings("unchecked")
    public <T> T getElementValue(T defaultValue) {
        return getElementText().map(text -> (T) stringToValue(defaultValue.getClass(), text, localDateAsEpochDay))
                .orElse(defaultValue);
    }
}
