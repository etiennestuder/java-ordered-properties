java-ordered-properties
=======================

> The work on this software project is in no way associated with my employer nor with the role I'm having at my employer. Any requests for changes will be decided upon exclusively by myself based on my personal preferences. I maintain this project as much or as little as my spare time permits.

# Overview

Alternative to the JDK class `java.util.Properties`.

The provided class `nu.studer.java.util.OrderedProperties` fixes the design flaw of using 
inheritance over composition, while keeping up the same APIs as the original class. In contrary 
to the original implementation, keys and values are guaranteed to always be of type `String`.

As additional functionality, the properties are read, stored, and iterated in a well-defined 
order. Also, an option is provided to omit the current date from being persisted as a comment 
in the specified properties file.

The java-ordered-properties dependency is hosted at [Bintray's JCenter](https://bintray.com/etienne/java-utilities/java-ordered-properties).

## Build scan

Recent build scan: https://gradle.com/s/neny3ulp4s3mc

Find out more about build scans for Gradle and Maven at https://scans.gradle.com.

# Goals

The goals of this class are the following:
 
 * Provide a non-obstructed API by using composition over inheritance
 * Ensure all APIs operate exclusively on keys and values of type `java.lang.String`
 * Allow to iterate over the properties read in from a properties file in the same order they are listed in the file 
 * Allow to store the properties to a properties file in the same order they were added or read in 
 * Make it possible to customize the ordering strategy of the properties
 * Offer a flag to omit the current date from being stored as a comment in the properties file
 * Delegate all file read / write logic to the original implementation 
 
# Functionality

All functionality is encapsulated in the class `nu.studer.java.util.OrderedProperties`.

# Design

The class `nu.studer.java.util.OrderedProperties` extends directly from `java.lang.Object`. It keeps its 
own `java.util.Map` of the properties it manages. The ordering of the properties by their keys can be customized
through a `java.util.Comparator` instance. Filtering out the current date from being stored to the properties file 
is achieved by a decorating `java.io.BufferedWriter`. Reading properties to and from a file is delegated to the 
`java.util.Properties` class since the involved logic is quite complex and code duplication is not desired.

The class `nu.studer.java.util.OrderedProperties` implements `equals` and `hashCode` based on the properties 
and the order in which they appear. The class also fulfills the contract of `java.io.Serializable`. 

# Usage

## Original functionality

Use the already existing functionality by instantiating the class `nu.studer.java.util.OrderedProperties` and 
use its APIs to set, get, iterate, persist, and load properties the same way it is done with the API of the 
class `java.util.Properties`.

```java
OrderedProperties properties = new OrderedProperties();
properties.load(new FileInputStream(new File("~/some.properties")));
String value = properties.getProperty("someKey", "someDefaultValue");
```

You can also test for the presence of a given property, and remove a given property.

```java
boolean isPresent = properties.containsProperty("someKey");
String value = properties.removeProperty("someKey");
```

## New functionality

Use the new functionality by instantiating the builder class `nu.studer.java.util.OrderedProperties.OrderedPropertiesBuilder`. You 
can then configure the builder accordingly to use a custom ordering and to omit the current date in the properties file.

```java
OrderedPropertiesBuilder builder = new OrderedPropertiesBuilder();
builder.withOrdering(String.CASE_INSENSITIVE_ORDER);
builder.withSuppressDateInComment(true);
OrderedProperties properties = builder.build();
```

An instance of `nu.studer.java.util.OrderedProperties` can be copied into a new instance through a static factory method.
 
```java
OrderedProperties copy = OrderedProperties.copyOf(sourceOrderedProperties);
```

If needed for compatibility with existing APIs that consume JDK properties, an instance of 
`nu.studer.java.util.OrderedProperties` can be converted to an instance of `java.util.Properties`.
  
```java
OrderedProperties properties = new OrderedProperties();
properties.setProperty("someKey", "someValue");
java.util.Properties jdkProperties = properties.toJdkProperties();
```

# Feedback and Contributions

Both feedback and contributions are very welcome.

# Acknowledgements

None, yet.

# License

This class is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

(c) by Etienne Studer

