package nl.quintor.ayic.mss.guice;

import org.junit.Before;
import org.junit.Test;

public class AppBindingTest {

    AppBinding ab;
    @Before
    public void setUp() throws Exception {
        ab = new AppBinding();
    }

    @Test(expected = IllegalStateException.class)
    public void testConfigureServlets() throws Exception {
        ab.configureServlets();
    }
}