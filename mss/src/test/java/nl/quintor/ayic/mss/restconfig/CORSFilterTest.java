package nl.quintor.ayic.mss.restconfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

public class CORSFilterTest {

    CORSFilter filter;

    @Before
    public void setUp() throws Exception{
        filter = new CORSFilter();
    }

    @Test
    public void Constructor() throws Exception{
        CORSFilter filter = new CORSFilter();
    }

    @Test(expected = NullPointerException.class)
    public void testFilter() throws Exception{
        filter.filter(Mockito.mock(ContainerRequestContext.class), Mockito.mock(ContainerResponseContext.class));
    }
}