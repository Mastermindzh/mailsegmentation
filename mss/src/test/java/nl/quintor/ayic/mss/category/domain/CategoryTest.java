package nl.quintor.ayic.mss.category.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    Category category;

    @Before
    public void setUp() throws Exception {
        category = new Category("test");
        category.setCountContacts(5);
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("test",category.getName());
    }
    @Test
    public void testGetCount() throws Exception {
        category.setCountContacts(5);
        assertEquals(5,category.getCountContacts());
    }


    @Test
    public void testEmptyConstructor() throws Exception{
        Category category1 = new Category();
    }

    @Test
    public void testSetName() throws Exception {
        category.setName("test2");
        assertEquals("test2",category.getName());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Category{name='test', countContacts=5}",category.toString());
    }
}