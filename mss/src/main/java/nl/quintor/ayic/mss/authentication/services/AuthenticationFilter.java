package nl.quintor.ayic.mss.authentication.services;

import nl.quintor.ayic.mss.authentication.dao.AuthenticationDAO;
import nl.quintor.ayic.mss.authentication.dao.IAuthenticationDAO;
import nl.quintor.ayic.mss.authentication.domain.Secured;

import javax.annotation.Priority;
import javax.naming.AuthenticationException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    IAuthenticationDAO dao;
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(AuthenticationService.class));
    /**
     * constructor
     * @throws IOException
     * @throws SQLException
     */
    public AuthenticationFilter() throws IOException, SQLException {
        dao = new AuthenticationDAO();
    }

    public AuthenticationFilter(IAuthenticationDAO paramDAO){
        dao = paramDAO;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
        try {
            validateToken(token);
        } catch (Exception e) {
            LOGGER.log(Level.INFO,"Can't verify user access",e);
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
            }
    }

    /**
     * use the database to check whether the token is registered. if it isn't throw an exception.
     * @param token token provided by the front-end
     * @throws Exception
     */
    private void validateToken(String token) throws AuthenticationException, SQLException {
        // Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        if(!dao.checkToken(token)){
            throw new AuthenticationException();
        }
    }
}
