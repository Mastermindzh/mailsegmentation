package nl.quintor.ayic.mss.database.dao;

import nl.quintor.ayic.mss.database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Dao {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Dao.class));
    private Database database;

    public Dao() throws SQLException, IOException {
        setDatabase(new Database("database.properties"));
        loadDbDriver(this.database);
    }

    /**
     * Will load the DB driver.
     * @param database
     */
    public void loadDbDriver(Database database) throws SQLException {
        try {
            Class.forName(database.getDriver());
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.INFO,"Can't instantiate driver", e);
            LOGGER.severe("cannot instantiate driver" + e.getMessage());
        }
    }

    /**
     * will return the database
     * @return database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * assign the given database to the database variable
     * @param database
     */
    public void setDatabase(Database database) {
        this.database = database;
    }


    /**
     * method to log and throw SQL exceptions
     *
     * @param e exception to log and throw
     * @throws SQLException
     */
    public void logAndThrow(SQLException e) throws SQLException {
        LOGGER.log(Level.SEVERE, e.getSQLState());
        LOGGER.log(Level.SEVERE, e.getMessage());
        LOGGER.log(Level.SEVERE, "Error with database: " + database.getDriver(), e);
        throw new SQLException(e.getMessage());
    }

    public Connection getConnection() throws SQLException {
        try {
            return database.getConnection();
        } catch (SQLException e) {
            logAndThrow(e);
        }

        return null;
    }

    protected void executeAndClose(Connection conn, PreparedStatement stmt) throws SQLException {
        stmt.executeUpdate();
        closeConn(conn, stmt);
    }

    protected void closeConn(Connection conn, PreparedStatement stmt) throws SQLException {
        stmt.close();
        conn.close();
    }
}
