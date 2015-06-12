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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
