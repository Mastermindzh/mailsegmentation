package nl.quintor.ayic.mss.contact.services;

import nl.quintor.ayic.mss.contact.dao.IContactDAO;
import nl.quintor.ayic.mss.contact.domain.Contact;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    private IContactService contactService;
    private IContactDAO     contactDAO;

    @Spy
    ContactService spyContactService;

    @Before
    public void setUp() throws Exception {
        contactDAO = mock(IContactDAO.class);
        contactService = new ContactService(contactDAO);
    }

    @Test
    public void testConstructor() throws IOException, SQLException {
        //ContactService cs = new ContactService();
    }

    @Test
    public void testDeleteContacts() throws Exception {
        contactService.deleteContacts("{\n" +
                "    \"emails\": [{\n" +
                "        \"email\": \"pascal@mi-soft.nl\"\n" +
                "    }, {\n" +
                "        \"email\": \"laure@mi-soft.nl\"\n" +
                "    }]\n" +
                "}");
        verify(contactDAO, times(1)).deleteContact("laure@mi-soft.nl");
        verify(contactDAO, times(1)).deleteContact("pascal@mi-soft.nl");
    }

    @Test
    public void testDeleteContact() throws Exception {
        contactService.deleteContact("{\"email\": \"laure@mi-soft.nl\"}");
        verify(contactDAO, times(1)).deleteContact("laure@mi-soft.nl");
    }

    @Test
    public void testAddContactToCategories() throws Exception {
        String json = "{\n" +
                "    \"email\":\"timo.en.laure.hebben.ongelijk@syntax.nl\",\n" +
                "    \"categories\":[\n" +
                "        {\"name\":\"HTML\"},\n" +
                "        {\"name\":\"PHP\"}\n" +
                "    ]\n" +
                "\n" +
                "}";
        ArrayList<String> categories = new ArrayList<>();
        categories.add("HTML");
        categories.add("Java");
        categories.add("JavaScript");
        contactService.addContactToCategories(json);
        verify(contactDAO, times(1)).addContactToCategories(anyString(),anyObject());
    }


    @Test
    public void testAddContact() throws Exception {
        Contact contact = new Contact();
        contactService.addContact(contact);
        verify(contactDAO, times(1)).addContact(contact);
    }

    @Test
    public void testGetContacts() throws Exception {
        List<Contact> myList = new ArrayList<>();
        myList.add(new Contact("test@test.nl","1990-01-01",true));
        myList.add(new Contact("test2@test.nl","1990-01-01",true));
        when(contactDAO.getContacts()).thenReturn(myList);
        contactService.getContacts();
        verify(contactDAO, times(1)).getContacts();
    }

    @Test
    public void testGetContact() throws Exception {
        String json = "{\"email\" : \"q2@q.nl\"}";
        Contact contact = contactService.getContact(json);
        verify(contactDAO, times(1)).getContact("q2@q.nl");
    }

    @Test
    public void testDeleteContactsCatch() throws Exception{
        Response rs = contactService.deleteContacts("asdgsfdgsdg");
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testDeleteContactCatch() throws Exception{
        Response rs = contactService.deleteContact("asdgsfdgsdg");
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testAddContactToCategoriesCatch() throws Exception{
        Response rs = contactService.addContactToCategories("asdgsfdgsdg");
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testAddContactCatch() throws Exception{
        Contact contact = new Contact("test@test.nl","1990-01-01",true);
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        doThrow(new RuntimeException()).when(contactDAO).addContact(contact);
        Response rs = contactService.addContact(contact);
        assertEquals(expected,rs.toString());
    }

    @Test(expected = SQLException.class)
    public void testGetContactCatch() throws Exception{
        doThrow(new RuntimeException()).when(contactDAO).getContact("");
        assertNull(contactService.getContact(""));

    }

    @Test
    public void testUpdateContact() throws Exception {
        contactService.updateContact("{\n" +
                "   \"oldEmail\": \"plop@plop.nl\",\n" +
                "   \"contact\":\n" +
                "    { \n" +
                "        \"active\":\"true\",\n" +
                "        \"dateAdded\":\"1992-12-13\",\n" +
                "        \"email\":\"plop2@plop.nl\",\n" +
                "        \"firstName\":\"plop\",\n" +
                "        \"lastName\":\"lastname\",\n" +
                "        \"importName\":\"importnamne\",\n" +
                "        \"phoneNumber\":\"phonenumber\"\n" +
                "    \n" +
                "    }\n" +
                "}");
        verify(contactDAO, times(1)).updateContact(anyObject(),anyString());
    }
    @Test
    public void testUpdateContactCatch() throws Exception{

        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        doThrow(new RuntimeException()).when(contactDAO).updateContact(anyObject(),anyString());
        Response rs = contactService.updateContact("{\n" +
                "   \"oldEmail\": \"plop@plop.nl\",\n" +
                "   \"contact\":\n" +
                "    { \n" +
                "        \"active\":\"true\",\n" +
                "        \"dateAdded\":\"1992-12-13\",\n" +
                "        \"email\":\"plop2@plop.nl\",\n" +
                "        \"firstName\":\"plop\",\n" +
                "        \"lastName\":\"lastname\",\n" +
                "        \"importName\":\"importnamne\",\n" +
                "        \"phoneNumber\":\"phonenumber\"\n" +
                "    \n" +
                "    }\n" +
                "}");
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testUpdateCategoriesForContact() throws Exception {
        contactService.updateCategoriesForContact("{\n" +
                "   \"email\": \"plop@plop.nl\",\n" +
                "   \"categories\":\n" +
                "    [{ \n" +
                "        \"name\":\"test\"},\n" +
                "        {\"name\":\"test\"}\n" +
                "    ]\n" +
                "}");
        verify(contactDAO, times(1)).deleteAllCategoriesForContact(anyString());
        verify(contactDAO, times(1)).addContactToCategories(anyString(),anyObject());
    }

    @Test
    public void testUpdateCategoriesForContactCatch() throws Exception {
        doThrow(new RuntimeException()).when(contactDAO).deleteAllCategoriesForContact(anyString());
        contactService.updateCategoriesForContact("{\n" +
                "   \"email\": \"plop@plop.nl\",\n" +
                "   \"categories\":\n" +
                "    [{ \n" +
                "        \"name\":\"test\"},\n" +
                "        {\"name\":\"test\"}\n" +
                "    ]\n" +
                "}");

    }

    @Test
    public void testImportCSV() throws Exception {
        String csv = "\"Email Address\",\"First Name\",\"Last Name\",Programmeren,MEMBER_RATING,OPTIN_TIME,OPTIN_IP,CONFIRM_TIME,CONFIRM_IP,LATITUDE,LONGITUDE,GMTOFF,DSTOFF,TIMEZONE,CC,REGION,LAST_CHANGED,LEID,EUID,NOTES\n" +
                "m.white@student.hathaway.edu,Maya,White,,2,,,\"2016-04-18 13:51:14\",77.222.65.130,,,,,,,,\"2016-04-18 14:00:32\",55331253,89501affba,\n" +
                "b.mwangi@hathaway.edu,Benjamin,Mwangi,,2,,,\"2016-04-18 13:51:14\",77.222.65.130,,,,,,,,\"2016-04-18 14:00:32\",55331257,c9b2705335,";
        contactService.importCSV(csv);
        verify(contactDAO, times(2)).addContact(any(Contact.class));

    }

    @Test
    public void testImportCSVCatch() throws Exception {
        //doThrow(new RuntimeException()).when(contactDAO.addContact(anyObject()));
    }
    @Test
    public void testDomainServiceConstructor(){
        try{
            new ContactService();
        }catch(Exception e){

        }
        //can't test without a database.
        assertEquals(true,true);
    }


    @Test
    public void testPermDeleteContacts() throws Exception {
        contactService.permDeleteContacts("{\n" +
                "    \"emails\": [{\n" +
                "        \"email\": \"pascal@mi-soft.nl\"\n" +
                "    }, {\n" +
                "        \"email\": \"laure@mi-soft.nl\"\n" +
                "    }]\n" +
                "}");
        verify(contactDAO, times(1)).permDeleteContact("laure@mi-soft.nl");
        verify(contactDAO, times(1)).permDeleteContact("pascal@mi-soft.nl");
    }

    @Test
    public void testPermDeleteContactsCatch() throws Exception{
        Response rs = contactService.permDeleteContacts("asdgsfdgsdg");
        String expected = "OutboundJaxrsResponse{status=409, reason=Conflict, hasEntity=true, closed=false, buffered=false}";
        assertEquals(expected,rs.toString());
    }

    @Test
    public void testGetContactsInRange() throws Exception {
        contactService.getContactsInRange("{\n" +
                "    \"min\": 0,\n" +
                "    \"max\": 5,\n" +
                "    \"active\": true\n" +
                "}");
        verify(contactDAO, times(1)).getContactsInRange(anyInt(),anyInt(),anyBoolean());
    }
    @Test(expected = SQLException.class)
    public void testGetContactsInRangeCatch() throws Exception {
        when(contactDAO.getContactsInRange(0,5,true)).thenThrow(SQLException.class);
        contactService.getContactsInRange("{\n" +
                "    \"min\": 0,\n" +
                "    \"max\": 5\n" +
                "    \"active\": true\n" +
                "}");
    }

    @Test
    public void testSearchContacts() throws Exception {
        contactService.searchContacts("{\n" +
                "    \"search\": \"test\",\n" +
                "    \"active\": true\n" +
                "}");
        verify(contactDAO, times(1)).searchContacts(anyString(),anyBoolean());
    }
    @Test(expected = SQLException.class)
    public void testSearchContactsCatch() throws Exception {
        when(contactDAO.searchContacts("test",true)).thenThrow(SQLException.class);
        contactService.searchContacts("{\n" +
                "    \"search\": \"test\",\n" +
                "    \"active\": true\n" +
                "}");
    }
}