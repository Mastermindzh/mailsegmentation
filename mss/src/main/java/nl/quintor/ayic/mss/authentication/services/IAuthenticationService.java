package nl.quintor.ayic.mss.authentication.services;


import nl.quintor.ayic.mss.authentication.domain.Credentials;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public interface IAuthenticationService {

    /**
     * authenticate a user in the backend
     * @param credentials user credentials
     * @return http response
     */
    Response authenticateUser(Credentials credentials);

    /**
     * check whether token exists in database
     * @param token token to validate
     * @return boolean
     * @throws SQLException
     */
    boolean checkToken(String token) throws SQLException;

    /**
     * deletes token
     * @param token
     * @return
     * @throws SQLException
     */
    Response deleteToken(String token) throws SQLException;
}
