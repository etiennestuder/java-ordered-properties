package nu.studer.java.util

import spock.lang.Specification


class OrderedPropertiesTest extends Specification {

    def props = new OrderedProperties()

    def "empty properties"() {
        setup:
        assert props.isEmpty()
    }

    def "get property without default value specified"() {
        setup:
        props.setProperty("aaa", "111")
        assert props.getProperty("aaa") == "111"
        assert props.getProperty("bbb") == null

        props.setProperty("bbb", null)
        assert props.getProperty("bbb") == null
    }

    def "get property with default value specified"() {
        setup:
        props.setProperty("aaa", "111")
        assert props.getProperty("aaa", "222") == "111"
        assert props.getProperty("bbb", "222") == "222"

        props.setProperty("bbb", null)
        assert props.getProperty("bbb", "222") == "222"
    }

    def "properties remain ordered in toString()"() {
        setup:
        props.setProperty("bbb", "222")
        props.setProperty("ccc", "333")
        props.setProperty("aaa", "111")

        assert props.toString() == "{bbb=222, ccc=333, aaa=111}"
    }

    def "properties remain ordered when loading from stream"() {
        setup:
        def stream = asStream """\
b=222
c=333
a=111
"""
        when:
        props.load(stream)

        then:
        props.propertyNames().toList() == ["b", "c", "a"]
        props.stringPropertyNames() == ["b", "c", "a"] as Set
        props.getProperty("b") == "222"
        props.getProperty("c") == "333"
        props.getProperty("a") == "111"
        props.getProperty("d") == null
    }

    def "properties remain ordered when loading from reader"() {
        setup:
        def reader = asReader """\
b=222
c=333
a=111
"""
        when:
        props.load(reader)

        then:
        props.propertyNames().toList() == ["b", "c", "a"]
        props.stringPropertyNames() == ["b", "c", "a"] as Set
        props.getProperty("b") == "222"
        props.getProperty("c") == "333"
        props.getProperty("a") == "111"
        props.getProperty("d") == null
    }

    def "properties remain ordered when loading from stream as xml"() {
        setup:
        def stream = asStream """\
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
  <entry key="b">222</entry>
  <entry key="c">333</entry>
  <entry key="a">111</entry>
</properties>
"""
        when:
        props.loadFromXML(stream)

        then:
        props.propertyNames().toList() == ["b", "c", "a"]
        props.stringPropertyNames() == ["b", "c", "a"] as Set
        props.getProperty("b") == "222"
        props.getProperty("c") == "333"
        props.getProperty("a") == "111"
        props.getProperty("d") == null
    }


    def "properties remain ordered when writing to stream"() {
        setup:
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def stream = new ByteArrayOutputStream()

        when:
        props.store(stream, null)

        then:
        stream.toString() endsWith """
b=222
c=333
a=111
"""
    }

    def "properties remain ordered when writing to writer"() {
        setup:
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def writer = new StringWriter()

        when:
        props.store(writer, null)

        then:
        writer.toString() endsWith """
b=222
c=333
a=111
"""
    }

    def "properties remain ordered when writing to stream as xml"() {
        setup:
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def stream = new ByteArrayOutputStream()

        when:
        props.storeToXML(stream, "foo")

        then:
        stream.toString() == """\
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<comment>foo</comment>
<entry key="b">222</entry>
<entry key="c">333</entry>
<entry key="a">111</entry>
</properties>
"""
    }

    def "properties remain ordered when writing to stream as xml with custom encoding"() {
        setup:
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def stream = new ByteArrayOutputStream()

        when:
        props.storeToXML(stream, "foo", "ISO-8859-1")

        then:
        stream.toString() == """\
<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<comment>foo</comment>
<entry key="b">222</entry>
<entry key="c">333</entry>
<entry key="a">111</entry>
</properties>
"""
    }

    def "date can be suppressed when writing to stream without comment"() {
        setup:
        props = OrderedProperties.withoutWritingDateComment()
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def stream = new ByteArrayOutputStream()

        when:
        props.store(stream, null)

        then:
        stream.toString() == """\
b=222
c=333
a=111
"""
    }

    def "date can be suppressed for empty set of properties when writing to stream without comment"() {
        setup:
        props = OrderedProperties.withoutWritingDateComment()
        def stream = new ByteArrayOutputStream()

        when:
        props.store(stream, null)

        then:
        stream.toString() == ""
    }

    def "date can be suppressed when writing to stream with comment"() {
        setup:
        props = OrderedProperties.withoutWritingDateComment()
        props.setProperty("b", "222")
        props.setProperty("c", "333")
        props.setProperty("a", "111")
        def stream = new ByteArrayOutputStream()

        when:
        props.store(stream, "some comment")

        then:
        stream.toString() == """\
#some comment
b=222
c=333
a=111
"""
    }

    def "date can be suppressed for empty set of properties when writing to stream with comment"() {
        setup:
        props = OrderedProperties.withoutWritingDateComment()
        def stream = new ByteArrayOutputStream()

        when:
        props.store(stream, "some comment")

        then:
        stream.toString() == """\
#some comment
"""
    }


    private static Reader asReader(String text) {
        new StringReader(text)
    }

    private static InputStream asStream(String text) {
        new ByteArrayInputStream(text.getBytes("ISO-8859-1"))
    }

}
