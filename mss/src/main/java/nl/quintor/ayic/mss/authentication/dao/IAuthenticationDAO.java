package nl.quintor.ayic.mss.authentication.dao;

import java.sql.SQLException;

public interface IAuthenticationDAO {

    /**
     * register a token in the database
     * @param username username to register the token for
     * @param token token to register
     * @throws SQLException
     */
    void insertToken(String username, String token) throws SQLException;

    /**
     * returns the user password from the database
     * @param username user identifier
     * @return String
     * @throws SQLException
     */
    String getPassword(String username) throws SQLException;

    /**
     * verify if the token exists in the database
     * @param token token to be checked against
     * @return true of false
     * @throws SQLException
     */
    boolean checkToken(String token) throws  SQLException;

    /**
     * delete a token from the database
     * @param token token to be deleted
     * @throws SQLException
     */
    void deleteToken(String token) throws  SQLException;

}
