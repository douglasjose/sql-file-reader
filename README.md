# SQL File Reader

## Summary

Externalize SQL statements to `.sql` files that can be consumed directly by your Java application.

## Motivation

Allow developers to externalize their queries in a SQL-friendly format, without having to worry about limitations of other formats, such as properties and XML files, that were not designed to support SQL queries.

SQL files can be easily consumed by database clients while preserving the queries formatting and leveraging the SQL support of text editors and IDEs.

## Basic Usage

Declare your queries in a `.sql` file (e.g., `queries.sql`). Name your queries using the `#queryName` format in the comments:

```sql
-- #selectAllCustomers
SELECT * FROM CUSTOMER;

-- #selectCustomerByLastName
SELECT * FROM CUSTOMER WHERE LASTNAME = ?;

```

Initialize a `SQLFileReader` object

```java
InputStream is = getClass().getClassLoader().getResourceAsStream("queries.sql");
SQLFileReader sqlFile = new SQLFileReader(is);
String query = sqlFile.query("selectAllCustomers");
```

## Logging

Enable `INFO` logging for `com.douglasjose.tech` to log information about the queries:

```
[main] INFO  com.douglasjose.tech.SQLFileReader - 2 queries initialized: [selectAllCustomers, selectCustomerByLastName]
```

## License
