package eu.piroutek.jan.database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHandlerBase {
    protected static final String location = "kanboard.db";

    protected static final Logger LOGGER = LogManager.getLogger(DbHandlerBase.class);

    protected Connection connection;

    public DbHandlerBase() throws SQLException, ClassNotFoundException{
        try {
            this.connection = createConnection();
        } catch (SQLException exception) {
            LOGGER.error("cannot create connection to database");
            throw exception;
        }
    }

    /**
     * @return connection to sqlite database
     * @throws SQLException
     */
    protected Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + location);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.connection.close();
    }
}
