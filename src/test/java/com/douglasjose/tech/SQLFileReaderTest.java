package com.douglasjose.tech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Set;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFileReaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(SQLFileReaderTest.class);

    @Test
    public void dummy() {
        InputStream basicSQL = SQLFileReaderTest.class.getClassLoader().getResourceAsStream("basic.sql");
        assert basicSQL != null;

        SQLFileReader sfr = new SQLFileReader(basicSQL);

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
