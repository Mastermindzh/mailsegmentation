package nl.quintor.ayic.mss.guice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class GuiceWebFilterTest {
    GuiceWebFilter gwf;

    @Before
    public void setUp() throws Exception {
        gwf = new GuiceWebFilter();
    }

    @Test(expected = ClassCastException.class)
    public void testDoFilter() throws Exception {
        gwf.doFilter(Mockito.mock(ServletRequest.class),Mockito.mock(ServletResponse.class), Mockito.mock(FilterChain.class));
    }
}