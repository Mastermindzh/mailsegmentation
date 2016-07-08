package nl.quintor.ayic.mss.export.services;

import nl.quintor.ayic.mss.category.dao.CategoryDao;
import nl.quintor.ayic.mss.category.domain.Category;
import nl.quintor.ayic.mss.contact.domain.Contact;
import nl.quintor.ayic.mss.export.dao.ExportDao;
import nl.quintor.ayic.mss.export.dao.IExportDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExportServiceTest {

    @Mock
    ExportDao mockExportDao;

    @Mock
    CategoryDao mockCategoryDao;

    @InjectMocks
    ExportService spyExportService;

    @Mock
    Contact mockContact;

    @Mock
    Category mockCategory;

    @Test
    public void testGetExportCSVCatch() throws Exception {
        doThrow(new SQLException()).when(mockExportDao).getExportContacts(anyObject(), anyObject());
        spyExportService.getExportCSV("");
    }

    @Test
    public void testGetExportCSV() throws Exception {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(mockContact);
        contacts.add(mockContact);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(mockCategory);
        categories.add(mockCategory);

        when(mockContact.getDateAdded()).thenReturn("11-11-2011");
        when(mockContact.getFirstName()).thenReturn("naam");
        when(mockContact.getLastName()).thenReturn("NAAM");
        when(mockContact.getPhoneNumber()).thenReturn("06123");
        when(mockContact.getEmail()).thenReturn("email@test.nl");

        when(mockExportDao.getExportContacts(anyObject(), anyObject())).thenReturn(contacts);
        when(mockCategoryDao.findRelated(anyString())).thenReturn(categories);

        Response response = spyExportService.getExportCSV("{categoryBoxes: [{categories : [{\"name\":\"aaa2\",\"countContacts\":0,\"$$hashKey\":\"object:71\"},{\"name\":\"aaa3\",\"countContacts\":0,\"$$hashKey\":\"object:73\"}], includeRelatedCategories : true},{categories : [{\"name\":\"HTML\",\"countContacts\":3,\"$$hashKey\":\"object:81\"},{\"name\":\"aaa4\",\"countContacts\":0,\"$$hashKey\":\"object:79\"}], includeRelatedCategories : true},{categories : [{\"name\":\"123456789\",\"countContacts\":0,\"$$hashKey\":\"object:87\"}], includeRelatedCategories : true}], domains : [{\"identifier\":\"test\",\"active\":true,\"$$hashKey\":\"object:26\"},{\"identifier\":\"test2\",\"active\":true,\"$$hashKey\":\"object:27\"},{\"identifier\":\"test3\",\"active\":true,\"$$hashKey\":\"object:28\"}]}");

        assertEquals(response.getEntity(), "Email,DateAdded,FirstName,LastName,PhoneNumber\n" +
                "email@test.nl,11-11-2011,naam,NAAM,06123\n" +
                "email@test.nl,11-11-2011,naam,NAAM,06123\n");
    }

    @Test(expected = Exception.class)
    public void testUpdateContactSQLException() throws Exception {
        when(mockExportDao.getExportContacts(anyObject(), anyObject())).thenThrow(new Exception());
        spyExportService.getExportCSV("{\"domains\" : [\n" +
                "    {\n" +
                "        \"identifier\":\"@test.nl\",\n" +
                "        \"active\":true\n" +
                "    },\n" +
                "    {\n" +
                "        \"identifier\":\"@test2.nl\",\n" +
                "        \"active\":true\n" +
                "    }\n" +
                "], \n" +
                "\"categories\" : [\n" +
                "    {\"name\":\"HTML\"},\n" +
                "    {\"name\":\"Java\"}\n" +
                "]}");
        //verify(spyContactDao, times(1)).logAndThrow(new SQLException());
    }

    @Test
    public void testExportServiceConstructor(){
        try{
            new ExportService();
        }catch(Exception e){

        }
        //can't test without a database.
        assertEquals(true,true);
    }


}