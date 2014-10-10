package nu.studer.java.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
 * This class provides a drop-in replacement for the java.util.Properties class. It fixes the design flaw of using inheritance over composition, while keeping up the same APIs as
 * the original class. As additional functionality, this class keeps its properties in a well-defined order. By default, the order is the one in which the individual properties
 * have been added, either through explicit API calls or through reading them top-to-bottom from a properties file.
 *
 * @see Properties
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class OrderedProperties {

    private static final Object LOCK = new Object();

    private final Map<String, String> properties;
    private final boolean suppressDate;

    /**
     * Creates a new instance that will keep the properties in the order they have been added.
     */
    public OrderedProperties() {
        this(false);
    }

    private OrderedProperties(boolean suppressDate) {
        this.properties = new LinkedHashMap<String, String>();
        this.suppressDate = suppressDate;
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
            if (suppressDate) {
                customProperties.store(new DateSuppressingPropertiesBufferedWriter(new OutputStreamWriter(stream, "8859_1")), comments);
            } else {
                customProperties.store(stream, comments);
            }
        }
    }

    public void store(Writer writer, String comments) throws IOException {
        CustomProperties customProperties = new CustomProperties();
        synchronized (LOCK) {
            if (suppressDate) {
                customProperties.store(new DateSuppressingPropertiesBufferedWriter(writer), comments);
            } else {
                customProperties.store(writer, comments);
            }
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

    /**
     * Creates a new instance that will omit the date comment when writing the properties to a stream.
     *
     * @return a new instance
     */
    public static OrderedProperties withoutWritingDateComment() {
        return new OrderedProperties(true);
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

    /**
     * Custom writer for storing properties that will write all leading lines of comments except
     * the last comment line. Using the JDK Properties class to store properties, the last comment
     * line always contains the current date which is what we want to filter out.
     */
    private static final class DateSuppressingPropertiesBufferedWriter extends BufferedWriter {

        private final String LINE_SEPARATOR = System.getProperty("line.separator");

        private StringBuilder currentComment;
        private String previousComment;

        private DateSuppressingPropertiesBufferedWriter(Writer out) {
            super(out);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void write(String string) throws IOException {
            if (currentComment != null) {
                currentComment.append(string);
                if (string.endsWith(LINE_SEPARATOR)) {
                    if (previousComment != null) {
                        super.write(previousComment);
                    }

                    previousComment = currentComment.toString();
                    currentComment = null;
                }
            } else if (string.startsWith("#")) {
                currentComment = new StringBuilder(string);
            } else {
                super.write(string);
            }
        }

    }

}

