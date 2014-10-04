package nu.studer.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
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
public final class OrderedProperties {

    private final Map<String, String> properties = new LinkedHashMap<String, String>();

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.get(key);
        return (value == null) ? defaultValue : value;
    }

    public String setProperty(String key, String value) {
        return properties.put(key, value);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<?> propertyNames() {
        return new Vector<String>(properties.keySet()).elements();
    }

    public Set<String> stringPropertyNames() {
        return new LinkedHashSet<String>(properties.keySet());
    }

    public synchronized void load(InputStream stream) throws IOException {
        new Properties() {
            @Override
            public Object put(Object key, Object value) {
                return properties.put((String) key, (String) value);
            }
        }.load(stream);
    }

    public synchronized void load(Reader reader) throws IOException {
        new Properties() {
            @Override
            public Object put(Object key, Object value) {
                return properties.put((String) key, (String) value);
            }
        }.load(reader);
    }

    public synchronized void loadFromXML(InputStream stream) throws IOException, InvalidPropertiesFormatException {
        new Properties() {
            @Override
            public Object put(Object key, Object value) {
                return properties.put((String) key, (String) value);
            }
        }.loadFromXML(stream);
    }

    public void store(OutputStream out, String comments) throws IOException {
        new Properties() {
            @Override
            public synchronized Enumeration<Object> keys() {
                return new Vector<Object>(properties.keySet()).elements();
            }

            @Override
            public synchronized Object get(Object key) {
                return properties.get(key);
            }
        }.store(out, comments);
    }

    @Override
    public String toString() {
        return properties.toString();
    }

}

