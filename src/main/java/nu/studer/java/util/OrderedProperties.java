package nu.studer.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * This class provides a drop-in replacement for the java.util.Properties class. It fixes the design flaw of using
 * inheritance over composition, while keeping up the same APIs as the original class. As additional functionality, this
 * class keeps its properties in a well-defined order. By default, the order is the one in which the individual
 * properties have been added, either through explicit API calls or through reading them top-to-bottom from a properties
 * file.
 *
 * @see Properties
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class OrderedProperties {

    private static final Object LOCK = new Object();

    private final Map<String, String> properties;

    public OrderedProperties() {
        properties = new LinkedHashMap<String, String>();
    }

    public String getProperty(String key) {
        synchronized (LOCK) {
            return properties.get(key);
        }
    }

    public String getProperty(String key, String defaultValue) {
        synchronized (LOCK) {
            String value = properties.get(key);
            return (value == null) ? defaultValue : value;
        }
    }

    public String setProperty(String key, String value) {
        synchronized (LOCK) {
            return properties.put(key, value);
        }
    }

    public boolean isEmpty() {
        synchronized (LOCK) {
            return properties.isEmpty();
        }
    }

    public Enumeration<?> propertyNames() {
        synchronized (LOCK) {
            return new Vector<String>(properties.keySet()).elements();
        }
    }

    public Set<String> stringPropertyNames() {
        synchronized (LOCK) {
            return new LinkedHashSet<String>(properties.keySet());
        }
    }

    public void load(InputStream stream) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.load(stream);
        }
    }

    public void load(Reader reader) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.load(reader);
        }
    }

    @SuppressWarnings("DuplicateThrows")
    public void loadFromXML(InputStream stream) throws IOException, InvalidPropertiesFormatException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.loadFromXML(stream);
        }
    }

    public void store(OutputStream stream, String comments) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.store(stream, comments);
        }
    }

    public void store(Writer writer, String comments) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.store(writer, comments);
        }
    }

    public void storeToXML(OutputStream stream, String comment) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.storeToXML(stream, comment);
        }
    }

    public void storeToXML(OutputStream stream, String comment, String encoding) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            customProperties.storeToXML(stream, comment, encoding);
        }
    }

    @Override
    public String toString() {
        synchronized (LOCK) {
            return properties.toString();
        }
    }

    private final class CustomProperties extends Properties {

        @Override
        public Object get(Object key) {
            return properties.get(key);
        }

        @Override
        public Object put(Object key, Object value) {
            return properties.put((String) key, (String) value);
        }

        @Override
        public String getProperty(String key) {
            return properties.get(key);
        }

        @Override
        public Enumeration<Object> keys() {
            return new Vector<Object>(properties.keySet()).elements();
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public Set<Object> keySet() {
            return new LinkedHashSet<Object>(properties.keySet());
        }

    }

}

