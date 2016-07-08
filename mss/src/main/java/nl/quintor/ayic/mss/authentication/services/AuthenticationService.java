package nl.quintor.ayic.mss.authentication.services;

import nl.quintor.ayic.mss.authentication.dao.AuthenticationDAO;
import nl.quintor.ayic.mss.authentication.dao.IAuthenticationDAO;
import nl.quintor.ayic.mss.authentication.domain.Credentials;
import nl.quintor.ayic.mss.authentication.domain.Secured;
import nl.quintor.ayic.mss.response.ResponseHandler;
import org.json.JSONObject;

import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/authentication")
public class AuthenticationService implements IAuthenticationService{

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(AuthenticationService.class));
    private IAuthenticationDAO dao;

    public AuthenticationService() throws IOException, SQLException {
        dao = new AuthenticationDAO();
    }

    public AuthenticationService(IAuthenticationDAO paramDao) throws IOException, SQLException {
        dao = paramDao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/authenticate")
    public Response authenticateUser(Credentials credentials) {
        try {
            // Authenticate the user using the credentials provided
            authenticate(credentials);
            // Issue a token for the user
            String token = issueToken(credentials.getUsername());
            // Return the token on the response
            return Response.ok(token).build();
        } catch (Exception e) {
            LOGGER.log(Level.INFO,"user authentication failed", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/checkToken")
    @Override
    public boolean checkToken(String jsonData) throws SQLException {
        JSONObject inputJSON = new JSONObject(jsonData);
        String token = inputJSON.getString("token");
        return dao.checkToken(token);
    }

    @POST
    @Consumes("application/json")
    @Path("/deleteToken")
    @Secured
    @Override
    public Response deleteToken(String jsonData) throws SQLException {
        try {
            JSONObject inputJSON = new JSONObject(jsonData);
            String token = inputJSON.getString("token");
            String result = "Token deleted";
            dao.deleteToken(token);
            return ResponseHandler.getInstance().getSuccess(result);
        } catch (Exception e) {
            LOGGER.fine("Couldn't delete token: " + e.getMessage());
            return ResponseHandler.getInstance().getFailure("Couldn't delete token: ", e);
        }
    }

    /**
     * authenticates user in the database
     * @param credentials user credentials
     * @throws Exception
     */
    private void authenticate(Credentials credentials) throws AuthenticationException, SQLException {
        String passwordToVerifyAgainst = dao.getPassword(credentials.getUsername());
        if(!passwordToVerifyAgainst.equals(credentials.getPassword())){
            throw new AuthenticationException();
        }
    }

    /**
     * issue a token to a user
     * @param username user to which token should be issued
     * @return string
     * @throws SQLException
     */
    private String issueToken(String username) throws SQLException {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        SecureRandom random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        dao.insertToken(username,token);
        return token;
    }
}