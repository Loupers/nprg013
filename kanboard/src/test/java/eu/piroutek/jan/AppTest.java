package eu.piroutek.jan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;



/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void sqliteWorking() {
	Connection c = null;

	try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
	} catch (Exception exception) {
		assertTrue(false);
	}
      System.out.println("Opened database successfully");

    }

    @Test
    public void loggerWorking() {
	Logger logger = LogManager.getLogger(AppTest.class);
	logger.error("test error");

    }
}
