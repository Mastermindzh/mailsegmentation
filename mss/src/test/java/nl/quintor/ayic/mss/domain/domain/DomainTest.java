package nl.quintor.ayic.mss.domain.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DomainTest {

    Domain domain;

    @Before
    public void setUp() throws Exception {
        domain = new Domain("test",true);
    }

    @Test
    public void testEmptyConstructor(){
        new Domain();
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertEquals("test",domain.getIdentifier());
    }

    @Test
    public void testSetIdentifier() throws Exception {
        domain.setIdentifier("test2");
        assertEquals("test2",domain.getIdentifier());
    }

    @Test
    public void testIsActive() throws Exception {
        assertEquals(true,domain.isActive());
    }

    @Test
    public void testSetActive() throws Exception {
        domain.setActive(false);
        assertEquals(false,domain.isActive());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Domain{identifier='test', active=true}",domain.toString());
    }

    @Test
    public void testGetCountContacts() throws Exception {
        domain.setCountContacts(5);
        assertEquals(5,domain.getCountContacts());
    }
}