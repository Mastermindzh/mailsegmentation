package nl.quintor.ayic.mss.guice;

import org.junit.Before;
import org.junit.Test;

public class GuiceServletConfigTest {

    GuiceServletConfig gsc;

    @Before
    public void setUp() throws Exception {
        gsc = new GuiceServletConfig();
    }

    @Test
    public void testGetInjector() throws Exception {
        gsc.getInjector();
    }
}