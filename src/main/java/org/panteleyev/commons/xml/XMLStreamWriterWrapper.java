/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.panteleyev.commons.xml.Converter.valueToString;

/**
 * Implements convenience wrapper for {@link XMLStreamWriter} instances.
 * <p>Attribute values are converted to string accoring to the following rules:</p>
 * <table border="1" cellpadding="5" cellspacing="0">
 *     <tr><th>Class</th><th>String Value</th></tr>
 *     <tr><td>{@link String}</td><td>as is</td></tr>
 *     <tr><td>{@link LocalDateTime}</td><td>ISO date time</td></tr>
 *     <tr><td>{@link Enum}</td><td>{@link Enum#name()}</td></tr>
 *     <tr><td>{@link LocalDate}</td><td>{@link LocalDate#toEpochDay()} or ISO date depending on {@link SerializationOption options}.</td></tr>
 *     <tr><td>{@link Object}</td><td>{@link Object#toString()}</td></tr>
 * </table>
 */
@SuppressWarnings("UnusedReturnValue")
public class XMLStreamWriterWrapper implements AutoCloseable {
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newInstance();

    private final XMLStreamWriter writer;
    private final boolean localDateAsEpochDay;

    /**
     * Creates an instance of {@link XMLStreamWriterWrapper} with default options.
     *
     * @param out output stream
     * @return instance of {@link XMLStreamWriterWrapper}
     */
    public static XMLStreamWriterWrapper newInstance(OutputStream out) {
        return newInstance(out, Set.of());
    }

    /**
     * Creates an instance of {@link XMLStreamWriterWrapper} with specified options.
     *
     * @param out     output stream
     * @param options set of options
     * @return instance of {@link XMLStreamWriterWrapper}
     */
    public static XMLStreamWriterWrapper newInstance(OutputStream out, Set<SerializationOption> options) {
        try {
            return new XMLStreamWriterWrapper(
                    OUTPUT_FACTORY.createXMLStreamWriter(out),
                    options.contains(SerializationOption.LOCAL_DATE_AS_EPOCH_DAY)
            );
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    private XMLStreamWriterWrapper(XMLStreamWriter writer, boolean localDateAsEpochDay) {
        this.writer = writer;
        this.localDateAsEpochDay = localDateAsEpochDay;
    }

    /**
     * Closes the writer. See {@link XMLStreamWriter#close()}.
     */
    @Override
    public void close() {
        try {
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Flushes the writer. See {@link XMLStreamWriter#flush()}.
     */
    public void flush() {
        try {
            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes XML document.
     *
     * @param body block that generates document XML
     */
    public void document(Runnable body) {
        try {
            writer.writeStartDocument();
            body.run();
            writer.writeEndDocument();
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes XML document with root element.
     *
     * @param name root element name
     * @param body block that generates document XML
     */
    public void document(QName name, Runnable body) {
        try {
            writer.writeStartDocument();
            writer.writeStartElement(name.getLocalPart());
            body.run();
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes XML element.
     *
     * @param name element name
     * @param body block that generates element XML: attributes, nodes, etc.
     * @return this instance
     */
    public XMLStreamWriterWrapper element(QName name, Runnable body) {
        try {
            writer.writeStartElement(name.getLocalPart());
            body.run();
            writer.writeEndElement();
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes XML element with text.
     * <br>
     * {@code
     * <localName>text</localName>
     * }
     *
     * @param name  element name
     * @param value value converted to element text
     * @return this instance
     */
    public XMLStreamWriterWrapper textElement(QName name, Object value) {
        try {
            writer.writeStartElement(name.getLocalPart());
            writer.writeCharacters(valueToString(value, localDateAsEpochDay));
            writer.writeEndElement();
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes element with attributes.
     *
     * @param name       element name
     * @param attributes attributes
     * @return this instance
     */
    public XMLStreamWriterWrapper element(QName name, Map<QName, ?> attributes) {
        return textElement(name, attributes, null);
    }

    /**
     * Writes element with attributes and text.
     *
     * @param name       element local name
     * @param attributes attributes
     * @param value      value converted to element text
     * @return this instance
     */
    public XMLStreamWriterWrapper textElement(QName name, Map<QName, ?> attributes, Object value) {
        try {
            writer.writeStartElement(name.getLocalPart());
            this.attributes(attributes);
            if (value != null) {
                writer.writeCharacters(valueToString(value, localDateAsEpochDay));
            }
            writer.writeEndElement();
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes a single attribute.
     *
     * @param name  attribute name
     * @param value attribute value
     * @return this instance
     */
    public XMLStreamWriterWrapper attribute(QName name, Object value) {
        try {
            if (value != null) {
                writer.writeAttribute(name.getLocalPart(), valueToString(value, localDateAsEpochDay));
            }
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes several attributes.
     *
     * @param attributes attributes
     * @return this instance
     */
    public XMLStreamWriterWrapper attributes(Map<QName, ?> attributes) {
        try {
            for (var entry : attributes.entrySet()) {
                if (entry.getValue() != null) {
                    writer.writeAttribute(entry.getKey().getLocalPart(),
                            valueToString(entry.getValue(), localDateAsEpochDay));
                }
            }
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes element text.
     *
     * @param text element text
     * @return this instance
     */
    public XMLStreamWriterWrapper text(String text) {
        try {
            writer.writeCharacters(text);
            return this;
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }
}
