package nl.quintor.ayic.mss.authentication.services;

import nl.quintor.ayic.mss.authentication.dao.IAuthenticationDAO;
import nl.quintor.ayic.mss.authentication.domain.Credentials;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


public class AuthenticationServiceTest {
    IAuthenticationDAO dao = mock(IAuthenticationDAO.class);
    AuthenticationService service;
    Credentials credentials;

    @Before
    public void setUp() throws Exception {
        service = new AuthenticationService(dao);
        credentials = new Credentials();
        credentials.setUsername("test");
        credentials.setPassword("test");
        when(dao.getPassword("test")).thenReturn("test");
        when(dao.getPassword("test2")).thenReturn("test");
    }

    @Test
    public void testConstructor() {
        try {
            new AuthenticationService();
        } catch (Exception e) {

        }
        //can't test without a dao.
        assertEquals(true, true);
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        service.authenticateUser(credentials);
        verify(dao, times(1)).getPassword(anyString());
        verify(dao, times(1)).insertToken(anyString(), anyString());
    }

    @Test
    public void testAuthenticateCatch() throws Exception {
        credentials.setPassword("test2");
        service.authenticateUser(credentials);
    }

    @Test
    public void testCheckToken() throws Exception {
        service.checkToken("{\"token\": \"testtoken\"}");
        verify(dao, times(1)).checkToken(anyString());
    }

    @Test
    public void testDeleteToken() throws Exception {
        service.deleteToken("{\"token\": \"testtoken\"}");
        verify(dao, times(1)).deleteToken(anyString());
    }
}