# SQL File Reader

## Summary

Externalize SQL statements to `.sql` files that can be consumed directly by your Java application.

## Motivation

Allow developers to externalize their queries in a SQL-friendly format, without having to worry about limitations of other formats, such as properties and XML files, that were not designed to support SQL queries.

SQL files can be easily consumed by database clients while preserving the queries formatting and leveraging the SQL support of text editors and IDEs.

## Dependency Management

```xml
<dependencies>
  <dependency>
    <groupId>com.douglasjose.tech</groupId>
    <artifactId>sql-file-reader</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

## Basic Usage

Declare your queries in a `.sql` file (e.g., `queries.sql`). Name your queries using the `#queryName` format in the comments:

```sql
-- #selectAllCustomers
SELECT * FROM CUSTOMER;

-- #selectCustomerByLastName
SELECT * FROM CUSTOMER WHERE LASTNAME = ?;

```

Initialize a `SQLFile` object

```java
InputStream is = getClass().getClassLoader().getResourceAsStream("queries.sql");
SQLFile sqlFile = new SQLFile(is);
String query = sqlFile.query("selectAllCustomers");
```

## Logging

Enable `INFO` logging for `com.douglasjose.tech` to log information about the queries:

```
[main] INFO  com.douglasjose.tech.SQLFile - 2 queries initialized: [selectAllCustomers, selectCustomerByLastName]
```
## Continuous Integration

Master: [![Build Status](https://travis-ci.org/douglasjose/sql-file-reader.svg?branch=master)](https://travis-ci.org/douglasjose/sql-file-reader)

Development:  [![Build Status](https://travis-ci.org/douglasjose/sql-file-reader.svg?branch=development)](https://travis-ci.org/douglasjose/sql-file-reader)

## Releases

* v1.0.0 -  [jar](https://repo1.maven.org/maven2/com/douglasjose/tech/sql-file-reader/1.0.0/sql-file-reader-1.0.0.jar) - [javadoc](http://douglasjose.com/sql-file-reader/javadocs/v1.0.0/)


## License

[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
