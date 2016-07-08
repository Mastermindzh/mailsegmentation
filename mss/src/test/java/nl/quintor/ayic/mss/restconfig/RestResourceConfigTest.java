package nl.quintor.ayic.mss.restconfig;

import org.glassfish.hk2.api.ServiceLocator;
import org.junit.Test;
import org.mockito.Mockito;

public class RestResourceConfigTest {
    RestResourceConfig rrc;

    @Test(expected=IllegalStateException.class)
    public void testConstructor() throws Exception{
        rrc = new RestResourceConfig(Mockito.mock(ServiceLocator.class));
    }
}