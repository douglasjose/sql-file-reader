package com.douglasjose.tech;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFileReaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(SQLFileReaderTest.class);

    @Test
    public void dummy() throws URISyntaxException, IOException {
        URL basic = SQLFileReaderTest.class.getClassLoader().getResource("basic.sql");
        assert basic != null;
        LOG.debug("Reading SQL from {}", basic.toURI().getPath());

        SQLFileReader sfr = new SQLFileReader(basic.openStream());

        Set<String> queryNames = sfr.queryNames();

        assert queryNames != null;
        assert queryNames.size() == 2;
        assert queryNames.contains("myNamedQuery");
        assert queryNames.contains("anotherQuery");

        for (String queryName : sfr.queryNames()) {
            LOG.debug("Query: " + queryName);
            LOG.debug(sfr.query(queryName));
        }


    }


}
