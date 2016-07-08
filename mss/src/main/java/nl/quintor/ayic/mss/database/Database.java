package nl.quintor.ayic.mss.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Properties property = new Properties();
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     *
     * @param dbResource
     * @throws SQLException
     */
    public Database(String dbResource) throws SQLException, IOException {
        loadDriver(dbResource);
    }

    /**
     * loads the driver for this class
     * @param dbResource
     */
    public void loadDriver(String dbResource) throws IOException {
        try {
            property.load(getClass().getClassLoader().getResourceAsStream(dbResource));
        } catch (IOException | NullPointerException e) {
            logger.log(Level.SEVERE, "Can't access property file: " + dbResource , e);
        }
    }

    /**
     * Will return a connection to the database
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(property.getProperty("connectionstring"), property.getProperty("user"), property.getProperty("password"));
    }

    /**
     * will return the driverstring
     * @return driverstring
     */
    public String getDriver() {
        return this.property.getProperty("driver");
    }
}
