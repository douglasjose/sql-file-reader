package com.douglasjose.tech;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Douglas José (douglasjose@gmail.com)
 */
public class SQLFileTest {

    private static final Logger LOG = LoggerFactory.getLogger(SQLFileTest.class);

    @Test
    public void dummy() throws URISyntaxException, IOException {
        URL basic = SQLFileTest.class.getClassLoader().getResource("basic.sql");
        assert basic != null;
        LOG.debug("Reading SQL from {}", basic.toURI().getPath());

        SQLFile sfr = new SQLFile(basic.openStream());

        Set<String> queryNames = sfr.queryNames();

        assert queryNames != null;
        assert queryNames.size() == 2;
        assert queryNames.contains("myNamedQuery");
        assert queryNames.contains("anotherQuery");
        assert !queryNames.contains("invalidQuery");


        for (String queryName : sfr.queryNames()) {
            LOG.debug("Query: " + queryName);
            LOG.debug(sfr.query(queryName));
        }

        assert sfr.query("invalidQuery") == null;


    }

    @Test
    public void removeSemicolon() {
        Map<String,String> map = new HashMap<>();
        map.put("basicUsage", "select col from tab;");
        map.put("whitespacesAfter", "select col from tab;   ");
        map.put("tabsAfter", "select col from tab;\t\t\t");
        map.put("commentAfter", "select col from tab; -- This is my comment");
        map.put("literal", "select 'this;that' from dual");
        map.put("endingLiteral", "select col from 'WEIRD_TABLE;'");

        SQLFile sfr = fromMap(map);

        assert sfr.query("basicUsage").equals("select col from tab");
        assert sfr.query("whitespacesAfter").equals("select col from tab");
        assert sfr.query("tabsAfter").equals("select col from tab");
        assert sfr.query("commentAfter").equals("select col from tab");
        assert sfr.query("literal").equals("select 'this;that' from dual");
        assert sfr.query("endingLiteral").equals("select col from 'WEIRD_TABLE;'");
    }


    private SQLFile fromMap(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();

        for (String k : map.keySet()) {
            sb.append("-- #").append(k).append('\n').append(map.get(k)).append('\n');
        }

        return new SQLFile(new ByteArrayInputStream(sb.toString().getBytes()));
    }

}
