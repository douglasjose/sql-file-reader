package com.douglasjose.tech;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFileReader {

    private final Properties queries = new Properties();

    public SQLFileReader(InputStream is) {
        parse(is);
    }

    private void parse(InputStream is) {
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
                sb.append(queryLine).append('\n');
            }
            queries.setProperty(queryName, sb.substring(0, sb.length() - 1));
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

    public Set<String> queryNames() {
        return queries.stringPropertyNames();
    }

    public String query(String name) {
        return queries.getProperty(name);
    }
}
