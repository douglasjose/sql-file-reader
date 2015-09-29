/*
Copyright 2015 Douglas José (douglasjose@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.douglasjose.tech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code SQLFile} class represents an immutable set of named SQL queries.
 *
 * <p>It must be initialized with an {@link InputStream} that contains the text representation of the named SQL queries.
 *
 * <p>The name of a query is defined in the beginning of a commented line (i.e., a line starting with <code>--</code>),
 * starting with a pound sign immediately followed by the query name, that can consist of letters or numbers (but no
 * whitespaces), preceding the query declaration.
 *
 * <p>For instance, let's assume an {@code SQLFile} initialized with the contents of the following file:
 *
 * <pre>
 * -- #queryName
 * --
 * -- My query's description
 * SELECT COLUMN FROM TABLE;
 * </pre>
 *
 * <p>The corresponding object would have this internal state:
 *
 * <pre>
 * sqlFile.queryNames();        // (queryName)
 * sqlFile.query("queryName");  // "SELECT COLUMN FROM TABLE"
 * </pre>
 *
 * @see Properties
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFile implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SQLFile.class);

    private final Properties queries = new Properties();

    /**
     * Constructs a SQL file with the specified input stream
     * @param is Input stream representing the SQL file
     */
    public SQLFile(InputStream is) {
        parseFile(is);
        if (LOG.isInfoEnabled()) {
            logConfiguration();
        }
    }

    private void parseFile(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            String queryName = null;
            List<String> queryLines = null;
            while ((line = nextLine(reader)) != null) {
                if (isQueryName(line)) {
                    registerQuery(queryName, queryLines);
                    queryName = extractQueryName(line);
                    queryLines = new ArrayList<>();
                } else {
                    // Query line
                    if (queryLines != null) {
                        queryLines.add(line);
                    }
                }
            }
            registerQuery(queryName, queryLines);
        } catch (IOException e) {
            throw new IllegalArgumentException("SQL file could not be read", e);
        }
    }

    private void registerQuery(String queryName, List<String> queryLines) {
        if (queryName != null && !queryName.trim().isEmpty() && queryLines != null && !queryLines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String queryLine: queryLines) {
                sb.append(queryLine.replaceFirst("--.*$", "")).append('\n'); // Strip comments from any query lines
            }
            queries.setProperty(queryName, sb.substring(0, sb.length() - 1).replaceFirst(";\\s*$", ""));
        }
    }

    private String extractQueryName(String line) {
        Matcher matcher = Pattern.compile("#(\\w+)").matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean isQueryLine(String line) {
        return !line.trim().isEmpty() && !line.matches("^--.*");
    }

    private boolean isQueryName(String line) {
        return line.matches("^--\\s*#\\w+");
    }

    /**
     * Reads next valid line from file
     * @param reader File reader
     * @return Next valid line, or <code>null</code> if EOF.
     * @throws IOException If the file could not be read
     */
    private String nextLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !isValidLine(line) );
        return line;
    }

    private boolean isValidLine(String line) {
        return isQueryLine(line) || isQueryName(line);
    }

    /**
     * Names of all queries valid in the context of this SQL file.
     *
     * @return Set of query names declared on the SQL file
     */
    public Set<String> queryNames() {
        return queries.stringPropertyNames();
    }

    /**
     * Gets the query associated with the provided query name.
     * @param name Name of the query to be retrieved
     * @return The query identified by <code>name</code>, or <code>null</code> if
     * <code>!this.queryNames().contains(name)</code>
     */
    public String query(String name) {
        return queries.getProperty(name);
    }

    private void logConfiguration() {
        SortedSet<String> queryNames = new TreeSet<>(queryNames());
        LOG.info("{} {} initialized: {}", queryNames.size(), queryNames.size() > 1 ? "queries" : "query" , queryNames);
    }

}
