package com.douglasjose.tech;

import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFileReaderTest {

    @Test
    public void dummy() {
        InputStream basicSQL = SQLFileReaderTest.class.getClassLoader().getResourceAsStream("basic.sql");
        assert basicSQL != null;
        SQLFileReader sfr = new SQLFileReader(basicSQL);

        for (String queryName : sfr.queryNames()) {
            System.out.println("Query: " + queryName);
            System.out.println(sfr.query(queryName));
        }

    }
}
