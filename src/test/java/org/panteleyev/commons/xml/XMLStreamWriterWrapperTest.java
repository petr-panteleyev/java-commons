/*
 Copyright © 2024 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.commons.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLStreamWriterWrapperTest {
    private enum TestEnum {
        ENUM_VALUE
    }

    @Test
    public void testDocument() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.document(() -> writer.element("element", "text"));
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <?xml version="1.0" ?><element>text</element>""", xml);
        }
    }

    @Test
    public void testElementWithBody() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", () -> writer.element("subElement", "text"));
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <element><subElement>text</subElement></element>""", xml);
        }
    }

    private static List<Arguments> testAttributeArguments() {
        return List.of(
                Arguments.of(
                        "name",
                        "value",
                        """
                                <element name="value"></element>"""
                ),
                Arguments.of("name",
                        LocalDateTime.of(2024, 12, 13, 1, 2, 3),
                        """
                                <element name="2024-12-13T01:02:03"></element>"""
                ),
                Arguments.of("name",
                        LocalDate.of(2024, 12, 13),
                        """
                                <element name="2024-12-13"></element>"""
                ),
                Arguments.of("name",
                        TestEnum.ENUM_VALUE,
                        """
                                <element name="ENUM_VALUE"></element>"""
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testAttributeArguments")
    public void testAttribute(String name, Object value, String expected) throws IOException {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", () -> writer.attribute(name, value));
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals(expected, xml);
        }
    }

    @Test
    public void testElementWithTextNoAttributes() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", "text");
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <element>text</element>""", xml);
        }
    }

    @Test
    public void testElementWithAttributes() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", Map.of(
                    "attr1", "123"
            ));
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <element attr1="123"></element>""", xml);
        }
    }

    @Test
    public void testElementWithAttributesAndText() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", Map.of(
                    "attr", 100
            ), "text");
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <element attr="100">text</element>""", xml);
        }
    }

    @Test
    public void testText() throws Exception {
        try (var out = new ByteArrayOutputStream(); var writer = XMLStreamWriterWrapper.newInstance(out)) {
            writer.element("element", () -> writer.text("text"));
            var xml = out.toString(StandardCharsets.UTF_8);
            assertEquals("""
                    <element>text</element>""", xml);
        }
    }
}
