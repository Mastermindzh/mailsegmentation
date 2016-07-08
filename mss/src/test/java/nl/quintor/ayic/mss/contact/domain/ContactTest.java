package nl.quintor.ayic.mss.contact.domain;

import nl.quintor.ayic.mss.category.domain.Category;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContactTest {

    Contact contact;

    @Before
    public void setUp() throws Exception {
        contact = new Contact("test@mail.nl","1990-01-01",false);
        contact.setFirstName("firstname");
        contact.setLastName("lastname");
        contact.setPhoneNumber("+31612345678");
        contact.setImportName("import");
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("test@mail.nl",contact.getEmail());
    }

    @Test
    public void testSetEmail() throws Exception {
        contact.setEmail("test1@mail.nl");
        assertEquals("test1@mail.nl",contact.getEmail());
    }

    @Test
    public void testGetFirstName() throws Exception {
        assertEquals("firstname",contact.getFirstName());
    }

    @Test
    public void testSetFirstName() throws Exception {
        contact.setFirstName("method");
        assertEquals("method",contact.getFirstName());
    }

    @Test
    public void testGetLastName() throws Exception {
        assertEquals("lastname",contact.getLastName());
    }

    @Test
    public void testSetLastName() throws Exception {
        contact.setLastName("method");
        assertEquals("method", contact.getLastName());
    }

    @Test
    public void testGetPhoneNumber() throws Exception {
        assertEquals("+31612345678",contact.getPhoneNumber());
    }

    @Test
    public void testSetPhoneNumber() throws Exception {
        contact.setPhoneNumber("phonenumber");
        assertEquals("phonenumber",contact.getPhoneNumber());
    }

    @Test
    public void testGetDateAdded() throws Exception {
        assertEquals("1990-01-01",contact.getDateAdded());
    }

    @Test
    public void testSetDateAdded() throws Exception {
        contact.setDateAdded("1991-01-01");
        assertEquals("1991-01-01",contact.getDateAdded());
    }

    @Test
    public void testIsActive() throws Exception {
        assertEquals(false,contact.isActive());
    }

    @Test
    public void testSetActive() throws Exception {
        contact.setActive(true);
        assertEquals(true,contact.isActive());
    }

    @Test
    public void testGetImportName() throws Exception {
        assertEquals("import",contact.getImportName());
    }

    @Test
    public void testSetImportName() throws Exception {
        contact.setImportName("method");
        assertEquals("method",contact.getImportName());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(
            "Contact{email='test@mail.nl', firstName='firstname', lastName='lastname', phoneNumber='+31612345678', dateAdded='1990-01-01', active=false, importName='import'}"
            ,contact.toString());
    }

    @Test
    public void testConstructor() throws Exception{
        Contact contact1 = new Contact();
    }

    @Test
    public void testGetContacts() throws Exception {
        List<Category> myList = new ArrayList<>();
        contact.setCategories(myList);
        assertEquals(myList,contact.getCategories());
    }

}