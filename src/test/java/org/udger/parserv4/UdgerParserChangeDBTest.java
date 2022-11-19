package org.udger.parserv4;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.junit.Test;

public class UdgerParserChangeDBTest {

    //@Test
    public void testUaString1() throws SQLException, IOException {
        String uaQuery = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";
        URL resource = this.getClass().getClassLoader().getResource("udgerdb_test_v4.dat");
        UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData(resource.getFile());
        try (UdgerParser parser = new UdgerParser(parserDbData)) {

          UdgerUaResult qr = parser.parseUa(uaQuery);
          assertEquals("50", qr.getUaUptodateCurrentVersion());
        }

        URL resource2 = this.getClass().getClassLoader().getResource("udgerdb_v4_test_switch.dat");
        UdgerParser.ParserDbData parserDbData2 = new UdgerParser.ParserDbData(resource2.getFile());
        try (UdgerParser parser2 = new UdgerParser(parserDbData2)) {
          UdgerUaResult qr2 = parser2.parseUa(uaQuery);
          assertEquals("50X", qr2.getUaUptodateCurrentVersion());
        }
    }
}
