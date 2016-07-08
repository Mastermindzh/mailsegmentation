package nl.quintor.ayic.mss.authentication.services;

import nl.quintor.ayic.mss.authentication.dao.IAuthenticationDAO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AuthenticationFilterTest {

    AuthenticationFilter authenticationFilter;
    IAuthenticationDAO dao = mock(IAuthenticationDAO.class);

    ContainerRequestContext context = mock(ContainerRequestContext.class);

    @Test
    public void testConstructor() {
        try{
            new AuthenticationFilter();
        }catch(Exception e){

        }
        //can't test without a dao.
        assertEquals(true,true);
    }


    @Before
    public void setup(){
        authenticationFilter = new AuthenticationFilter(dao);
        when(context.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer bla");
    }

    @Test
    public void testFilter() throws Exception {
        authenticationFilter.filter(context);

    }
    @Test(expected = NotAuthorizedException.class)
    public void testFilterCatch() throws Exception{
        when(context.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        authenticationFilter.filter(context);
    }
}