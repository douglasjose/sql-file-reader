package com.douglasjose.tech;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class PlainJdbcTest {

    private static final Logger LOG = LoggerFactory.getLogger(PlainJdbcTest.class);

    private static final String DB_URL = "jdbc:hsqldb:mem:mymemdb";
    private static final String DB_USERNAME = "SA";
    private static final String DB_PASSWORD = "";

    @Test
    public void simpleDBTest() throws SQLException, IOException {
        Connection connection = connection();
        assert connection != null;

        populateDatabase();

        URL plainJdbcFile = PlainJdbcTest.class.getClassLoader().getResource("plain-jdbc.sql");
        SQLFileReader sqlFile = new SQLFileReader(plainJdbcFile.openStream());
        String selectAllQuery = sqlFile.query("selectAllCustomers");
        assert selectAllQuery != null;

        PreparedStatement statement = connection.prepareStatement(selectAllQuery);
        ResultSet rs = statement.executeQuery();

        List<Map<String, Object>> data = readResultSet(rs);

        assert data != null;
        assert data.size() == 50;

        for (Map<String, Object> row : data) {
            LOG.debug(row.toString());
        }

        statement.close();
        rs.close();
        connection.close();

    }

    private Connection connection() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalStateException("Could not connect to database", e);
        }
    }

    private void populateDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(DB_URL, DB_USERNAME, DB_PASSWORD);
        flyway.migrate();
    }

    private List<Map<String, Object>> readResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> columnNames = columnNames(rs.getMetaData());
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String column : columnNames) {
                row.put(column, rs.getString(column));
            }
            result.add(row);
        }
        return result;
    }

    private Set<String> columnNames(ResultSetMetaData meta) throws SQLException {
        Set<String> result = new TreeSet<>();
        int cols = meta.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            result.add(meta.getColumnName(i));
        }
        return result;
    }
}
