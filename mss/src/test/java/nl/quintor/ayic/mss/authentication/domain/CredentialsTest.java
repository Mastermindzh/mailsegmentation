package nl.quintor.ayic.mss.authentication.domain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CredentialsTest {

    Credentials credentials;

    @Before
    public void setup(){
        credentials = new Credentials();
        credentials.setUsername("test");
        credentials.setPassword("test");
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals("test",credentials.getUsername());
    }

    @Test
    public void testSetUsername() throws Exception {
        credentials.setUsername("test2");
        assertEquals("test2",credentials.getUsername());
    }

    @Test
    public void testGetPassword() throws Exception {
        assertEquals("test",credentials.getPassword());
    }

    @Test
    public void testSetPassword() throws Exception {
        credentials.setPassword("test2");
        assertEquals("test2",credentials.getPassword());

    }
}